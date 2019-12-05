package epicsquid.roots.entity.projectile;

import java.util.List;
import java.util.UUID;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.ritual.RitualFireStorm;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FlareEntity extends Entity {
  private static final DataParameter<Float> value = EntityDataManager.createKey(FlareEntity.class, DataSerializers.FLOAT);
  private int lifetime = 320;
  private UUID id = null;

  public FlareEntity(EntityType<?> entityTypeIn, World worldIn) {
    super(entityTypeIn, worldIn);
  }

  // TODO: Custom spawn particle

/*  public FlareEntity(World worldIn) {
    super(worldIn);
    this.setInvisible(true);
    this.getDataManager().register(value, 0f);
  }*/

  public void initCustom(double x, double y, double z, double vx, double vy, double vz, double value) {
    this.posX = x;
    this.posY = y;
    this.posZ = z;
    this.setMotion(vx, vy, vz);
    // TODO: Custom sizes?
    //this.setSize((float) value / 10.0f, (float) value / 10.0f);
    this.getDataManager().set(FlareEntity.value, (float) value);
    //this.setSize((float) value / 10.0f, (float) value / 10.0f);
  }

  @Override
  public void tick() {
    super.tick();
    float alpha = Math.min(40, (320.0f - (float) lifetime)) / 40.0f;
    this.lifetime--;
    this.getDataManager().set(value, this.getDataManager().get(value) - 0.025f);

    if (this.lifetime <= 0 || this.getDataManager().get(value) <= 0) {
      this.remove();
    }

    Vec3d motion = this.getMotion();

    this.posX += motion.x;
    this.posY += motion.y;
    this.posZ += motion.z;
    BlockState state = getEntityWorld().getBlockState(getPosition());
    if (state.isSolid() && state.isOpaqueCube(getEntityWorld(), getPosition())) {
      if (getEntityWorld().isRemote) {
        for (int i = 0; i < 40; i++) {
          ParticleUtil.spawnParticleFiery(getEntityWorld(), (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f),
              0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), 255, 96, 32, 0.5f * alpha,
              getDataManager().get(value) + rand.nextFloat() * getDataManager().get(value), 40);
        }
      }
      List<LivingEntity> entities = Util
          .getEntitiesWithinRadius(getEntityWorld(), LivingEntity.class, this.getPosition(), (float) (getDataManager().get(value) * 0.125), (float) (getDataManager().get(value) * 0.125), (float) (getDataManager().get(value) * 0.125));
      this.attackWithFire(entities);
      if (world.isAirBlock(getPosition().up())) {
        world.setBlockState(getPosition().up(), ModBlocks.fey_fire.getDefaultState());
      }
      this.remove();
    }
    if (getEntityWorld().isRemote) {
      for (double i = 0; i < 3; i++) {
        double coeff = i / 3.0;
        ParticleUtil.spawnParticleFiery(getEntityWorld(), (float) (prevPosX + (posX - prevPosX) * coeff), (float) (prevPosY + (posY - prevPosY) * coeff),
            (float) (prevPosZ + (posZ - prevPosZ) * coeff), 0.0125f * (rand.nextFloat() - 0.5f), 0.0125f * (rand.nextFloat() - 0.5f),
            0.0125f * (rand.nextFloat() - 0.5f), 255, 96, 32, 0.5f * alpha, getDataManager().get(value) + rand.nextFloat() * getDataManager().get(value), 40);
      }
    }
    List<LivingEntity> entities = Util
        .getEntitiesWithinRadius(getEntityWorld(), LivingEntity.class, this.getPosition(), (float) (getDataManager().get(value) * 0.125), (float) (getDataManager().get(value) * 0.125), (float) (getDataManager().get(value) * 0.125));
    if (entities.size() > 0) {
      this.attackWithFire(entities);
      if (getEntityWorld().isRemote) {
        for (int i = 0; i < 40; i++) {
          ParticleUtil.spawnParticleFiery(getEntityWorld(), (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f),
              0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), 255, 96, 32, 0.5f * alpha,
              getDataManager().get(value) + rand.nextFloat() * getDataManager().get(value), 40);
        }
      }
      this.remove();
    }
  }

  private void attackWithFire(List<LivingEntity> entities) {
    for (LivingEntity target : entities) {
      if (target instanceof PlayerEntity) continue;
      DamageSource source = ModDamage.wildfireDamage(target.world);
      if (source == null) {
        continue;
      }
      target.attackEntityFrom(source, ((RitualFireStorm) RitualRegistry.ritual_fire_storm).projectile_damage);
      Vec3d motion = this.getMotion();
      target.knockBack(this, ((RitualFireStorm) RitualRegistry.ritual_fire_storm).projectile_knockback, -motion.x, -motion.z);
    }
  }

  @Override
  protected void registerData() {
    this.getDataManager().register(value, 0f);
  }

  @Override
  protected void readAdditional(CompoundNBT compound) {
    getDataManager().set(FlareEntity.value, compound.getFloat("value"));
    if (compound.contains("UUIDmost")) {
      id = new UUID(compound.getLong("UUIDmost"), compound.getLong("UUIDleast"));
    }
  }

  @Override
  protected void writeAdditional(CompoundNBT compound) {
    compound.putFloat("value", getDataManager().get(value));
    if (id != null) {
      compound.putLong("UUIDmost", id.getMostSignificantBits());
      compound.putLong("UUIDleast", id.getLeastSignificantBits());
    }
  }

  // TODO: This

  @Override
  public IPacket<?> createSpawnPacket() {
    return null;
  }
}