package epicsquid.roots.modifiers;

import epicsquid.roots.api.Herb;
import epicsquid.roots.properties.Property;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.util.types.IRegistryItem;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public interface IModifier extends IRegistryItem {
  boolean isDisabled();

  String getTranslationKey();

  String getFormatting();

  ItemStack getStack();

  IModifierCore getCore();

  boolean isBasic();

  Map<CostType, IModifierCost> getCosts();

  Set<IModifier> getConflicts();

  String getIdentifier();

  List<Property<SpellBase.ModifierCost>> asProperties ();

  default Supplier<IModifier> supply() {
    return () -> this;
  }

  default void addConflict(IModifier supplier) {
    addConflict(supplier, true);
  }

  default void addConflict(IModifier supplier, boolean reverse) {
    throw new IllegalStateException(this.getClass().toString() + " does not support addConflict.");
  }

  default boolean conflicts(IModifier modifier) {
    for (IModifier mod : getConflicts()) {
      if (mod.getModifier().equals(modifier.getModifier())) {
        return true;
      }
    }

    return false;
  }

  default <T extends IModifier, V extends NBTBase> boolean isConflicting(IModifierList<T, V> modifiers) {
    for (T modifier : modifiers.getModifiers()) {
      if (conflicts(modifier)) {
        return true;
      }
    }

    return false;
  }

  default IModifier getModifier() {
    return this;
  }

  Object2DoubleOpenHashMap<Herb> apply(final Object2DoubleOpenHashMap<Herb> costs, CostType phase);

  default Object2DoubleOpenHashMap<Herb> apply(final SpellBase spell, CostType phase) {
    return apply(spell.getCosts(), phase);
  }

  default Object2DoubleOpenHashMap<Herb> apply(Object2DoubleOpenHashMap<Herb> costs) {
    for (CostType type : CostType.values()) {
      costs = apply(costs, type);
    }

    return costs;
  }

  default Object2DoubleOpenHashMap<Herb> apply(final SpellBase spell) {
    return apply(spell.getCosts());
  }
}
