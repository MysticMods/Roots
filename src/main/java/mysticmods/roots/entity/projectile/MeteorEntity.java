package mysticmods.roots.entity.projectile;

import mysticmods.roots.init.ModParticles;
import mysticmods.roots.particle.ColorGravityParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;


public class MeteorEntity extends Entity {
  private static final EntityDataAccessor<Integer> MINIMUM_HEIGHT = SynchedEntityData.defineId(MeteorEntity.class, EntityDataSerializers.INT);

  public MeteorEntity(EntityType<?> entityType, Level level) {
    super(entityType, level);
    this.noPhysics = true;
  }

  @Override
  public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
    return super.getAddEntityPacket(entity);
  }

  @Override
  public boolean fireImmune() {
    return true;
  }

  @Override
  public void recreateFromPacket(ClientboundAddEntityPacket packet) {
    super.recreateFromPacket(packet);
    Vec3 vec3 = new Vec3(packet.getXa(), packet.getYa(), packet.getZa());
    this.setDeltaMovement(vec3);
  }

  private void setInitialMovement(Vec3 movement, double accelerationPower) {
    this.setDeltaMovement(movement.scale(accelerationPower));
    this.hasImpulse = true;
  }

  @Override
  public void tick() {
    BlockPos currentPosition = this.blockPosition();
    if (this.level().isClientSide() || this.level().hasChunkAt((this.blockPosition()))) {
      if (getDeltaMovement().equals(Vec3.ZERO)) {
        setDeltaMovement(new Vec3(0, -0.90, 0));
        this.hasImpulse = true;
      }

      if (this.level().isClientSide()) {
        for (int i = 0; i < 9; i++) {
          level().addParticle(
              new ColorGravityParticleOptions(
                  ModParticles.METEOR,
                  0xe87a21,
                  0xc10000,
                  -(this.random.nextFloat() * 0.03f)
              ),
              getX() + (this.random.nextFloat() - 0.5f) * 0.35f,
              getY(),
              getZ() + (this.random.nextFloat() - 0.5f) * 0.35f,
              0,
              -0.01f,
              0
          );
        }
      }

      super.tick();

      // We don't care about being on fire
      if (currentPosition.getY() <= getMinimumHeight() || getMinimumHeight() == 512) {
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity, ClipContext.Block.COLLIDER);
        BlockPos hit = null;
        if (hitresult.getType() == HitResult.Type.ENTITY) {
          hit = ((EntityHitResult) hitresult).getEntity().blockPosition();
        } else if (hitresult.getType() == HitResult.Type.BLOCK) {
          hit = ((BlockHitResult) hitresult).getBlockPos();
        }

        if (hit != null) {
          if (!this.level().isClientSide()) {
            if (this.level().getBlockState(hit.above()).isAir()) {
              // TODO: Fey fire
/*              this.level().setBlock(hit.above(), Blocks.FIRE.defaultBlockState(), 3);*/
            }
          }
          this.discard();
        }

        // TODO: Not sure if this really needs to be called
        this.checkInsideBlocks();
      }

      Vec3 delta = this.getDeltaMovement();
      double d0 = this.getX() + delta.x;
      double d1 = this.getY() + delta.y;
      double d2 = this.getZ() + delta.z;

      this.setDeltaMovement(delta.add(delta.normalize().scale(0.2)).scale(0.65));
      this.setPos(d0, d1, d2);
    } else {
      this.discard();
    }
  }

  @Override
  public boolean hurt(DamageSource source, float amount) {
    if (!source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
      return false;
    }

    return super.hurt(source, amount);
  }

  // Update this in a roundabout way
  protected boolean canHitEntity(Entity target) {
    if (!target.canBeHitByProjectile() || target.noPhysics) {
      return false;
    }

    if (target.getType().equals(getType())) {
      return false;
    }

    if (target instanceof Player) {
      return false;
    }

    return true;
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    builder.define(MINIMUM_HEIGHT, 512);
  }

  public void setMinimumHeight(int height) {
    this.entityData.set(MINIMUM_HEIGHT, height);
  }

  public int getMinimumHeight() {
    return this.entityData.get(MINIMUM_HEIGHT);
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag compound) {
    if (compound.contains("MinimumHeight")) {
      setMinimumHeight(compound.getInt("MinimumHeight"));
    }
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag compound) {
    compound.putInt("MinimumHeight", getMinimumHeight());
  }

  @Override
  protected void tryCheckInsideBlocks() {
    super.tryCheckInsideBlocks();
  }

  @Override
  public boolean isOnFire() {
    return true;
  }
}
