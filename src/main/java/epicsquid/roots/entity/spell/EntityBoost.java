package epicsquid.roots.entity.spell;

import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellSkySoarer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityBoost extends Entity {
  private static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityBoost.class, DataSerializers.VARINT);
  private UUID playerId = null;
  private double origX;
  private double origY;
  private double origZ;

  public EntityBoost(World worldIn) {
    super(worldIn);
    this.setInvisible(true);
    this.setSize(1, 1);
    getDataManager().register(lifetime, 20);
  }

  public void setPlayer(UUID id) {
    this.playerId = id;
    Entity[] result = getTargets();
    if (result != null) {
      origX = result[1].motionX;
      origY = result[1].motionY;
      origZ = result[1].motionZ;
    }
  }

  @Nullable
  private Entity[] getTargets() {
    if (this.playerId != null) {
      PlayerEntity player = world.getPlayerEntityByUUID(playerId);
      if (player != null) {
        Entity riding = player.getLowestRidingEntity();
        if (riding != null) {
          return new Entity[]{player, riding};
        } else {
          return new Entity[]{player, player};
        }
      }
    }

    return null;
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
      if (!world.isRemote) {
        Entity[] target = getTargets();
        if (target != null) {
          target[1].motionX = origX;
          target[1].motionY = origY;
          target[1].motionZ = origZ;
        }
      }
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
    } else {
      if (playerId != null) {
        Entity[] result = getTargets();
        if (result != null) {
          PlayerEntity player = (PlayerEntity) result[0];
          Entity target = result[1];
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
  }

  @Override
  protected void readEntityFromNBT(CompoundNBT compound) {
    this.playerId = net.minecraft.nbt.NBTUtil.getUUIDFromTag(compound.getCompoundTag("id"));
  }

  @Override
  protected void writeEntityToNBT(CompoundNBT compound) {
    compound.setTag("id", net.minecraft.nbt.NBTUtil.createUUIDTag(playerId));
  }

}
