package epicsquid.roots.entity.projectile;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.ritual.RitualFireStorm;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.List;

public class EntityFlare extends Entity {
  private static final DataParameter<Float> value = EntityDataManager.createKey(EntityFlare.class, DataSerializers.FLOAT);
  private static final double VARIANCE = 5.5;
  private int lifetime = 320;
  private double threshhold;

  public EntityFlare(World worldIn) {
    super(worldIn);
    this.setInvisible(true);
    this.getDataManager().register(value, 0f);
  }

  public void initCustom(double x, double y, double z, double vx, double vy, double vz, double value, double threshold) {
    this.posX = x;
    this.posY = y;
    this.posZ = z;
    this.motionX = vx;
    this.motionY = vy;
    this.motionZ = vz;
    this.setSize((float) value / 10.0f, (float) value / 10.0f);
    this.getDataManager().set(EntityFlare.value, (float) value);
    this.getDataManager().setDirty(EntityFlare.value);
    this.setSize((float) value / 10.0f, (float) value / 10.0f);
    this.threshhold = threshold;
  }

  @Override
  protected void entityInit() {
  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    getDataManager().set(EntityFlare.value, compound.getFloat("value"));
    getDataManager().setDirty(EntityFlare.value);
    this.threshhold = compound.getDouble("threshold");
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    compound.setFloat("value", getDataManager().get(value));
    compound.setDouble("threhsold", this.threshhold);
  }

  @Override
  public void onUpdate() {
    super.onUpdate();
    float alpha = Math.min(40, (320.0f - (float) lifetime)) / 40.0f;
    this.lifetime--;
    float val = this.getDataManager().get(value);
    this.getDataManager().set(value, val - 0.025f);

    if (this.lifetime <= 0 || val <= 0) {
      this.getEntityWorld().removeEntity(this);
      this.setDead();
    }

    this.posX += this.motionX;
    this.posY += this.motionY;
    this.posZ += this.motionZ;
    IBlockState state = getEntityWorld().getBlockState(getPosition());
    if (state.isFullCube() && state.isOpaqueCube() && this.posY <= threshhold + VARIANCE) {
      if (getEntityWorld().isRemote) {
        for (int i = 0; i < 40; i++) {
          ParticleUtil.spawnParticleFiery(getEntityWorld(), (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), 255, 96, 32, 0.5f * alpha, val + rand.nextFloat() * val, 40);
        }
      }
      List<EntityLivingBase> entities = Util.getEntitiesWithinRadius(getEntityWorld(), EntityLivingBase.class, this.getPosition(), val * 0.125f, val * 0.125f, val * 0.125f);
      this.attackWithFire(entities);
      if (world.isAirBlock(getPosition().up())) {
        world.setBlockState(getPosition().up(), ModBlocks.wild_fire.getDefaultState());
      }
      this.setDead();
    }
    if (getEntityWorld().isRemote) {
      for (double i = 0; i < 3; i++) {
        double coeff = i / 3.0;
        ParticleUtil.spawnParticleFiery(getEntityWorld(), (float) (prevPosX + (posX - prevPosX) * coeff), (float) (prevPosY + (posY - prevPosY) * coeff), (float) (prevPosZ + (posZ - prevPosZ) * coeff), 0.0125f * (rand.nextFloat() - 0.5f), 0.0125f * (rand.nextFloat() - 0.5f), 0.0125f * (rand.nextFloat() - 0.5f), 255, 96, 32, 0.5f * alpha, val + rand.nextFloat() * val, 40);
      }
    }
    List<EntityLivingBase> entities = Util.getEntitiesWithinRadius(getEntityWorld(), EntityLivingBase.class, this.getPosition(), val * 0.125f, val * 0.125f, val * 0.125f);
    if (entities.size() > 0) {
      this.attackWithFire(entities);
      if (getEntityWorld().isRemote) {
        for (int i = 0; i < 40; i++) {
          ParticleUtil.spawnParticleFiery(getEntityWorld(), (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), 255, 96, 32, 0.5f * alpha, val + rand.nextFloat() * val, 40);
        }
      }
      this.setDead();
    }
  }

  private void attackWithFire(List<EntityLivingBase> entities) {
    for (EntityLivingBase target : entities) {
      if (target instanceof EntityPlayer) {
        continue;
      }
      if (EntityUtil.isFriendly(target, RitualRegistry.ritual_fire_storm) || !EntityUtil.isHostile(target, RitualRegistry.ritual_fire_storm)) {
        continue;
      }
      DamageSource source = ModDamage.wildfireDamage(target.world);
      if (source == null) {
        continue;
      }
      target.attackEntityFrom(source, ((RitualFireStorm) RitualRegistry.ritual_fire_storm).projectile_damage);
      target.knockBack(this, ((RitualFireStorm) RitualRegistry.ritual_fire_storm).projectile_knockback, -motionX, -motionZ);
    }
  }
}