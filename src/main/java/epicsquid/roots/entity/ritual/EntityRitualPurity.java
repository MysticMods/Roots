package epicsquid.roots.entity.ritual;

import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntityRitualPurity extends EntityRitualBase {
  public EntityRitualPurity(World worldIn) {
    super(worldIn);
    getDataManager().register(lifetime, RitualRegistry.ritual_purity.getDuration() + 20);
  }

  @Override
  public void onUpdate() {
    super.onUpdate();
    float alpha = (float) Math.min(40, (RitualRegistry.ritual_life.getDuration() + 20) - getDataManager().get(lifetime)) / 40.0f;

    if (world.isRemote && getDataManager().get(lifetime) > 0) {
      ParticleUtil.spawnParticleStar(world, (float) posX, (float) posY, (float) posZ, 0, 0, 0, 100, 255, 100, 0.5f * alpha, 20.0f, 40);
      if (rand.nextInt(5) == 0) {
        ParticleUtil.spawnParticleSpark(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.0625f * (rand.nextFloat()),
            0.125f * (rand.nextFloat() - 0.5f), 255, 255, 255, 1.0f * alpha, 1.0f + rand.nextFloat(), 160);
      }
      for (float i = 0; i < 360; i += 72.0f) {
        double ang = ticksExisted % 360;
        float tx = (float) posX + 1.0f * (float) Math.sin(Math.toRadians(i + ang));
        float ty = (float) posY;
        float tz = (float) posZ + 1.0f * (float) Math.cos(Math.toRadians(i + ang));
        ParticleUtil.spawnParticleGlow(world, tx, ty, tz, 0, 0, 0, 255, 255, 255, 0.5f * alpha, 8.0f, 40);
      }
    }
    if (this.ticksExisted % 20 == 0) {
      List<EntityLivingBase> entities = world
          .getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX - 15.5, posY - 15.5, posZ - 15.5, posX + 15.5, posY + 15.5, posZ + 15.5));
      for (EntityLivingBase e : entities) {
        List<Potion> toRemove = new ArrayList<>();
        for (PotionEffect potionEffect : e.getActivePotionEffects()) {
          if (potionEffect.getPotion().isBadEffect()) {
            toRemove.add(potionEffect.getPotion());
          }
        }
        for (Potion potion : toRemove) {
          e.removePotionEffect(potion);
        }
        e.extinguish();
        if (e instanceof EntityZombieVillager && ((EntityZombieVillager)e).isConverting()) {
          ((EntityZombieVillager)e).conversionTime -= 1;
        }
        if (world.isRemote) {
          for (float i = 0; i < 8; i++) {
            ParticleUtil
                .spawnParticleStar(world, (float) e.posX + 0.5f * (rand.nextFloat() - 0.5f), (float) e.posY + e.height / 2.5f + (rand.nextFloat() - 0.5f),
                    (float) e.posZ + 0.5f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), 0.01875f * (rand.nextFloat()),
                    0.125f * (rand.nextFloat() - 0.5f), 100, 255, 100, 1.0f * alpha, 1.0f + 2.0f * rand.nextFloat(), 40);
          }
        }
      }
    }
  }
}
