package mysticmods.roots.block;

import mysticmods.roots.init.ModBlocks;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

public class ThatchBlock extends WaterloggedBlock {
  public ThatchBlock(Properties properties) {
    super(properties);
  }

  @Override
  public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
    if (itemAbility == ItemAbilities.SHEARS_TRIM) {
      return ModBlocks.SHEARED_THATCH.get().defaultBlockState().setValue(WATERLOGGED, state.getValue(WATERLOGGED));
    }

    return super.getToolModifiedState(state, context, itemAbility, simulate);
  }
}
