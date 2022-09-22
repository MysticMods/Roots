package mysticmods.roots.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

// Does nothing for non-player mobs
public class SkySoarerEffect extends MobEffect {
  public SkySoarerEffect() {
    super(MobEffectCategory.BENEFICIAL, 0x03f0fc);
  }

  @Override
  public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
    super.applyEffectTick(pLivingEntity, pAmplifier);
    if (pLivingEntity instanceof Player player) {
    }
  }

  @Override
  public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
    super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    if (pLivingEntity instanceof Player player) {
      // Check modifiers and apply slow fall
    }
  }
}
