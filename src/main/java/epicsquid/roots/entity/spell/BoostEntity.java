package epicsquid.roots.entity.spell;

import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellSkySoarer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class BoostEntity extends Entity {
  private static final DataParameter<Integer> lifetime = EntityDataManager.createKey(BoostEntity.class, DataSerializers.VARINT);
  private UUID playerId = null;
  private double origX;
  private double origY;
  private double origZ;

  public BoostEntity(EntityType<?> entityTypeIn, World worldIn) {
    super(entityTypeIn, worldIn);
  }

/*  public BoostEntity(World worldIn) {
    super(worldIn);
    this.setInvisible(true);
    this.setSize(1, 1);
  }*/

  @Override
  protected void registerData() {
    getDataManager().register(lifetime, 20);
  }

  public void setPlayer(UUID id) {
    this.playerId = id;
    Entity[] result = getTargets();
    if (result != null) {
      Vec3d motion = result[1].getMotion();
      origX = motion.x;
      origY = motion.y;
      origZ = motion.z;
    }
  }

  // TODO: Rewrite this

  @Nullable
  private Entity[] getTargets() {
    if (this.playerId != null) {
      PlayerEntity player = world.getPlayerByUuid(playerId);
      if (player != null) {
        Entity riding = player.getLowestRidingEntity();
        if (riding != player) {
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
  public IPacket<?> createSpawnPacket() {
    return null;
  }

  @Override
  public void tick() {
    getDataManager().set(lifetime, getDataManager().get(lifetime) - 1);
    if (getDataManager().get(lifetime) <= 0) {
      remove();
      if (!world.isRemote) {
        Entity[] target = getTargets();
        if (target != null) {
          target[1].setMotion(origX, origY, origZ);
        }
      }
    }
    if (world.isRemote) {
      Vec3d motion = getMotion();
      for (int i = 0; i < 4; i++) {
        if (rand.nextBoolean()) {
          ParticleUtil.spawnParticleStar(world, (float) posX + (rand.nextFloat()) - 0.5f, (float) posY + (rand.nextFloat()) + 0.5f,
              (float) posZ + (rand.nextFloat()) - 0.5f, -0.125f * (float) motion.x, -0.125f * (float) motion.y, -0.125f * (float) motion.z,
              SpellSkySoarer.instance.getRed1(), SpellSkySoarer.instance.getGreen1(), SpellSkySoarer.instance.getBlue1(), 0.5f,
              5.0f * rand.nextFloat() + 5.0f, 40);
        } else {
          ParticleUtil.spawnParticleStar(world, (float) posX + (rand.nextFloat()) - 0.5f, (float) posY + (rand.nextFloat()) + 0.5f,
              (float) posZ + (rand.nextFloat()) - 0.5f, -0.125f * (float) motion.x, -0.125f * (float) motion.y, -0.125f * (float) motion.z,
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
          target.setMotion( player.getLookVec().x * 0.8, player.getLookVec().y * 0.8, player.getLookVec().z * 0.8);
          setMotion(player.getLookVec().x, player.getLookVec().y, player.getLookVec().z);
          target.fallDistance = 0;
          target.velocityChanged = true;
        }
      }
    }
  }

  @Override
  protected void readAdditional(CompoundNBT compound) {
    this.playerId = NBTUtil.readUniqueId(compound.getCompound("id"));
  }

  @Override
  protected void writeAdditional(CompoundNBT compound) {
    compound.put("id", NBTUtil.writeUniqueId(playerId));
  }
}
