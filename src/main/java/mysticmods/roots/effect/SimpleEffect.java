package mysticmods.roots.effect;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleSupplier;

public class SimpleEffect extends MobEffect {
  private final boolean hiddenByDefault;

  public SimpleEffect(MobEffectCategory pCategory, int pColor) {
    this(pCategory, pColor, false);
  }

  public SimpleEffect(MobEffectCategory category, int color, boolean hiddenByDefault) {
    super(category, color);
    this.hiddenByDefault = hiddenByDefault;
  }

  public boolean onEffectExpired(LivingEntity entity, int amplifier) {
    return onEffectRemoved(entity, amplifier);
  }

  public boolean onEffectRemoved(LivingEntity entity, int amplifier) {
    return false;
  }

  public boolean isHiddenByDefault() {
    return hiddenByDefault;
  }
}
