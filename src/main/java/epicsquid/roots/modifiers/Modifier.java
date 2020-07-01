package epicsquid.roots.modifiers;

import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.util.types.RegistryItem;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Modifier extends RegistryItem implements IModifier {
  private final IModifierCore core;
  private final List<IModifierCost> costs;
  private final List<Supplier<IModifier>> conflicts;

  public Modifier(ResourceLocation name, IModifierCore core, List<IModifierCost> costs) {
    setRegistryName(name);
    this.costs = costs;
    this.core = core;
    this.conflicts = new ArrayList<>();
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
  public String getFormatting() {
    return core.getFormatting();
  }

  @Override
  public ItemStack getStack() {
    return core.getStack();
  }

  @Override
  public IModifierCore getCore() {
    return core;
  }

  @Override
  public boolean isBasic() {
    return core.isBasic();
  }

  @Override
  public List<IModifierCost> getCosts() {
    return costs;
  }

  @Override
  public List<Supplier<IModifier>> getConflicts() {
    return conflicts;
  }

  @Override
  public Object2DoubleOpenHashMap<Herb> apply(final Object2DoubleOpenHashMap<Herb> costs, CostType phase) {
    if (phase == CostType.NO_COST) {
      return costs;
    }

    final Object2DoubleOpenHashMap<Herb> result = new Object2DoubleOpenHashMap<>(costs);
    for (IModifierCost cost : getCosts()) {
      if (cost.getCost() != phase) {
        continue;
      }

      if (cost.getCost() == CostType.ADDITIONAL_COST) {
        if (result.containsKey(cost.getHerb())) {
          result.put(cost.getHerb(), result.getDouble(cost.getHerb()) + cost.getValue());
        } else {
          result.put(cost.getHerb(), cost.getValue());
        }
      } else if (cost.getCost() == CostType.ALL_COST_MULTIPLIER) {
        for (Object2DoubleMap.Entry<Herb> actualCost : costs.object2DoubleEntrySet()) {
          double val = actualCost.getDoubleValue();
          Herb herb = actualCost.getKey();
          result.put(herb, val + val * cost.getValue());
        }
      }
    }

    return result;
  }
}
