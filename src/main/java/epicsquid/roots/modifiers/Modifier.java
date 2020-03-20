package epicsquid.roots.modifiers;

import epicsquid.roots.api.Herb;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.util.types.RegistryItem;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class Modifier extends RegistryItem {
  private final ModifierType modifier;
  private final double value;
  private final Herb herb;
  private final ItemStack item;

  public Modifier(ResourceLocation name, ModifierType modifierType, double value, ItemStack item, @Nullable Herb herb) {
    setRegistryName(name);
    this.modifier = modifierType;
    this.value = value;
    this.herb = herb;
    this.item = item;
    if (modifier == ModifierType.ADDITIONAL_COST && this.herb == null) {
      throw new IllegalStateException("Modifier cannot be additional cost without a herb specified.");
    }
    if (item.isEmpty()) {
      throw new IllegalStateException("Modifier ItemStack cannot be empty");
    }
  }

  public ItemStack getItem() {
    return item;
  }

  public Object2DoubleOpenHashMap<Herb> apply (SpellBase spell) {
    final Object2DoubleOpenHashMap<Herb> costs = spell.getCosts();
    if (modifier == ModifierType.NO_COST) {
      return costs;
    }
    if (modifier == ModifierType.ADDITIONAL_COST) {
      final Object2DoubleOpenHashMap<Herb> result = new Object2DoubleOpenHashMap<>(costs);
      if (result.containsKey(this.herb)) {
        result.put(this.herb, result.getDouble(this.herb) + this.value);
      } else {
        result.put(this.herb, this.value);
      }
      return result;
    }
    final Object2DoubleOpenHashMap<Herb> result = new Object2DoubleOpenHashMap<>();
    for (Object2DoubleMap.Entry<Herb> cost : costs.object2DoubleEntrySet()) {
      double val = cost.getDoubleValue();
      Herb herb = cost.getKey();
      result.put(herb, val + val * this.value);
    }
    return result;
  }
}
