package epicsquid.roots.modifiers.modifier;

import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.modifiers.IModifier;
import epicsquid.roots.modifiers.ModifierType;
import epicsquid.roots.util.types.RegistryItem;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class Modifier extends RegistryItem implements IModifier {
  private final IModifierCore core;
  private final ModifierType modifier;
  private final double value;

  public Modifier(ResourceLocation name, ModifierType modifierType, IModifierCore core, double value) {
    setRegistryName(name);
    this.modifier = modifierType;
    this.value = value;
    this.core = core;
    if (modifier == ModifierType.ADDITIONAL_COST && !this.core.isHerb()) {
      throw new IllegalStateException("Modifier cannot be additional cost without a herb specified.");
    }
  }

  @Override
  public String getTranslationKey() {
    ResourceLocation rl = getRegistryName();
    if (rl.getNamespace().equals(Roots.MODID)) {
      return "roots.modifiers.modifiers." + rl.getPath();
    } else {
      return "roots.modifiers.modifiers." + rl.getNamespace() + "." + rl.getPath();
    }
  }

  @Override
  public String getFormatting () {
    return core.getFormatting();
  }

  @Override
  public ItemStack getStack() {
    return core.getStack();
  }

  @Override
  public ModifierType getType() {
    return modifier;
  }

  @Override
  public IModifierCore getCore() {
    return core;
  }

  @Override
  public Object2DoubleOpenHashMap<Herb> apply(final Object2DoubleOpenHashMap<Herb> costs) {
    if (modifier == ModifierType.NO_COST) {
      return costs;
    }
    if (modifier == ModifierType.ADDITIONAL_COST) {
      final Object2DoubleOpenHashMap<Herb> result = new Object2DoubleOpenHashMap<>(costs);
      if (result.containsKey(core.getHerb())) {
        result.put(core.getHerb(), result.getDouble(core.getHerb()) + this.value);
      } else {
        result.put(core.getHerb(), this.value);
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
