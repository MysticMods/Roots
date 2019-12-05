package epicsquid.roots.entity.spell;

import epicsquid.roots.init.ModPotions;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellTimeStop;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class TimeStopEntity extends Entity {
  private static final DataParameter<Integer> lifetime = EntityDataManager.createKey(TimeStopEntity.class, DataSerializers.VARINT);
  private UUID playerId = null;

  private int duration;

  public TimeStopEntity(EntityType<?> entityTypeIn, World worldIn) {
    super(entityTypeIn, worldIn);
  }

  public TimeStopEntity(World worldIn) {
    this(worldIn, SpellTimeStop.duration);
  }

  public TimeStopEntity(World world, int duration) {
    this(null, null);
    this.setInvisible(true);
    this.duration = duration;
  }

  public void setPlayer(UUID id) {
    this.playerId = id;
  }

  @Override
  public boolean isEntityInsideOpaqueBlock() {
    return false;
  }

  // TODO: Custom packet

  @Override
  public IPacket<?> createSpawnPacket() {
    return null;
  }

  @Override
  protected void registerData() {
    getDataManager().register(lifetime, duration);
  }

  @Override
  public void tick() {
    getDataManager().set(lifetime, getDataManager().get(lifetime) - 1);
    if (getDataManager().get(lifetime) <= 0) {
      remove();
    }
    if (world.isRemote) {
      for (float i = 0; i < 360; i += 24.0f) {
        if (rand.nextInt(4) == 0) {
          if (rand.nextBoolean()) {
            ParticleUtil
                .spawnParticleSmoke(world, (float) posX + rand.nextFloat() * rand.nextFloat() * 8.0f * (float) Math.sin(Math.toRadians(i)), (float) posY - 0.5f,
                    (float) posZ + rand.nextFloat() * rand.nextFloat() * 8.0f * (float) Math.cos(Math.toRadians(i)), 0, rand.nextFloat() * 0.05f, 0,
                    SpellTimeStop.instance.getRed1(), SpellTimeStop.instance.getGreen1(), SpellTimeStop.instance.getBlue1(), rand.nextFloat() * 0.25f,
                    rand.nextFloat() * 24f, 120, true);
          } else {
            ParticleUtil
                .spawnParticleSmoke(world, (float) posX + rand.nextFloat() * rand.nextFloat() * 8.0f * (float) Math.sin(Math.toRadians(i)), (float) posY - 0.5f,
                    (float) posZ + rand.nextFloat() * rand.nextFloat() * 8.0f * (float) Math.cos(Math.toRadians(i)), 0, rand.nextFloat() * 0.05f, 0,
                    SpellTimeStop.instance.getRed2(), SpellTimeStop.instance.getRed2(), SpellTimeStop.instance.getBlue2(), rand.nextFloat() * 0.25f,
                    rand.nextFloat() * 24f, 120, true);
          }
        }
      }
    }
    List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(posX - 9.0, posY - 1.0, posZ - 9.0, posX + 9.0, posY + 19.0, posZ + 9.0));
    for (LivingEntity e : entities) {
      if (playerId != null && e.getUniqueID() != playerId) {
        e.addPotionEffect(new EffectInstance(ModPotions.time_stop, 40, 0, false, false));
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
