package epicsquid.roots.entity.ritual;

import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.ritual.RitualHealingAura;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class EntityRitualHealingAura extends EntityRitualBase {
  private RitualHealingAura ritual;

  public EntityRitualHealingAura(World worldIn) {
    super(worldIn);
    getDataManager().register(lifetime, RitualRegistry.ritual_healing_aura.getDuration() + 20);
    ritual = (RitualHealingAura) RitualRegistry.ritual_healing_aura;
  }

  @Override
  public void onUpdate() {
    super.onUpdate();
    float alpha = (float) Math.min(40, (RitualRegistry.ritual_healing_aura.getDuration() + 20) - getDataManager().get(lifetime)) / 40.0f;

    if (world.isRemote && getDataManager().get(lifetime) > 0) {
      ParticleUtil.spawnParticleStar(world, (float) posX, (float) posY, (float) posZ, 0, 0, 0, 100, 255, 100, 0.5f * alpha, 20.0f, 40);
      if (rand.nextInt(5) == 0) {
        ParticleUtil.spawnParticleSpark(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.0625f * (rand.nextFloat()),
            0.125f * (rand.nextFloat() - 0.5f), 100, 255, 100, 1.0f * alpha, 1.0f + rand.nextFloat(), 160);
      }
      for (float i = 0; i < 360; i += 72.0f) {
        double ang = ticksExisted % 360;
        float tx = (float) posX + 2.0f * (float) Math.sin(Math.toRadians(i + ang));
        float ty = (float) posY;
        float tz = (float) posZ + 2.0f * (float) Math.cos(Math.toRadians(i + ang));
        ParticleUtil.spawnParticleGlow(world, tx, ty, tz, 0, 0, 0, 100, 255, 100, 0.5f * alpha, 8.0f, 40);
      }
    }
    if (this.ticksExisted % ritual.player_interval == 0) {
      List<EntityPlayer> entities = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(posX - ritual.radius_x, posY - ritual.radius_y, posZ - ritual.radius_z, posX + ritual.radius_x, posY + ritual.radius_y, posZ + ritual.radius_z));
      for (EntityPlayer e : entities) {
        if (!world.isRemote) {
          e.heal(ritual.player_amount);
        }
        if (world.isRemote) {
          for (float i = 0; i < 8; i++) {
            ParticleUtil.spawnParticleStar(world, (float) e.posX + 0.5f * (rand.nextFloat() - 0.5f), (float) e.posY + e.height / 2.5f + (rand.nextFloat() - 0.5f), (float) e.posZ + 0.5f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), 0.01875f * (rand.nextFloat()), 0.125f * (rand.nextFloat() - 0.5f), 100, 255, 100, 1.0f * alpha, 1.0f + 2.0f * rand.nextFloat(), 40);
          }
        }
      }
    }
    if (this.ticksExisted % ritual.interval == 0) {
      List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX - ritual.radius_x, posY - ritual.radius_y, posZ - ritual.radius_z, posX + ritual.radius_x, posY + ritual.radius_y, posZ + ritual.radius_z));
      for (EntityLivingBase e : entities) {
        if (e instanceof EntityPlayer) {
          continue;
        }
        if (EntityUtil.isHostile(e, RitualRegistry.ritual_healing_aura)) {
          continue;
        }
        if (!world.isRemote) {
          e.heal(ritual.amount);
        }
        if (world.isRemote) {
          for (float i = 0; i < 8; i++) {
            ParticleUtil.spawnParticleStar(world, (float) e.posX + 0.5f * (rand.nextFloat() - 0.5f), (float) e.posY + e.height / 2.5f + (rand.nextFloat() - 0.5f), (float) e.posZ + 0.5f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), 0.01875f * (rand.nextFloat()), 0.125f * (rand.nextFloat() - 0.5f), 100, 255, 100, 1.0f * alpha, 1.0f + 2.0f * rand.nextFloat(), 40);
          }
        }
      }
    }
  }
}