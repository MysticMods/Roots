package mysticmods.roots.ritual;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.RitualInformation;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.util.PositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;

public class HeavyStormsRitual extends Ritual {
  private float lightningChance;
  private boolean causesRain, causesThunder;
  private int weatherDuration;

  private static final BiPredicate<Level, BlockPos> AIR_ABOVE = (level, pos) -> level.isEmptyBlock(pos.above()) && !level.isEmptyBlock(pos);
  private static final List<BiPredicate<Level, BlockPos>> PREDICATES = Arrays.asList(AIR_ABOVE);

  @Override
  public List<BiPredicate<Level, BlockPos>> getPredicates() {
    return PREDICATES;
  }

  @Override
  protected void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, @Nullable PositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    if (pCache == null && requiresCache()) {
      RootsAPI.LOG.error("Ritual {} requires a PositionCache but none was provided. This will cause the ritual to not function correctly.", getOrCreateDescriptionId());
      return;
    }

    if (duration % getInterval() == 0) {
      // TODO: Weather in modded dimensions
      ServerLevel serverLevel = (ServerLevel) pLevel;
      if (causesThunder || causesRain) {
        RitualInformation info = serverLevel.getData(ModAttachments.RITUAL_INFORMATION);
        if (info.shouldStartWeather()) {
          info.startHeavyStorms();
          info.stopProtection();
          serverLevel.setWeatherParameters(0, getDuration(), causesRain, causesThunder);
        }
      }

      if (randomSource.nextFloat() < lightningChance) {
        BlockPos pos = pCache.random(AIR_ABOVE, randomSource);
        if (pos != null) {
          LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(pLevel);
          if (lightning == null) {
            return;
          }
          lightning.moveTo(Vec3.atBottomCenterOf(pos));
          pLevel.addFreshEntity(lightning);
        }
      }
    }
  }

  @Override
  public void stops(Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, RandomSource random) {
    super.stops(pLevel, pPos, pState, blockEntity, random);
    RitualInformation info = pLevel.getData(ModAttachments.RITUAL_INFORMATION);
    info.stopHeavyStorms();
  }

  @Override
  public void removed(Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, RandomSource random) {
    super.removed(pLevel, pPos, pState, blockEntity, random);
    RitualInformation info = pLevel.getData(ModAttachments.RITUAL_INFORMATION);
    info.stopHeavyStorms();
  }

  @Override
  protected void initialize(Holder<Ritual> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.RITUAL_PROPERTY_DATA);
    lightningChance = properties.get(ModRituals.HEAVY_STORMS_LIGHTNING_CHANCE);
    causesRain = properties.get(ModRituals.HEAVY_STORMS_CAUSES_RAIN);
    causesThunder = properties.get(ModRituals.HEAVY_STORMS_CAUSES_THUNDER);
    weatherDuration = properties.get(ModRituals.HEAVY_STORMS_WEATHER_DURATION);
  }

  @Override
  protected void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModRituals.HEAVY_STORMS_LIGHTNING_CHANCE);
    properties.add(ModRituals.HEAVY_STORMS_CAUSES_RAIN);
    properties.add(ModRituals.HEAVY_STORMS_CAUSES_THUNDER);
    properties.add(ModRituals.HEAVY_STORMS_WEATHER_DURATION);
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
