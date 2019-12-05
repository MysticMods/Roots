package epicsquid.roots.entity.ritual;

import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.ritual.RitualDivineProtection;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class DivineProtectionRitualEntity extends BaseRitualEntity {
  private RitualDivineProtection ritual;

  public DivineProtectionRitualEntity(EntityType<?> entityTypeIn, World worldIn) {
    super(entityTypeIn, worldIn);
    this.ritual = (RitualDivineProtection) RitualRegistry.ritual_divine_protection;
  }

/*  public DivineProtectionRitualEntity(World worldIn) {
    super(worldIn);
  }*/

  @Override
  protected void registerData() {
    getDataManager().register(lifetime, RitualRegistry.ritual_divine_protection.getDuration() + 20);
  }

  @Override
  public void tick() {
    super.tick();

    float alpha = (float) Math.min(40, (RitualRegistry.ritual_divine_protection.getDuration() + 20) - getDataManager().get(lifetime)) / 40.0f;
    if (world.isRemote && getDataManager().get(lifetime) > 0) {
      ParticleUtil.spawnParticleStar(world, (float) posX, (float) posY, (float) posZ, 0, 0, 0, 255, 255, 75, 0.5f * alpha, 20.0f, 40);
      if (rand.nextInt(5) == 0) {
        ParticleUtil.spawnParticleSpark(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.0625f * (rand.nextFloat()),
            0.125f * (rand.nextFloat() - 0.5f), 255, 255, 75, 1.0f * alpha, 1.0f + rand.nextFloat(), 160);
      }
      if (rand.nextInt(2) == 0) {
        float pitch = rand.nextFloat() * 360.0f;
        float yaw = rand.nextFloat() * 360.0f;
        for (float i = 0; i < 3.5; i += 0.2f) {
          float tx = (float) posX + i * (float) Math.sin(Math.toRadians(yaw)) * (float) Math.sin(Math.toRadians(pitch));
          float ty = (float) posY + i * (float) Math.cos(Math.toRadians(pitch));
          float tz = (float) posZ + i * (float) Math.cos(Math.toRadians(yaw)) * (float) Math.sin(Math.toRadians(pitch));
          float coeff = i / 3.5f;
          ParticleUtil.spawnParticleGlow(world, tx, ty, tz, 0, 0, 0, 255, 255, 75, 0.5f * alpha * (1.0f - coeff), 18.0f * (1.0f - coeff), 20);
        }
      }
    }
    if (this.ticksExisted % 20 == 0) {
      if (ritual.rain) {
        if (world.getWorldInfo().isRaining()) {
          world.getWorldInfo().setRaining(false);
        }
      }
      // TODO: Handle time
/*      if (ritual.time) {
        if (world.getWorldTime() % ritual.day_length >= 0 && world.getWorldTime() % ritual.day_length < ritual.night_threshold) {
          if (rand.nextInt(ritual.day_extension) == 0) {
            world.setWorldTime(world.getWorldTime() - 1);
          }
        } else {
          world.setWorldTime(world.getWorldTime() + ritual.night_reduction);
        }
      }*/
      List<LivingEntity> entities = world
          .getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(posX - ritual.radius_x, posY - ritual.radius_y, posZ - ritual.radius_z, posX + ritual.radius_x, posY + ritual.radius_y, posZ + ritual.radius_z));
      for (LivingEntity e : entities) {
        if (e.isEntityUndead()) {
/*          if (Loader.isModLoaded("consecration")) {
            e.attackEntityFrom(ModDamage.radiantDamageFrom(null), ritual.consecration_damage);
          } else {*/
          e.attackEntityFrom(DamageSource.IN_FIRE, ritual.fire_damage);
          //}
          e.setFire(ritual.fire_duration);
          if (world.isRemote) {
            for (float i = 0; i < 16; i++) {
              ParticleUtil
                  .spawnParticleFiery(world, (float) e.posX + 0.5f * (rand.nextFloat() - 0.5f), (float) e.posY + e.getHeight() / 2.5f + (rand.nextFloat() - 0.5f),
                      (float) e.posZ + 0.5f * (rand.nextFloat() - 0.5f), 0.0625f * (rand.nextFloat() - 0.5f), 0.09375f * (rand.nextFloat()),
                      0.0625f * (rand.nextFloat() - 0.5f), 255.0f, 96.0f, 32.0f, 1.0f * alpha, 4.0f + 12.0f * rand.nextFloat(), 40);
            }
          }
        }
      }
    }
  }
}