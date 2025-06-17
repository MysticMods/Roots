package mysticmods.roots.ritual;

import mysticmods.roots.api.attachment.RitualInformation;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.util.PositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.List;

public class ProtectionRitual extends Ritual {
  private boolean clearsWeather, shortensNight, lengthensDay;
  private int dayLength, nightThreshold, clearDuration;
  private float daySpeed, nightSpeed;

  public static int getDayLength () {
    return ModRituals.PROTECTION.get().dayLength;
  }

  @Override
  protected void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, PositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    ServerLevel server = (ServerLevel) pLevel;

    long dayTime = server.getDayTime() % dayLength;

    if (dayTime % dayLength >= 0 && dayTime % dayLength < nightThreshold) {
      if (lengthensDay) {
        server.setDayTimePerTick(nightSpeed);
      }
    } else {
      if (shortensNight) {
        server.setDayTimePerTick(daySpeed);
      }
    }

    // TODO: Modded dimensions
    if (duration % getInterval() == 0 && clearsWeather) {
      // Clear the rains!
      RitualInformation info = server.getData(ModAttachments.RITUAL_INFORMATION);
      if (info.shouldStopWeather()) {
        server.setWeatherParameters(clearDuration, 0, false, false);
        info.startProtection();
        info.stopHeavyStorms();
      }
    }
  }

  @Override
  protected void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {

  }

  @Override
  public void stops(Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, RandomSource random) {
    super.stops(pLevel, pPos, pState, blockEntity, random);
    RitualInformation info = pLevel.getData(ModAttachments.RITUAL_INFORMATION);
    info.stopProtection();
  }

  @Override
  public void ends(Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, RandomSource random) {
    super.ends(pLevel, pPos, pState, blockEntity, random);

    ServerLevel server = (ServerLevel) pLevel;
    server.setDayTimePerTick(-1f);
  }

  @Override
  public void removed(Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, RandomSource random) {
    super.removed(pLevel, pPos, pState, blockEntity, random);
    ServerLevel server = (ServerLevel) pLevel;
    server.setDayTimePerTick(-1f);
    RitualInformation info = pLevel.getData(ModAttachments.RITUAL_INFORMATION);
    info.stopProtection();
  }

  @Override
  protected void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModRituals.PROTECTION_DAY_LENGTH);
    properties.add(ModRituals.PROTECTION_NIGHT_THRESHOLD);
    properties.add(ModRituals.PROTECTION_CLEAR_DURATION);
    properties.add(ModRituals.PROTECTION_CLEARS_WEATHER);
    properties.add(ModRituals.PROTECTION_SHORTENS_NIGHT);
    properties.add(ModRituals.PROTECTION_LENGTHENS_DAY);
    properties.add(ModRituals.PROTECTION_DAY_SPEED);
    properties.add(ModRituals.PROTECTION_NIGHT_SPEED);
  }

  @Override
  protected void initialize(Holder<Ritual> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.RITUAL_PROPERTY_DATA);
    dayLength = properties.get(ModRituals.PROTECTION_DAY_LENGTH);
    nightThreshold = properties.get(ModRituals.PROTECTION_NIGHT_THRESHOLD);
    clearDuration = properties.get(ModRituals.PROTECTION_CLEAR_DURATION);
    clearsWeather = properties.get(ModRituals.PROTECTION_CLEARS_WEATHER);
    shortensNight = properties.get(ModRituals.PROTECTION_SHORTENS_NIGHT);
    lengthensDay = properties.get(ModRituals.PROTECTION_LENGTHENS_DAY);
    daySpeed = properties.get(ModRituals.PROTECTION_DAY_SPEED);
    nightSpeed = properties.get(ModRituals.PROTECTION_NIGHT_SPEED);
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getDurationProperty() {
    return ModRituals.PROTECTION_DURATION;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusXZProperty() {
    return ModRituals.PROTECTION_RADIUS_XZ;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModRituals.PROTECTION_RADIUS_Y;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getIntervalProperty() {
    return ModRituals.PROTECTION_INTERVAL;
  }
}
