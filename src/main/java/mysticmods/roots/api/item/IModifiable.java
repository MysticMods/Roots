package mysticmods.roots.api.item;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Map;
import java.util.function.Supplier;

public interface IModifiable {
  Map<Attribute, AttributeModifier> getModifiers();

  default AttributeModifier getOrCreateModifier(Attribute attribute, Supplier<AttributeModifier> supplier) {
    AttributeModifier mod = getModifiers().get(attribute);
    if (mod == null) {
      mod = supplier.get();
      getModifiers().put(attribute, mod);
    }
    return mod;
  }
}
