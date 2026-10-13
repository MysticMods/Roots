package mysticmods.roots.ritual;

import mysticmods.roots.api.ritual.IRitualInstance;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.util.PositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

public class EmptyRitual extends Ritual {
  public EmptyRitual(Properties properties) {
    super(properties);
  }

  @Override
  protected void functionalTick(IRitualInstance instance, Level pLevel, BlockPos pPos, BlockState pState, @Nullable PositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    throw new NotImplementedException("The empty ritual cannot be started!");
  }

  @Override
  protected void initialize(Holder<Ritual> holder) {

  }
}
