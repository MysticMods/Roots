package mysticmods.roots.entity.projectile;

import mysticmods.roots.api.attachment.SnapshotStorage;
import mysticmods.roots.init.ModDamage;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.particle.ColorGravityParticleOptions;
import mysticmods.roots.snapshot.WildfireEntitySnapshot;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class WildfireEntity extends Projectile {
  private int life;
  private SnapshotStorage snapshotStorage = new SnapshotStorage();

  public WildfireEntity(EntityType<? extends WildfireEntity> entityType, Level level) {
    super(entityType, level);
  }

  public WildfireEntity(EntityType<? extends WildfireEntity> entityType, double x, double y, double z, Level level) {
    super(entityType, level);
    this.setPos(x, y, z);

  }

  public WildfireEntity(EntityType<? extends WildfireEntity> entityType, LivingEntity owner, Level level) {
    this(entityType, owner.getX(), owner.getEyeY() - 0.1, owner.getZ(), level);
    this.setOwner(owner);
  }

  public SnapshotStorage getSnapshotStorage() {
    return snapshotStorage;
  }

  public void setSnapshot(WildfireEntitySnapshot snapshot) {
    this.snapshotStorage.addSnapshot(this, ModSerializers.WILDFIRE.get(), snapshot);
  }

  @Override
  public boolean shouldRenderAtSqrDistance(double distance) {
    double d0 = this.getBoundingBox().getSize() * 10.0;
    if (Double.isNaN(d0)) {
      d0 = 1.0;
    }

    d0 *= 64.0 * getViewScale();
    return distance < d0 * d0;
  }

  @Override
  public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
    super.shoot(x, y, z, velocity, inaccuracy);
    this.life = 0;
  }

  @Override
  public void lerpMotion(double x, double y, double z) {
    super.lerpMotion(x, y, z);
    this.life = 0;
  }

  @Override
  public void tick() {
    super.tick();
    if (!level().isClientSide) {
      tickDespawn();
    }

    this.snapshotStorage.tick(this);

    Vec3 vec3 = this.getDeltaMovement();
    if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
      double d0 = vec3.horizontalDistance();
      this.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * 180.0F / (float) Math.PI));
      this.setXRot((float) (Mth.atan2(vec3.y, d0) * 180.0F / (float) Math.PI));
      this.yRotO = this.getYRot();
      this.xRotO = this.getXRot();
    }

    BlockPos blockpos = this.blockPosition();
    BlockState blockstate = this.level().getBlockState(blockpos);
    if (!blockstate.isAir()) {
      VoxelShape voxelshape = blockstate.getCollisionShape(this.level(), blockpos);
      if (!voxelshape.isEmpty()) {
        Vec3 vec31 = this.position();

        for (AABB aabb : voxelshape.toAabbs()) {
          if (aabb.move(blockpos).contains(vec31)) {
            this.discard();
            return;
          }
        }
      }
    }

    Vec3 vec32 = this.position();
    Vec3 vec33 = vec32.add(vec3);
    HitResult hitresult = this.level()
        .clip(new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
    if (hitresult.getType() != HitResult.Type.MISS) {
      vec33 = hitresult.getLocation();
    }

    while (!this.isRemoved()) {
      EntityHitResult entityhitresult = this.findHitEntity(vec32, vec33);
      if (entityhitresult != null) {
        hitresult = entityhitresult;
      }

      if (hitresult != null && hitresult.getType() == HitResult.Type.ENTITY) {
        Entity entity = ((EntityHitResult) hitresult).getEntity();
        Entity entity1 = this.getOwner();
        if (entity instanceof Player && entity1 instanceof Player && !((Player) entity1).canHarmPlayer((Player) entity)) {
          hitresult = null;
          entityhitresult = null;
        }
      }

      if (hitresult != null && hitresult.getType() != HitResult.Type.MISS) {
        if (net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitresult))
          break;
        ProjectileDeflection projectiledeflection = this.hitTargetOrDeflectSelf(hitresult);
        this.hasImpulse = true;
        if (projectiledeflection != ProjectileDeflection.NONE) {
          break;
        }
      }

      if (entityhitresult == null) {
        break;
      }

      hitresult = null;
    }

    vec3 = this.getDeltaMovement();
    double d5 = vec3.x;
    double d6 = vec3.y;
    double d1 = vec3.z;
    for (int i = 0; i < 8; i++) {
          level().addParticle(
              new ColorGravityParticleOptions(
                  ModParticles.WILDFIRE,
                  0xe87a21,
                  0xc10000,
                  -(this.random.nextFloat() * 0.03f)
              ),
              getX() + (this.random.nextFloat() - 0.5f) * 0.15f,
              getY(),
              getZ() + (this.random.nextFloat() - 0.5f) * 0.15f,
              d5,
              d6,
              d1
          );
    }

    double d7 = this.getX() + d5;
    double d2 = this.getY() + d6;
    double d3 = this.getZ() + d1;
    double d4 = vec3.horizontalDistance();

    this.setYRot((float) (Mth.atan2(d5, d1) * 180.0F / (float) Math.PI));

    this.setXRot((float) (Mth.atan2(d6, d4) * 180.0F / (float) Math.PI));
    this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
    this.setYRot(lerpRotation(this.yRotO, this.getYRot()));

    this.setDeltaMovement(vec3.scale(1));
    this.setPos(d7, d2, d3);
    this.checkInsideBlocks();

  }

  @Override
  protected double getDefaultGravity() {
    return 0; // projectile is 0.05
  }

  @Override
  public ProjectileDeflection deflection(Projectile projectile) {
    return ProjectileDeflection.NONE;
  }

  protected void tickDespawn() {
    this.life++;
    if (this.life >= 90) {
      this.discard();
    }
  }

  @Override
  protected void onHitEntity(EntityHitResult result) {
    super.onHitEntity(result);
    Entity entity = result.getEntity();
    float f = (float) this.getDeltaMovement().length();
    // Get damage from the spell instance
    WildfireEntitySnapshot snapshot = snapshotStorage.getSnapshot(this, ModSerializers.WILDFIRE.get());
    if (snapshot != null) {
      // TODO:
      DamageSource damagesource = ModDamage.wildfire(this, getOwner() == null ? this : getOwner());

      if (getOwner() instanceof LivingEntity livingentity1) {
        livingentity1.setLastHurtMob(entity);
      }

      boolean flag = entity.getType() == EntityType.ENDERMAN;

      if (entity.hurt(damagesource, snapshot.getDamage())) {
        if (flag) {
          return;
        }

        if (entity instanceof LivingEntity livingentity) {
          this.doPostHurtEffects(livingentity);
        }

        entity.setRemainingFireTicks(120);
        // Sound
        //this.playSound(this.soundEvent, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.discard();
      } else {
        this.deflect(ProjectileDeflection.REVERSE, entity, this.getOwner(), false);
        this.setDeltaMovement(this.getDeltaMovement().scale(0.2));
        if (!this.level().isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7) {
          this.discard();
        }
      }
    }
    this.discard();
  }

  @Override
  protected void onHitBlock(BlockHitResult result) {
    super.onHitBlock(result);
    this.discard();
  }

  @Nullable
  protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
    return ProjectileUtil.getEntityHitResult(
        this.level(), this, startVec, endVec, this.getBoundingBox().expandTowards(this.getDeltaMovement())
            .inflate(2), this::canHitEntity
    );
  }

  @Override
  protected boolean canHitEntity(Entity target) {
    return super.canHitEntity(target) && target != this && target.getType() != this.getType();
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putInt("life", this.life);

    SnapshotStorage.CODEC.encodeStart(NbtOps.INSTANCE, this.snapshotStorage).result().ifPresent(tag -> compound.put("snapshots", tag));
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    this.life = compound.getInt("life");

    this.snapshotStorage = SnapshotStorage.CODEC.parse(NbtOps.INSTANCE, compound.get("snapshots")).result().orElseGet(SnapshotStorage::new);
  }

  @Override
  protected MovementEmission getMovementEmission() {
    return Entity.MovementEmission.NONE;
  }

  @Override
  public boolean isAttackable() {
    return false;
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {

  }

  protected void doPostHurtEffects(LivingEntity target) {

  }
}
