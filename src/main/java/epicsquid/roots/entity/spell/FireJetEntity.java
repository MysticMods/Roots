package epicsquid.roots.entity.spell;

import epicsquid.roots.particle.ParticleUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class FireJetEntity extends Entity {
  private static final DataParameter<Integer> lifetime = EntityDataManager.createKey(FireJetEntity.class, DataSerializers.VARINT);
  private UUID playerId = null;

  public FireJetEntity(EntityType<?> entityTypeIn, World worldIn) {
    super(entityTypeIn, worldIn);
  }

/*  public FireJetEntity(World worldIn) {
    super(worldIn);
    this.setInvisible(true);
    this.setSize(1, 1);
  }*/

  public void setPlayer(UUID id) {
    this.playerId = id;
  }

  @Override
  public boolean isEntityInsideOpaqueBlock() {
    return false;
  }

  // TODO: Custom spawn packet

  @Override
  public IPacket<?> createSpawnPacket() {
    return null;
  }

  @Override
  protected void registerData() {
    getDataManager().register(lifetime, 12);
  }

  @Override
  public void tick() {
    getDataManager().set(lifetime, getDataManager().get(lifetime) - 1);
    if (getDataManager().get(lifetime) <= 0) {
      remove();
    }
    if (world.isRemote) {
      Vec3d motion = getMotion();
      for (int i = 0; i < 8; i++) {
        float offX = 0.5f * (float) Math.sin(Math.toRadians(rotationYaw));
        float offZ = 0.5f * (float) Math.cos(Math.toRadians(rotationYaw));
        ParticleUtil.spawnParticleFiery(world, (float) posX + (float) motion.x * 2.5f + offX, (float) posY + 1.62F + (float) motion.y * 2.5f,
            (float) posZ + (float) motion.z * 2.5f + offZ, (float) motion.x + 0.125f * (rand.nextFloat() - 0.5f),
            (float) motion.y + 0.125f * (rand.nextFloat() - 0.5f), (float) motion.z + 0.125f * (rand.nextFloat() - 0.5f), 255.0f, 96.0f, 32.0f, 0.5f, 7.5f, 24);

      }
    }
    if (this.playerId != null) {
      PlayerEntity player = world.getPlayerByUuid(this.playerId);
      if (player != null) {
        this.posX = player.posX;
        this.posY = player.posY;
        this.posZ = player.posZ;
        setMotion(player.getLookVec().x * 0.5f, player.getLookVec().y * 0.5f, player.getLookVec().z * 0.5f);
        rotationYaw = -90.0f - player.rotationYaw;
        for (float i = 0; i < 3; i++) {
          float vx = (float) player.getLookVec().x * 3.0f;
          float vy = (float) player.getLookVec().y * 3.0f;
          float vz = (float) player.getLookVec().z * 3.0f;
          List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class,
              new AxisAlignedBB(posX + vx * i - 1.5, posY + vy * i - 1.5, posZ + vz * i - 1.5, posX + vx * i + 1.5, posY + vy * i + 1.5, posZ + vz * i + 1.5));
          for (LivingEntity entity : entities) {
            if (!(entity instanceof PlayerEntity && !world.getServer().isPVPEnabled())
                && entity.getUniqueID().compareTo(player.getUniqueID()) != 0) {
              entity.setFire(4);
              entity.attackEntityFrom((DamageSource.IN_FIRE).causeMobDamage(player), 4.5f);
              entity.setLastAttackedEntity(player);
              entity.setRevengeTarget(player);
            }
          }
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
