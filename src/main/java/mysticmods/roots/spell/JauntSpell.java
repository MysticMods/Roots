package mysticmods.roots.spell;

import mysticmods.roots.api.data.DataMaps;
import mysticmods.roots.api.data.PropertyDataMap;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.init.ModSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JauntSpell extends Spell {
  private int jauntDistance;

  public JauntSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.INSTANT, color, costs, 0x538ad4, 0xede768);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.JAUNT_COOLDOWN;
  }

  @Override
  public List<PropertyHolder<?>> getProperties() {
    List<PropertyHolder<?>> properties = super.getProperties();
    properties.add(ModSpells.JAUNT_DISTANCE);
    return properties;
  }

  @Override
  public void initialize() {
    PropertyDataMap properties = builtInRegistryHolder().getData(DataMaps.SPELL_PROPERTY_DATA);
    this.jauntDistance = properties.get(ModSpells.JAUNT_DISTANCE);
  }

  private boolean playerSafe (Level pLevel, Player player, BlockPos.MutableBlockPos position, Direction direction) {
    int safeCount = 0;
    int startY = position.getY();
    position.setY(startY - (direction == Direction.DOWN ? 2 : 1));
    if (!pLevel.getBlockState(position).isFaceSturdy(pLevel, position, Direction.UP, SupportType.RIGID )) {
      return false;
    }

    position.setY(startY);
    if (pLevel.getBlockState(position).isPathfindable(/*pLevel, position,*/ PathComputationType.LAND)) {
      safeCount++;
    }

    // TODO: Where is the level/position sensitive version?

    position.move(direction);
    if (pLevel.getBlockState(position).isPathfindable(/*pLevel, position,*/ PathComputationType.LAND)) {
      safeCount++;
    }

    return safeCount == 2;
  }

  @Nullable
  private Vec3 findSafePosition (Level pLevel, Player player) {
    Vec3 realPos = player.position().add(Vec3.directionFromRotation(0, player.getYRot()).scale(jauntDistance));
    BlockPos real = new BlockPos(0, 0, 0); // TODO: realPos);
    BlockPos.MutableBlockPos dest = real.mutable();
    int maxHeight = pLevel.dimensionType().logicalHeight() - 1;
    int safeY = pLevel.dimensionType().minY();
    for (int i = Math.min(real.getY() + 128, maxHeight); i > real.getY(); i--) {
      dest.setY(i);
      if (playerSafe(pLevel, player, dest, Direction.UP)) {
        safeY = i;
        break;
      }
    }

    if (safeY == pLevel.dimensionType().minY()) {
      for (int i = safeY + 1; i < real.getY(); i++) {
        dest.setY(i);
        if (playerSafe(pLevel, player, dest, Direction.DOWN)) {
          safeY = i;
          break;
        }
      }
    }

    if (safeY == pLevel.dimensionType().minY()) {
      return null;
    }

    return new Vec3(real.getX() + 0.5, safeY + 0.01, real.getZ() + 0.5);
  }

  @Override
  public void cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {
    Vec3 dest = findSafePosition(pLevel, pPlayer);
    if (dest == null) {
      costs.noCharge();
    } else {
      pPlayer.teleportTo(dest.x, dest.y, dest.z);
      pPlayer.fallDistance = 0f;
    }
  }
}
