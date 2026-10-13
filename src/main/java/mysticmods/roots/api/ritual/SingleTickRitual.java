package mysticmods.roots.api.ritual;

import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.util.PositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class SingleTickRitual extends Ritual {
  public SingleTickRitual(Properties properties) {
    super(properties);
  }

  @Override
  protected void functionalTick(IRitualInstance instance, Level pLevel, BlockPos pPos, BlockState pState, @Nullable PositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    if (duration % instance.getInterval() == 0) {
      singleTick(instance ,pLevel, pPos, pState, pCache, blockEntity, duration, randomSource);
    }
  }

  protected abstract void singleTick(IRitualInstance instance, Level pLevel, BlockPos pPos, BlockState pState, @Nullable PositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource);
}
