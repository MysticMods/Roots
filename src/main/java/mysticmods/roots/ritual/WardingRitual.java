package mysticmods.roots.ritual;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.util.PositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import javax.annotation.Nullable;
import java.util.List;

// TODO: More
public class WardingRitual extends Ritual {
  private int potionDuration, potionAmplifier;

  @Override
  public boolean requiresCache() {
    return true;
  }

  @Override
  protected void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, @Nullable PositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    if (pCache == null && requiresCache()) {
      RootsAPI.LOG.error("Ritual {} requires a PositionCache but none was provided. This will cause the ritual to not function correctly.", getOrCreateDescriptionId());
      return;
    }

    if (duration % getInterval() == 0) {
      List<LivingEntity> entities = pLevel.getEntitiesOfClass(LivingEntity.class, pCache.getAABB());
      for (LivingEntity entity : entities) {
        entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, potionDuration, potionAmplifier, false, false), blockEntity.getLastPlayer());
      }
    }
  }

  @Override
  protected void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {

  }

  @Override
  protected void initialize(Holder<Ritual> holder) {
    var properties = holder.getData(DataMaps.RITUAL_PROPERTY_DATA);
    this.potionAmplifier = properties.get(ModRituals.WARDING_POTION_AMPLIFIER);
    this.potionDuration = properties.get(ModRituals.WARDING_POTION_DURATION);
  }

  @Override
  protected void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModRituals.WARDING_POTION_AMPLIFIER);
    properties.add(ModRituals.WARDING_POTION_DURATION);
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getDurationProperty() {
    return ModRituals.WARDING_DURATION;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusXZProperty() {
    return ModRituals.WARDING_RADIUS_XZ;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModRituals.WARDING_RADIUS_Y;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getIntervalProperty() {
    return ModRituals.WARDING_INTERVAL;
  }
}
