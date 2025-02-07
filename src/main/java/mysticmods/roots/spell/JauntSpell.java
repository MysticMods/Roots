package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// TODO: Let's just reimplement this conceptually
public class JauntSpell extends Spell {
  private int jauntDistance;

  public JauntSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.CHARGED, color, costs, 0x538ad4, 0xede768);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.JAUNT_COOLDOWN;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getMaxUseProperty() {
    return ModSpells.JAUNT_MAX_USE;
  }

  @Override
  public List<PropertyHolder<?>> getProperties() {
    List<PropertyHolder<?>> properties = super.getProperties();
    properties.add(ModSpells.JAUNT_DISTANCE);
    return properties;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.jauntDistance = properties.get(ModSpells.JAUNT_DISTANCE);
  }

  private boolean playerSafe(Level pLevel, Player player, BlockPos.MutableBlockPos position, Direction direction) {
    int safeCount = 0;
    int startY = position.getY();
    position.setY(startY - (direction == Direction.DOWN ? 2 : 1));
    if (!pLevel.getBlockState(position).isFaceSturdy(pLevel, position, Direction.UP, SupportType.RIGID)) {
      return false;
    }

    position.setY(startY);
    if (pLevel.getBlockState(position).isPathfindable(PathComputationType.AIR) && pLevel.getFluidState(position)
        .isEmpty()) {
      safeCount++;
    }

    position.move(direction);
    if (pLevel.getBlockState(position).isPathfindable(PathComputationType.AIR) && pLevel.getFluidState(position)
        .isEmpty()) {
      safeCount++;
    }

    return safeCount == 2;
  }

  @Nullable
  private Vec3 findSafePosition(Level pLevel, Player player, int charge) {
    Vec3 realPos = player.position().add(Vec3.directionFromRotation(0, player.getYRot()).scale(jauntDistance + charge));
    BlockPos real = new BlockPos((int) realPos.x, (int) realPos.y, (int) realPos.z); // TODO: realPos);
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
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    Vec3 dest = findSafePosition(pLevel, pPlayer, ticks);
    if (dest == null) {
      costs.noCharge();
      return 0;
    } else {
      pPlayer.teleportTo(dest.x, dest.y, dest.z);
      pPlayer.fallDistance = 0f;
    }

    return cooldown;
  }
}
