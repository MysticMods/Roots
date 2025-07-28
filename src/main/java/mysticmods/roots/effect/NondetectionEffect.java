package mysticmods.roots.effect;

import mysticmods.roots.init.ModParticles;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class NondetectionEffect extends SimpleEffect {
  public NondetectionEffect(MobEffectCategory pCategory, int pColor) {
    super(pCategory, pColor);
  }

  @Override
  public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
    if (livingEntity.level().isClientSide()) {
      livingEntity.level()
          .addParticle(RootsParticleOptions.builder(ModParticles.NONDETECTION).entityId(livingEntity.getId())
              .build(), livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 0, 0, 0);
    }

    return super.applyEffectTick(livingEntity, amplifier);
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
    return true;
  }
}
