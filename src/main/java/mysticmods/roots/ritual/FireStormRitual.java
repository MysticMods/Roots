package mysticmods.roots.ritual;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.entity.projectile.MeteorEntity;
import mysticmods.roots.init.ModEntities;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.util.RitualPositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.List;

public class FireStormRitual extends Ritual {
  private int count;

  @Override
  protected void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, RitualPositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    if (duration % getInterval() == 0) {
      List<MeteorEntity> entities = pLevel.getEntitiesOfClass(MeteorEntity.class, pCache.getAABB());
      if (entities.size() < count) {
        MeteorEntity meteor = ModEntities.METEOR.get().create(pLevel);
        double x = pPos.getX() + 16 * (randomSource.nextDouble() - 0.5);
        double y = pPos.getY() + 10;
        double z = pPos.getZ() + 16 * (randomSource.nextDouble() - 0.5);
        if (meteor == null) {
          return;
        }
        meteor.setMinimumHeight(pPos.getY()+5);
        meteor.setPos(x, y, z);
        pLevel.addFreshEntity(meteor);
      }
    }
  }

  @Override
  protected void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {

  }

  @Override
  protected void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModRituals.FIRE_STORM_COUNT);
  }

  @Override
  protected void initialize(Holder<Ritual> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.RITUAL_PROPERTY_DATA);
    count = properties.get(ModRituals.FIRE_STORM_COUNT);
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getDurationProperty() {
    return ModRituals.FIRE_STORM_DURATION;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusXZProperty() {
    return ModRituals.FIRE_STORM_RADIUS_XZ;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModRituals.FIRE_STORM_RADIUS_Y;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getIntervalProperty() {
    return ModRituals.FIRE_STORM_INTERVAL;
  }
}
