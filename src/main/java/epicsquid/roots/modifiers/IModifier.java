package epicsquid.roots.modifiers;

import epicsquid.roots.api.Herb;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.util.types.IRegistryItem;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.function.Supplier;

public interface IModifier extends IRegistryItem {
  String getTranslationKey();

  String getFormatting();

  ItemStack getStack();

  IModifierCore getCore ();

  boolean isBasic ();

  List<IModifierCost> getCosts();

  List<Supplier<IModifier>> getConflicts();

  default boolean conflicts (IModifier modifier) {
    for (Supplier<IModifier> mod : getConflicts()) {
      if (mod.get().equals(modifier)) {
        return true;
      }
    }

    return false;
  }

  Object2DoubleOpenHashMap<Herb> apply(final Object2DoubleOpenHashMap<Herb> costs, CostType phase);

  default Object2DoubleOpenHashMap<Herb> apply(SpellBase spell, CostType phase) {
    return apply(spell.getCosts(), phase);
  }
}
