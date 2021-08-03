package epicsquid.roots.entity.spell;

import epicsquid.roots.init.ModPotions;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellTimeStop;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class EntityTimeStop extends EntitySpellModifiable<SpellTimeStop> {
  public static boolean refreshing = false;

  public EntityTimeStop(World worldIn) {
    this(worldIn, SpellTimeStop.instance.duration);
  }

  public EntityTimeStop(World world, int duration) {
    super(world, SpellTimeStop.instance, duration);
  }

  @Override
  public void onDeath() {
    if (SpellTimeStop.instance.shouldPlaySound()) {
      this.playSound(ModSounds.Spells.TIME_STOP_END, SpellTimeStop.instance.getSoundVolume(), 1);
    }
  }

  private AxisAlignedBB box = null;

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
    if (!world.isRemote) {
      if (box == null) {
        box = new AxisAlignedBB(posX - SpellTimeStop.instance.radius_x - 1, posY - 1.0, posZ - SpellTimeStop.instance.radius_z - 1, posX + SpellTimeStop.instance.radius_x, posY + SpellTimeStop.instance.radius_y, posZ + SpellTimeStop.instance.radius_z);
      }
      List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, box);
      for (EntityLivingBase e : entities) {
        if (!(e instanceof EntityPlayer)) {
          if (modifiers != null && modifiers.has(SpellTimeStop.PEACEFUL) && EntityUtil.isFriendly(e, SpellTimeStop.instance)) {
            continue;
          }
          if (modifiers != null && modifiers.has(SpellTimeStop.FAMILIARS) && EntityUtil.isFamiliar(e)) {
            continue;
          }
          refreshing = true;
          e.addPotionEffect(new PotionEffect(ModPotions.time_stop, 40, 0, false, false));
          e.getEntityData().setIntArray(SpellTimeStop.instance.getCachedName(), modifiers.toArray());
          refreshing = false;
          if (modifiers.has(SpellTimeStop.SLOW)) {
            e.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, instance.slow_duration + 40, instance.slow_amplifier));
          }
          if (modifiers.has(SpellTimeStop.WEAKNESS)) {
            e.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, instance.weakness_duration + 40, instance.weakness_amplifier));
          }
          if (modifiers.has(SpellTimeStop.FIRE)) {
            e.setFire(SpellTimeStop.instance.fire_duration);
          }
        }
      }
    }


  }
}
