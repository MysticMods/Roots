package epicsquid.roots.entity.ritual;

import epicsquid.roots.entity.projectile.EntityFlare;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.ritual.RitualFireStorm;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class EntityRitualFireStorm extends EntityRitualBase {
  private RitualFireStorm ritual;

  public EntityRitualFireStorm(World worldIn) {
    super(worldIn);
    this.getDataManager().register(lifetime, RitualRegistry.ritual_fire_storm.getDuration() + 20);
    this.ritual = (RitualFireStorm) RitualRegistry.ritual_fire_storm;
  }

  @Override
  public void onUpdate() {
    super.onUpdate();

    float alpha = (float) Math.min(40, (RitualRegistry.ritual_fire_storm.getDuration() + 20) - getDataManager().get(lifetime)) / 40.0f;
    if (world.isRemote && getDataManager().get(lifetime) > 0) {
      ParticleUtil.spawnParticleStar(world, (float) posX, (float) posY, (float) posZ, 0, 0, 0, 255, 96, 32, 0.5f * alpha, 20.0f, 40);
      if (rand.nextInt(5) == 0) {
        ParticleUtil.spawnParticleSpark(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.0625f * (rand.nextFloat()), 0.125f * (rand.nextFloat() - 0.5f), 255, 96, 32, 1.0f * alpha, 1.0f + rand.nextFloat(), 160);
      }
      if (rand.nextInt(2) == 0) {
        for (float i = 0; i < 360; i += rand.nextFloat() * 45.0f) {
          float tx = (float) posX + 2f * (float) Math.sin(Math.toRadians(i));
          float ty = (float) posY;
          float tz = (float) posZ + 2f * (float) Math.cos(Math.toRadians(i));
          ParticleUtil.spawnParticleFiery(world, tx, ty, tz, 0, 0, 0, 255, 96, 32, 0.5f * alpha, 6.0f + 6.0f * rand.nextFloat(), 40);
        }
      }
    }
    if (this.ticksExisted % 2 == 0) {
      List<EntityFlare> projectiles = world.getEntitiesWithinAABB(EntityFlare.class, new AxisAlignedBB(posX - 10.5f, posY - 10.5, posZ - 10.5, posX + 10.5, posY + 10.5, posZ + 10.5));
      if (projectiles.size() < ritual.projectile_count && !world.isRemote) {
        EntityFlare flare = new EntityFlare(world);
        flare.initCustom(posX + 16.0f * (rand.nextFloat() - 0.5f), posY + 43.0f, posZ + 16.0f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), -0.5f - rand.nextFloat() * 0.5f, 0.125f * (rand.nextFloat() - 0.5f), 4.0f + 8.0f * rand.nextFloat(), posY);
        world.spawnEntity(flare);
      }
    }
  }
}