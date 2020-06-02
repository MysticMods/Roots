package epicsquid.roots.modifiers;

import epicsquid.roots.api.Herb;
import epicsquid.roots.modifiers.modifier.IModifierCore;
import epicsquid.roots.spell.SpellBase;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.item.ItemStack;

public interface IModifier {
  String getTranslationKey();

  String getFormatting();

  ItemStack getStack();

  ModifierType getType();

  double getValue();

  IModifierCore getCore ();

  boolean isBasic ();

  Object2DoubleOpenHashMap<Herb> apply(final Object2DoubleOpenHashMap<Herb> costs);

  default Object2DoubleOpenHashMap<Herb> apply(SpellBase spell) {
    return apply(spell.getCosts());
  }
}
