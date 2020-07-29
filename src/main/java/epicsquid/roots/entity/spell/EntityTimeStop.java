package epicsquid.roots.entity.spell;

import epicsquid.roots.init.ModPotions;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellTimeStop;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class EntityTimeStop extends EntitySpellModifiable<SpellTimeStop> {

  public EntityTimeStop(World worldIn) {
    this(worldIn, SpellTimeStop.instance.duration);
  }

  public EntityTimeStop(World world, int duration) {
    super(world, SpellTimeStop.instance, duration);
  }

  @Override
  public void onUpdate() {
    super.onUpdate();
    if (world.isRemote) {
      for (float i = 0; i < 360; i += 24.0f) {
        if (rand.nextInt(4) == 0) {
          if (rand.nextBoolean()) {
            ParticleUtil.spawnParticleSmoke(world, (float) posX + rand.nextFloat() * rand.nextFloat() * 8.0f * (float) Math.sin(Math.toRadians(i)), (float) posY - 0.5f, (float) posZ + rand.nextFloat() * rand.nextFloat() * 8.0f * (float) Math.cos(Math.toRadians(i)), 0, rand.nextFloat() * 0.05f, 0, SpellTimeStop.instance.getRed1(), SpellTimeStop.instance.getGreen1(), SpellTimeStop.instance.getBlue1(), rand.nextFloat() * 0.25f, rand.nextFloat() * 24f, 120, true);
          } else {
            ParticleUtil.spawnParticleSmoke(world, (float) posX + rand.nextFloat() * rand.nextFloat() * 8.0f * (float) Math.sin(Math.toRadians(i)), (float) posY - 0.5f, (float) posZ + rand.nextFloat() * rand.nextFloat() * 8.0f * (float) Math.cos(Math.toRadians(i)), 0, rand.nextFloat() * 0.05f, 0, SpellTimeStop.instance.getRed2(), SpellTimeStop.instance.getRed2(), SpellTimeStop.instance.getBlue2(), rand.nextFloat() * 0.25f, rand.nextFloat() * 24f, 120, true);
          }
        }
      }
    }
    List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX - 9.0, posY - 1.0, posZ - 9.0, posX + 9.0, posY + 19.0, posZ + 9.0));
    for (EntityLivingBase e : entities) {
      if (!(e instanceof EntityPlayer)) {
        if (modifiers != null && instance.has(SpellTimeStop.PEACEFUL, modifiers) && EntityUtil.isFriendly(e)) {
          continue;
        }
        e.addPotionEffect(new PotionEffect(ModPotions.time_stop, 40, 0, false, false));
      }
    }
  }
}
