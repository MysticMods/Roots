package mysticmods.roots.spell;

import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.init.ModSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JauntSpell extends Spell {
  private int jauntDistance;

  public JauntSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.INSTANT, color, costs);
  }

  @Override
  public SpellProperty<Integer> getCooldownProperty() {
    return ModSpells.JAUNT_COOLDOWN.get();
  }

  @Override
  public void initialize() {
    this.jauntDistance = ModSpells.JAUNT_DISTANCE.get().getValue();
  }

  private boolean playerSafe (Level pLevel, Player player, BlockPos.MutableBlockPos position, Direction direction) {
    int safeCount = 0;
    int startY = position.getY();
    position.setY(startY - (direction == Direction.DOWN ? 2 : 1));
    if (!pLevel.getBlockState(position).isFaceSturdy(pLevel, position, Direction.UP, SupportType.RIGID )) {
      return false;
    }

    position.setY(startY);
    if (pLevel.getBlockState(position).isPathfindable(pLevel, position, PathComputationType.LAND)) {
      safeCount++;
    }

    position.move(direction);
    if (pLevel.getBlockState(position).isPathfindable(pLevel, position, PathComputationType.LAND)) {
      safeCount++;
    }

    return safeCount == 2;
  }

  @Nullable
  private Vec3 findSafePosition (Level pLevel, Player player) {
    Vec3 realPos = player.position().add(Vec3.directionFromRotation(0, player.getYRot()).scale(jauntDistance));
    BlockPos real = new BlockPos(realPos);
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
