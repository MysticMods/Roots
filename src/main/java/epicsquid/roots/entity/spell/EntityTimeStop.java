package epicsquid.roots.entity.spell;

import java.util.List;
import java.util.UUID;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellTimeStop;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityTimeStop extends Entity {
  private static final DataParameter<Integer> lifetime = EntityDataManager.<Integer>createKey(EntityTimeStop.class, DataSerializers.VARINT);
  private UUID playerId = null;

  public EntityTimeStop(World worldIn) {
    super(worldIn);
    this.setInvisible(true);
    this.setSize(1, 1);
    getDataManager().register(lifetime, 200);
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
    List<EntityLivingBase> entities = world
        .getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX - 9.0, posY - 1.0, posZ - 9.0, posX + 9.0, posY + 19.0, posZ + 9.0));
    for (EntityLivingBase e : entities) {
      if (playerId != null && e.getUniqueID() != playerId) {
        e.addPotionEffect(new PotionEffect(ModPotions.time_stop, 40));
      }
    }
  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    this.playerId = NBTUtil.getUUIDFromTag(compound.getCompoundTag("id"));
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    compound.setTag("id", NBTUtil.createUUIDTag(playerId));
  }

}
