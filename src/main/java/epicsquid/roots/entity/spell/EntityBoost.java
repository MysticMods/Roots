package epicsquid.roots.entity.spell;

import java.util.UUID;

import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.spell.SpellSkySoarer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityBoost extends Entity {
  private static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityBoost.class, DataSerializers.VARINT);
  private UUID playerId = null;

  public EntityBoost(World worldIn) {
    super(worldIn);
    this.setInvisible(true);
    this.setSize(1, 1);
    getDataManager().register(lifetime, 20);
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
      for (int i = 0; i < 4; i++) {
        if (rand.nextBoolean()) {
          ParticleUtil.spawnParticleStar(world, (float) posX + (rand.nextFloat()) - 0.5f, (float) posY + (rand.nextFloat()) + 0.5f,
              (float) posZ + (rand.nextFloat()) - 0.5f, -0.125f * (float) motionX, -0.125f * (float) motionY, -0.125f * (float) motionZ,
              SpellSkySoarer.instance.getRed1(), SpellSkySoarer.instance.getGreen1(), SpellSkySoarer.instance.getBlue1(), 0.5f,
              5.0f * rand.nextFloat() + 5.0f, 40);
        } else {
          ParticleUtil.spawnParticleStar(world, (float) posX + (rand.nextFloat()) - 0.5f, (float) posY + (rand.nextFloat()) + 0.5f,
              (float) posZ + (rand.nextFloat()) - 0.5f, -0.125f * (float) motionX, -0.125f * (float) motionY, -0.125f * (float) motionZ,
              SpellSkySoarer.instance.getRed2(), SpellSkySoarer.instance.getGreen2(), SpellSkySoarer.instance.getBlue2(), 0.5f,
              5.0f * rand.nextFloat() + 5.0f, 40);
        }
      }
    }
    if (playerId != null) {
      EntityPlayer player = world.getPlayerEntityByUUID(playerId);
      if (player != null) {
        Entity riding = player.getLowestRidingEntity();
        Entity target;
        if (riding != null) {
          target = riding;
        } else {
          target = player;
        }

        this.posX = player.posX;
        this.posY = player.posY + 1.0;
        this.posZ = player.posZ;
        target.motionX = player.getLookVec().x * 0.8;
        target.motionY = player.getLookVec().y * 0.8;
        target.motionZ = player.getLookVec().z * 0.8;
        this.motionX = player.getLookVec().x;
        this.motionY = player.getLookVec().y;
        this.motionZ = player.getLookVec().z;
        target.fallDistance = 0;
        target.velocityChanged = true;
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
