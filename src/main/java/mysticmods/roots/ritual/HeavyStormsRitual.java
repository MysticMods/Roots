package mysticmods.roots.ritual;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.List;

public class HeavyStormsRitual extends Ritual {
  private float lightningChance;
  private int lightningCount;

  @Override
  protected void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {

  }

  @Override
  protected void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {

  }

  @Override
  protected void initialize(Holder<Ritual> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.RITUAL_PROPERTY_DATA);
    lightningChance = properties.get(ModRituals.HEAVY_STORMS_LIGHTNING_CHANCE);
    lightningCount = properties.get(ModRituals.HEAVY_STORMS_LIGHTNING_COUNT);
  }

  @Override
  public List<PropertyHolder<?>> getProperties() {
    List<PropertyHolder<?>> properties = super.getProperties();
    properties.add(ModRituals.HEAVY_STORMS_LIGHTNING_CHANCE);
    properties.add(ModRituals.HEAVY_STORMS_LIGHTNING_COUNT);
    return properties;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getDurationProperty() {
    return ModRituals.HEAVY_STORMS_DURATION;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusXZProperty() {
    return ModRituals.HEAVY_STORMS_RADIUS_XZ;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModRituals.HEAVY_STORMS_RADIUS_Y;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getIntervalProperty() {
    return ModRituals.HEAVY_STORMS_INTERVAL;
  }
}
