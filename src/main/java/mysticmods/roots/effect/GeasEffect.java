package mysticmods.roots.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class GeasEffect extends SimpleEffect {
  public GeasEffect(MobEffectCategory pCategory, int pColor) {
    super(pCategory, pColor);
  }

  public GeasEffect(MobEffectCategory category, int color, boolean hiddenByDefault) {
    super(category, color, hiddenByDefault);
  }

  @Override
  public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
    return true;
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
    return duration % 20 == 0;
  }
}
