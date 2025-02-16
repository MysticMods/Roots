package mysticmods.roots.effect;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.function.BiConsumer;

public class AquaBubbleEffect extends SimpleEffect {
  public AquaBubbleEffect(MobEffectCategory pCategory, int pColor) {
    super(pCategory, pColor);
  }
}
