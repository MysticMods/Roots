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

public class TimeStopEntity extends Entity {
  private static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(TimeStopEntity.class, EntityDataSerializers.INT);
  private AABB aabb;
  private SnapshotStorage snapshotStorage = new SnapshotStorage();

  public TimeStopEntity(EntityType<TimeStopEntity> timeStopEntityEntityType, Level level) {
    super(ModEntities.TEMPORAL_MORASS.get(), level);
    this.noPhysics = true;
    setNoGravity(true);
  }

  public SnapshotStorage getSnapshotStorage() {
    return snapshotStorage;
  }

  public void setSnapshot(TemporalMorassEntitySnapshot snapshot) {
    this.snapshotStorage.addSnapshot(this, ModSerializers.TEMPORAL_MORASS.get(), snapshot);
  }

  protected TemporalMorassEntitySnapshot getSnapshot() {
    SnapshotStorage storage = getSnapshotStorage();
    if (storage == null) {
      return null;
    }

    return storage.getSnapshot(this, ModSerializers.TEMPORAL_MORASS.get());
  }

  @Nullable
  protected AABB getAabb() {
    if (this.aabb == null) {
      TemporalMorassEntitySnapshot snapshot = getSnapshot();
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
    this.snapshotStorage.tick(this);
    AABB aabb = this.getAabb();
    int newLifetime = this.getLifetime() - 1;
    this.setLifetime(newLifetime);
    if (aabb != null) {
      TemporalMorassEntitySnapshot snapshot = getSnapshot();
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
    SnapshotStorage.CODEC.encodeStart(NbtOps.INSTANCE, this.snapshotStorage).result()
        .ifPresent(tag -> compound.put("snapshots", tag));
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag compound) {
    compound.putInt("lifetime", this.getLifetime());
    SnapshotStorage.CODEC.parse(NbtOps.INSTANCE, compound.get("snapshots")).result()
        .ifPresent(storage -> this.snapshotStorage = storage);
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
