package mysticmods.roots.entity.other;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.attachment.SnapshotStorage;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModEntities;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.snapshot.SnapshotHelper;
import mysticmods.roots.snapshot.TemporalMorassEntitySnapshot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class TemporalMorassEntity extends Entity {
  private static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(TemporalMorassEntity.class, EntityDataSerializers.INT);
  private AABB aabb;

  public TemporalMorassEntity(EntityType<TemporalMorassEntity> timeStopEntityEntityType, Level level) {
    super(ModEntities.TEMPORAL_MORASS.get(), level);
    this.noPhysics = true;
    setNoGravity(true);
  }


  @Nullable
  protected AABB getAabb() {
    if (this.aabb == null) {
      TemporalMorassEntitySnapshot snapshot = SnapshotHelper.getSnapshot(this, ModSerializers.TEMPORAL_MORASS.get());
      if (snapshot == null) {
        return null;
      }

      this.aabb = snapshot.getAABB().move(getX(), getY(), getZ());
    }
    return this.aabb;
  }

  @Override
  public void tick() {
    super.tick();
    AABB aabb = this.getAabb();
    int newLifetime = this.getLifetime() - 1;
    this.setLifetime(newLifetime);
    if (aabb != null) {
      TemporalMorassEntitySnapshot snapshot = SnapshotHelper.getSnapshot(this, ModSerializers.TEMPORAL_MORASS.get());
      for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, aabb, entity -> {
            if (entity.getType().is(RootsTags.Entities.TEMPORAL_MORASS_EXCLUDE)) {
              return false;
            }

            return true;
          }
      )) {
        TemporalMorassEntitySnapshot livingSnapshot = new TemporalMorassEntitySnapshot(living, 10, snapshot.getRadiusZX(), snapshot.getRadiusY(), 10);
        living.addEffect(new MobEffectInstance(ModEffects.TEMPORAL_MORASS, livingSnapshot.getDuration(), 0, false, false));
        SnapshotHelper.addLiving(living, ModSerializers.TEMPORAL_MORASS.get(), livingSnapshot);
      }
    }

    if (newLifetime <= 0) {
      this.discard();
    }
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    builder.define(LIFETIME, 0);
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag compound) {
    this.setLifetime(compound.getInt("lifetime"));
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag compound) {
    compound.putInt("lifetime", this.getLifetime());
  }

  public void setLifetime(int duration) {
    this.entityData.set(LIFETIME, duration);
  }

  public int getLifetime() {
    return this.entityData.get(LIFETIME);
  }

  @Override
  protected MovementEmission getMovementEmission() {
    return Entity.MovementEmission.NONE;
  }

  @Override
  public boolean isAttackable() {
    return false;
  }
}
