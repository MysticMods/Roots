package mysticmods.roots.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FriendlyEarthEffect extends MobEffect {
  public FriendlyEarthEffect() {
    super(MobEffectCategory.BENEFICIAL, 0x946434);
  }

  @Override
  public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
    if (!pLivingEntity.onGround()) {
      pLivingEntity.fallDistance = 1.0f;
    }
    return true;
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
    return true;
  }
}
