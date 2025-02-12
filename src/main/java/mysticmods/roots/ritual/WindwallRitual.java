package mysticmods.roots.ritual;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.util.EntityUtils;
import mysticmods.roots.util.RitualPositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class WindwallRitual extends Ritual {
  private float strength;
  private double minimumY, heightPercentage;

  private void knockBack (Entity entity, float strength, double x, double z) {
    // TODO: Tag entities as not being knockable
    entity.hasImpulse = true;
    Vec3 delta = entity.getDeltaMovement();
    while (x * x + z * z < 1.0e-5f) {
      x = (Math.random() - Math.random()) * 0.01;
      z = (Math.random() - Math.random()) * 0.01;
    }

    Vec3 newDelta = new Vec3(x, 0, z).normalize().scale(strength);


    entity.setDeltaMovement(delta.x / 2.0 - newDelta.x, entity.onGround() ? Math.min(0.4, delta.y / 2.0 + strength) : delta.y, delta.z / 2.0 - newDelta.z);
  }

  @Override
  protected void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, RitualPositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    if (duration % getInterval() == 0) {
      List<Entity> entities = pLevel.getEntities(null, pCache.getAABB());
      for (Entity entity : entities) {
        if (!entity.isRemoved()) {
          if (entity.getType().is(RootsTags.Entities.WINDWALL_FORCE_EXCLUDE)) {
            continue;
          }

          if (!(entity instanceof Enemy) && !entity.getType().is(RootsTags.Entities.WINDWALL_FORCE_INCLUDE)) {
            continue;
          }

          double x = pPos.getX() + 0.5 - entity.getX();
          double z = pPos.getZ() + 0.5 - entity.getZ();

          if (entity instanceof LivingEntity living) {
            living.knockback(strength, x, z);
          } else {
            knockBack(entity, strength, x, z);
          }

          // This deals with entities "stuck" against walls
          Vec3 delta = entity.getDeltaMovement();
          entity.setDeltaMovement(delta.x, minimumY + (entity.getBbHeight() * heightPercentage), delta.z);
          entity.hasImpulse = true;
        }
      }
    }
  }

  @Override
  protected void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {

  }

  @Override
  protected void initialize(Holder<Ritual> holder) {
    var properties = holder.getData(DataMaps.RITUAL_PROPERTY_DATA);
    this.strength = properties.get(ModRituals.WINDWALL_KNOCKBACK_STRENGTH);
    this.minimumY = properties.get(ModRituals.WINDWALL_MINIMUM_Y_VELOCITY);
    this.heightPercentage = properties.get(ModRituals.WINDWALL_HEIGHT_PERCENTAGE);
  }

  @Override
  protected void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModRituals.WINDWALL_KNOCKBACK_STRENGTH);
    properties.add(ModRituals.WINDWALL_MINIMUM_Y_VELOCITY);
    properties.add(ModRituals.WINDWALL_HEIGHT_PERCENTAGE);
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getDurationProperty() {
    return ModRituals.WINDWALL_DURATION;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusXZProperty() {
    return ModRituals.WINDWALL_RADIUS_XZ;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModRituals.WINDWALL_RADIUS_Y;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getIntervalProperty() {
    return ModRituals.WINDWALL_INTERVAL;
  }
}
