package epicsquid.roots.entity.ritual;

import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.ritual.RitualPurity;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PurityRitualEntity extends BaseRitualEntity {
  private RitualPurity ritual;

  public PurityRitualEntity(EntityType<?> entityTypeIn, World worldIn) {
    super(entityTypeIn, worldIn);
    ritual = (RitualPurity) RitualRegistry.ritual_purity;
  }

/*  public PurityRitualEntity(World worldIn) {
    super(worldIn);
  }*/

  @Override
  protected void registerData() {
    getDataManager().register(lifetime, RitualRegistry.ritual_purity.getDuration() + 20);
  }

  public static Field zombieConversion = ObfuscationReflectionHelper.findField(ZombieVillagerEntity.class, "field_82234_d");

  @Override
  public void tick() {
    super.tick();
    float alpha = (float) Math.min(40, (RitualRegistry.ritual_healing_aura.getDuration() + 20) - getDataManager().get(lifetime)) / 40.0f;

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
    if (this.ticksExisted % ritual.interval == 0) {
      List<LivingEntity> entities = world
          .getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(posX - ritual.radius_x, posY - ritual.radius_y, posZ - ritual.radius_z, posX + ritual.radius_x, posY + ritual.radius_y, posZ + ritual.radius_z));
      for (LivingEntity e : entities) {
        if (EntityUtil.isHostile(e)) {
          if (e instanceof ZombieVillagerEntity && ((ZombieVillagerEntity)e).isConverting()) {
            try {
              zombieConversion.set(e, (int) zombieConversion.get(e) - ritual.zombie_count);
            } catch (IllegalAccessException e1) {
            }
            e.extinguish();
          }
          continue;
        }

        List<Effect> toRemove = new ArrayList<>();
        for (EffectInstance potionEffect : e.getActivePotionEffects()) {
          if (!potionEffect.getPotion().isBeneficial()) {
            toRemove.add(potionEffect.getPotion());
          }
        }
        for (Effect potion : toRemove) {
          e.removePotionEffect(potion);
        }
        e.extinguish();
        if (world.isRemote) {
          for (float i = 0; i < 8; i++) {
            ParticleUtil.spawnParticleStar(world, (float) e.posX + 0.5f * (rand.nextFloat() - 0.5f), (float) e.posY + e.getHeight() / 2.5f + (rand.nextFloat() - 0.5f), (float) e.posZ + 0.5f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), 0.01875f * (rand.nextFloat()), 0.125f * (rand.nextFloat() - 0.5f), 100, 255, 100, 1.0f * alpha, 1.0f + 2.0f * rand.nextFloat(), 40);
          }
        }
      }
    }
  }
}
