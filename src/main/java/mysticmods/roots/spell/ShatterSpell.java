package mysticmods.roots.spell;

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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShatterSpell extends Spell {
  public ShatterSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.INSTANT, color, costs, 0x606060, 0xc0c0c0);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.SHATTER_COOLDOWN;
  }

  @Override
  public void initialize(Holder<Spell> holder) {

  }

  @Override
  public Map<BlockPos, BlockState> getAffectedBlocks(Level level, Player player, ISpellInstance spell, ItemStack stack, BlockPos pos, BlockState blockState, BlockHitResult rayTraceResult) {
    Map<BlockPos, BlockState> result = new HashMap<>();

    float yaw = player.getViewYRot(1f);
    Direction playerFacing = Direction.fromYRot(yaw);
    Direction side = rayTraceResult.getDirection();
    Direction width = Direction.fromYRot(playerFacing.toYRot() + 90);
    Direction height = side == Direction.DOWN ? playerFacing : side.getAxis() == Direction.Axis.Y ? playerFacing.getOpposite() : Direction.DOWN;
    Direction depth = side.getOpposite();

    BlockPos start = pos;
    BlockPos stop = pos;

    stop = stop.relative(height);

    for (BlockPos blockPos : BlockPos.betweenClosed(start, stop)) {
      BlockState state = level.getBlockState(blockPos);
      if (state.isAir()) {
        continue;
      }
      result.put(blockPos.immutable(), state);
    }

    return result;
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    BlockHitResult rayTraceResult = pickBlock(pPlayer);
    Map<BlockPos, BlockState> toBreak = getAffectedBlocks(pLevel, pPlayer, instance, pStack, rayTraceResult.getBlockPos(), pLevel.getBlockState(rayTraceResult.getBlockPos()), rayTraceResult);
    for (Map.Entry<BlockPos, BlockState> entry : toBreak.entrySet()) {
      BlockPos pos = entry.getKey();
      BlockState state = entry.getValue();
      pLevel.destroyBlock(pos, true, pPlayer);
    }

    return cooldown;
  }
}
