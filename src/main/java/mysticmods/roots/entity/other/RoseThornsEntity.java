package mysticmods.roots.entity.other;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.attachment.SnapshotStorage;
import mysticmods.roots.init.ModDamage;
import mysticmods.roots.init.ModEntities;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.snapshot.RoseThornsEntitySnapshot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class RoseThornsEntity extends Entity implements TraceableEntity {
  private static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(RoseThornsEntity.class, EntityDataSerializers.INT);
  @javax.annotation.Nullable
  private UUID ownerUUID;
  @javax.annotation.Nullable
  private Entity cachedOwner;
  private AABB aabb;
  private SnapshotStorage snapshotStorage = new SnapshotStorage();

  public RoseThornsEntity(EntityType<RoseThornsEntity> timeStopEntityEntityType, Level level) {
    super(ModEntities.ROSE_THORNS.get(), level);
    setNoGravity(false);
  }

  @Nullable
  @Override
  public Entity getOwner() {
    if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
      return this.cachedOwner;
    } else if (this.ownerUUID != null && this.level() instanceof ServerLevel serverlevel) {
      this.cachedOwner = serverlevel.getEntity(this.ownerUUID);
      return this.cachedOwner;
    } else {
      return null;
    }
  }

  public void setOwner(@javax.annotation.Nullable Entity owner) {
    if (owner != null) {
      this.ownerUUID = owner.getUUID();
      this.cachedOwner = owner;
    }
  }

  @Override
  protected double getDefaultGravity() {
    return 0.05;
  }


  public SnapshotStorage getSnapshotStorage() {
    return snapshotStorage;
  }

  protected RoseThornsEntitySnapshot getSnapshot() {
    SnapshotStorage storage = getSnapshotStorage();
    if (storage == null) {
      return null;
    }

    return storage.getSnapshot(this, ModSerializers.ROSE_THORNS.get());
  }

  public void setSnapshot(RoseThornsEntitySnapshot snapshot) {
    this.snapshotStorage.addSnapshot(this, ModSerializers.ROSE_THORNS.get(), snapshot);
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
    if (!onGround()) {
      this.applyGravity();
      this.move(MoverType.SELF, this.getDeltaMovement());
    }

    this.snapshotStorage.tick(this);
    AABB aabb = this.getAabb();
    int newLifetime = this.getLifetime() - 1;
    this.setLifetime(newLifetime);
    this.checkInsideBlocks();
    RoseThornsEntitySnapshot snapshot = this.snapshotStorage.getSnapshot(this, ModSerializers.ROSE_THORNS.get());
    if (snapshot == null) {
      RootsAPI.LOG.error("RoseThornsEntitySnapshot is null for {}", this);
    } else {
      float damage = snapshot.getDamage();
      if (aabb != null && !this.level().isClientSide()) {
        if (!this.hasExactlyOnePlayerPassenger()) {
          List<Entity> list = this.level()
              .getEntities(this, aabb, EntitySelector.pushableBy(this)
                  .and(o -> !o.getType().is(RootsTags.Entities.ROSE_THORNS_EXCLUDE) && o != getOwner()));
          if (!list.isEmpty()) {
            for (Entity entity : list) {
              if (!entity.hasPassenger(this)) {
                if (this.getPassengers().isEmpty() && !entity.isPassenger() && entity instanceof LivingEntity) {
                  entity.hurt(ModDamage.roseThorns(this, getOwner() == null ? this : getOwner()), damage);
                  entity.startRiding(this);
                } else {
                  this.push(entity);
                }
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
  protected void addAdditionalSaveData(CompoundTag compound) {
    if (this.ownerUUID != null) {
      compound.putUUID("Owner", this.ownerUUID);
    }
    compound.putInt("lifetime", this.getLifetime());
    SnapshotStorage.CODEC.parse(NbtOps.INSTANCE, compound.get("snapshots")).result()
        .ifPresent(storage -> this.snapshotStorage = storage);
  }

  protected boolean ownedBy(Entity entity) {
    return entity.getUUID().equals(this.ownerUUID);
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag compound) {
    if (compound.hasUUID("Owner")) {
      this.ownerUUID = compound.getUUID("Owner");
      this.cachedOwner = null;
    }
    this.setLifetime(compound.getInt("lifetime"));
    SnapshotStorage.CODEC.encodeStart(NbtOps.INSTANCE, this.snapshotStorage).result()
        .ifPresent(tag -> compound.put("snapshots", tag));
  }

  @Override
  public void restoreFrom(Entity entity) {
    super.restoreFrom(entity);
    if (entity instanceof RoseThornsEntity projectile) {
      this.cachedOwner = projectile.cachedOwner;
    }
  }

  public int getLifetime() {
    return this.entityData.get(LIFETIME);
  }

  public void setLifetime(int duration) {
    this.entityData.set(LIFETIME, duration);
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
