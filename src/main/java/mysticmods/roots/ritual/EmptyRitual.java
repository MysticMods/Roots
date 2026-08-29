package mysticmods.roots.ritual;

import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.util.PositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EmptyRitual extends Ritual {
  @Override
  protected void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, @Nullable PositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {

  }

  @Override
  protected void initialize(Holder<Ritual> holder) {

  }

  @Override
  protected @NotNull PropertyHolder<Property.IntegerProperty> getDurationProperty() {
    return ModRituals.EMPTY_DURATION;
  }

  @Override
  protected @NotNull PropertyHolder<Property.IntegerProperty> getIntervalProperty() {
    return ModRituals.EMPTY_INTERVAL;
  }

  @Override
  protected @Nullable PropertyHolder<Property.IntegerProperty> getRadiusXZProperty() {
    return null;
  }

  @Override
  protected @Nullable PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return null;
  }
}
