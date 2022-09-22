package mysticmods.roots.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.OptionalInt;

public class BoostEntity extends Projectile {
  private static final EntityDataAccessor<OptionalInt> DATA_ATTACHED_TO_TARGET = SynchedEntityData.defineId(BoostEntity.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
  private int life;
  private int lifetime;
  @Nullable
  private LivingEntity attachedToEntity;

  public BoostEntity(EntityType<? extends BoostEntity> pEntityType, Level pLevel) {
    super(pEntityType, pLevel);
  }

  public BoostEntity(Level pLevel, double pX, double pY, double pZ) {
    super(EntityType.FIREWORK_ROCKET, pLevel);
    this.life = 0;
    this.setPos(pX, pY, pZ);
    int i = 1;
    this.setDeltaMovement(this.random.triangle(0.0D, 0.002297D), 0.05D, this.random.triangle(0.0D, 0.002297D));
    this.lifetime = 10 * i + this.random.nextInt(6) + this.random.nextInt(7);
  }

  public BoostEntity(Level pLevel, @Nullable Entity pShooter, double pX, double pY, double pZ) {
    this(pLevel, pX, pY, pZ);
    this.setOwner(pShooter);
  }

  public BoostEntity(Level pLevel, LivingEntity pShooter) {
    this(pLevel, pShooter, pShooter.getX(), pShooter.getY(), pShooter.getZ());
    this.entityData.set(DATA_ATTACHED_TO_TARGET, OptionalInt.of(pShooter.getId()));
    this.attachedToEntity = pShooter;
  }

  protected void defineSynchedData() {
    this.entityData.define(DATA_ATTACHED_TO_TARGET, OptionalInt.empty());
  }

  public boolean shouldRender(double pX, double pY, double pZ) {
    return false;
  }

  public void tick() {
    super.tick();
    if (this.isAttachedToEntity()) {
      if (this.attachedToEntity == null) {
        this.entityData.get(DATA_ATTACHED_TO_TARGET).ifPresent((p_37067_) -> {
          Entity entity = this.level.getEntity(p_37067_);
          if (entity instanceof LivingEntity) {
            this.attachedToEntity = (LivingEntity) entity;
          }

        });
      }

      if (this.attachedToEntity != null) {
        Vec3 vec3;
        Vec3 vec31 = this.attachedToEntity.getLookAngle();
        double d0 = 1.5D;
        double d1 = 0.1D;
        Vec3 vec32 = this.attachedToEntity.getDeltaMovement();
        this.attachedToEntity.setDeltaMovement(vec32.add(vec31.x * d1 + (vec31.x * d0 - vec32.x) * 0.5D, vec31.y * d1 + (vec31.y * d0 - vec32.y) * 0.5D, vec31.z * d1 + (vec31.z * d0 - vec32.z) * 0.5D));
        vec3 = this.attachedToEntity.getHandHoldingItemAngle(Items.FIREWORK_ROCKET);

        this.setPos(this.attachedToEntity.getX() + vec3.x, this.attachedToEntity.getY() + vec3.y, this.attachedToEntity.getZ() + vec3.z);
        this.setDeltaMovement(this.attachedToEntity.getDeltaMovement());
      }
    } else {
      if (!this.isShotAtAngle()) {
        double d2 = this.horizontalCollision ? 1.0D : 1.15D;
        this.setDeltaMovement(this.getDeltaMovement().multiply(d2, 1.0D, d2).add(0.0D, 0.04D, 0.0D));
      }

      Vec3 vec33 = this.getDeltaMovement();
      this.move(MoverType.SELF, vec33);
      this.setDeltaMovement(vec33);
    }

    if (!this.noPhysics) {
      this.hasImpulse = true;
    }

    this.updateRotation();
    if (this.life == 0 && !this.isSilent()) {
      this.level.playSound((Player) null, this.getX(), this.getY(), this.getZ(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.AMBIENT, 3.0F, 1.0F);
    }

    ++this.life;
    if (this.level.isClientSide && this.life % 2 < 2) {
      this.level.addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, -this.getDeltaMovement().y * 0.5D, this.random.nextGaussian() * 0.05D);
    }

  }

  private boolean isAttachedToEntity() {
    return this.entityData.get(DATA_ATTACHED_TO_TARGET).isPresent();
  }

  public boolean isShotAtAngle() {
    return true;
  }

  public void addAdditionalSaveData(CompoundTag pCompound) {
    super.addAdditionalSaveData(pCompound);
    pCompound.putInt("Life", this.life);
    pCompound.putInt("LifeTime", this.lifetime);
    if (this.attachedToEntity != null) {
      pCompound.putInt("Attached", this.attachedToEntity.getId());
    } else if (this.isAttachedToEntity()) {
      this.entityData.get(DATA_ATTACHED_TO_TARGET).ifPresent(o -> pCompound.putInt("Attached", o));
    }
  }

  public void readAdditionalSaveData(CompoundTag pCompound) {
    super.readAdditionalSaveData(pCompound);
    this.life = pCompound.getInt("Life");
    this.lifetime = pCompound.getInt("LifeTime");

  }

  public boolean isAttackable() {
    return false;
  }
}
