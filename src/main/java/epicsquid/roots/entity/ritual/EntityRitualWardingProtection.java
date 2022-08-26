package epicsquid.roots.entity.ritual;

import epicsquid.roots.init.ModPotions;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.ritual.RitualWardingProtection;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class EntityRitualWardingProtection extends EntityRitualBase {
  private RitualWardingProtection ritual;

  public EntityRitualWardingProtection(World worldIn) {
    super(worldIn);
    this.setInvisible(true);
    this.setSize(1, 1);
    getDataManager().register(lifetime, RitualRegistry.ritual_warding_protection.getDuration() + 20);
    ritual = (RitualWardingProtection) RitualRegistry.ritual_warding_protection;
  }

  @Override
  public void onUpdate() {
    super.onUpdate();
    float alpha = (float) Math.min(40, (RitualRegistry.ritual_warding_protection.getDuration() + 20) - getDataManager().get(lifetime)) / 40.0f;

    if (world.isRemote && getDataManager().get(lifetime) > 0) {
      ParticleUtil.spawnParticleStar(world, (float) posX, (float) posY, (float) posZ, 0, 0, 0, 100, 255, 235, 0.5f * alpha, 20.0f, 40);
      if (rand.nextInt(5) == 0) {
        ParticleUtil.spawnParticleSpark(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.0625f * (rand.nextFloat()),
            0.125f * (rand.nextFloat() - 0.5f), 100, 255, 235, 1.0f * alpha, 1.0f + rand.nextFloat(), 160);
      }
      for (float i = 0; i < 360; i += 90.0f) {
        double ang = ticksExisted % 360;
        float tx = (float) posX + 2.5f * (float) Math.sin(Math.toRadians(ang)) * (float) Math.sin(Math.toRadians(i + ang));
        float ty = (float) posY;
        float tz = (float) posZ + 2.5f * (float) Math.sin(Math.toRadians(ang)) * (float) Math.cos(Math.toRadians(i + ang));
        ParticleUtil.spawnParticleGlow(world, tx, ty, tz, 0, 0, 0, 100, 255, 235, 0.5f * alpha, 8.0f, 40);
      }
    }
    if (this.ticksExisted % ritual.interval == 0) {
      List<EntityLivingBase> entities = world
          .getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX - ritual.radius_x, posY - ritual.radius_y, posZ - ritual.radius_z, posX + ritual.radius_x, posY + ritual.radius_y, posZ + ritual.radius_z));
      for (EntityLivingBase e : entities) {
        e.addPotionEffect(new PotionEffect(ModPotions.invulnerability, ritual.invuln_duration, 0, false, false));
      }
    }
  }
}