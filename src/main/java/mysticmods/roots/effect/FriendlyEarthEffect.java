package mysticmods.roots.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FriendlyEarthEffect extends MobEffect {
  public FriendlyEarthEffect() {
    super(MobEffectCategory.BENEFICIAL, 0x946434);
  }

  @Override
  public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
    pLivingEntity.fallDistance = 0;
  }

  @Override
  public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
    return true;
  }
}
