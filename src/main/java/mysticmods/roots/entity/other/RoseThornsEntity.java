package mysticmods.roots.entity.other;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.attachment.SnapshotStorage;
import mysticmods.roots.init.ModEntities;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.snapshot.RoseThornsEntitySnapshot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RoseThornsEntity extends Entity {
  private static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(RoseThornsEntity.class, EntityDataSerializers.INT);
  private AABB aabb;
  private SnapshotStorage snapshotStorage = new SnapshotStorage();

  public RoseThornsEntity(EntityType<RoseThornsEntity> timeStopEntityEntityType, Level level) {
    super(ModEntities.ROSE_THORNS.get(), level);
  }

  public SnapshotStorage getSnapshotStorage() {
    return snapshotStorage;
  }

  public void setSnapshot(RoseThornsEntitySnapshot snapshot) {
    this.snapshotStorage.addSnapshot(this, ModSerializers.ROSE_THORNS.get(), snapshot);
  }

  protected RoseThornsEntitySnapshot getSnapshot() {
    SnapshotStorage storage = getSnapshotStorage();
    if (storage == null) {
      return null;
    }

    return storage.getSnapshot(this, ModSerializers.ROSE_THORNS.get());
  }

  @Override
  public boolean canCollideWith(Entity entity) {
    return super.canCollideWith(entity);
  }

  @Nullable
  protected AABB getAabb() {
    if (this.aabb == null) {
      RoseThornsEntitySnapshot snapshot = getSnapshot();
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
    this.checkInsideBlocks();
    if (aabb != null && !this.level().isClientSide()) {
      if (!this.hasExactlyOnePlayerPassenger()) {
        List<Entity> list = this.level()
            .getEntities(this, aabb, EntitySelector.pushableBy(this)
                .and(o -> !o.getType().is(RootsTags.Entities.ROSE_THORNS_EXCLUDE)));
        if (!list.isEmpty()) {
          for (Entity entity : list) {
            if (!entity.hasPassenger(this)) {
              if (this.getPassengers().isEmpty() && !entity.isPassenger() && entity instanceof LivingEntity) {
                entity.hurt(this.damageSources().magic(), 1.0F);
                entity.startRiding(this);
              } else {
                this.push(entity);
              }
            }
          }
        }
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
    return MovementEmission.NONE;
  }

  @Override
  public boolean isAttackable() {
    return true;
  }

  @Override
  protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
    return new Vec3(0.0, dimensions.height() / 3.0F, 0.0f).yRot(-this.getYRot() * (float) (Math.PI / 180.0));
  }
}
