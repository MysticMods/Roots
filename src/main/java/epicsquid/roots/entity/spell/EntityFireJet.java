package epicsquid.roots.entity.spell;

import epicsquid.roots.init.ModDamage;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellWildfire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.List;
import java.util.UUID;

public class EntityFireJet extends Entity {
  private static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityFireJet.class, DataSerializers.VARINT);
  private UUID playerId = null;
  private double amplifier, speedy;
  private SpellWildfire instance;

  public EntityFireJet(World worldIn) {
    super(worldIn);
    this.setInvisible(true);
    this.setSize(1, 1);
    getDataManager().register(lifetime, 12);
    this.instance = SpellWildfire.instance;
  }

  public double getAmplifier() {
    return amplifier;
  }

  public void setAmplifier(double amplifier) {
    this.amplifier = amplifier;
  }

  public double getSpeedy() {
    return speedy;
  }

  public void setSpeedy(double speedy) {
    this.speedy = speedy;
  }

  public void setPlayer(UUID id) {
    this.playerId = id;
  }

  @Override
  public boolean isEntityInsideOpaqueBlock() {
    return false;
  }

  @Override
  protected void entityInit() {

  }

  @Override
  public void onUpdate() {
    getDataManager().set(lifetime, getDataManager().get(lifetime) - 1);
    getDataManager().setDirty(lifetime);
    if (getDataManager().get(lifetime) <= 0) {
      setDead();
    }
    if (world.isRemote) {
      for (int i = 0; i < 8; i++) {
        float offX = 0.5f * (float) Math.sin(Math.toRadians(rotationYaw));
        float offZ = 0.5f * (float) Math.cos(Math.toRadians(rotationYaw));
        ParticleUtil.spawnParticleFiery(world, (float) posX + (float) motionX * 2.5f + offX, (float) posY + 1.62F + (float) motionY * 2.5f, (float) posZ + (float) motionZ * 2.5f + offZ, (float) motionX + 0.125f * (rand.nextFloat() - 0.5f), (float) motionY + 0.125f * (rand.nextFloat() - 0.5f), (float) motionZ + 0.125f * (rand.nextFloat() - 0.5f), 255.0f, 96.0f, 32.0f, 0.5f, 7.5f, 24);
      }
    }
    if (this.playerId != null) {
      EntityPlayer player = world.getPlayerEntityByUUID(this.playerId);
      if (player != null) {
        this.posX = player.posX;
        this.posY = player.posY;
        this.posZ = player.posZ;
        motionX = (float) player.getLookVec().x * 0.5f;
        motionY = (float) player.getLookVec().y * 0.5f;
        motionZ = (float) player.getLookVec().z * 0.5f;
        rotationYaw = -90.0f - player.rotationYaw;
        for (float i = 0; i < 3; i++) {
          float vx = (float) player.getLookVec().x * 3.0f;
          float vy = (float) player.getLookVec().y * 3.0f;
          float vz = (float) player.getLookVec().z * 3.0f;
          List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class,
              new AxisAlignedBB(posX + vx * i - 1.5, posY + vy * i - 1.5, posZ + vz * i - 1.5, posX + vx * i + 1.5, posY + vy * i + 1.5, posZ + vz * i + 1.5));
          for (EntityLivingBase entity : entities) {
            if (!(entity instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled()) && entity != player) {
              entity.setFire((int) (instance.fire_duration + instance.fire_duration * amplifier));
              entity.attackEntityFrom(ModDamage.fireDamageFrom(player), (float) (instance.damage + instance.damage * amplifier));
              entity.setLastAttackedEntity(player);
              entity.setRevengeTarget(player);
            }
          }
        }
      }
    }
  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    this.playerId = net.minecraft.nbt.NBTUtil.getUUIDFromTag(compound.getCompoundTag("id"));
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    compound.setTag("id", net.minecraft.nbt.NBTUtil.createUUIDTag(playerId));
  }

}
