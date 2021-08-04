package epicsquid.roots.modifiers;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.properties.Property;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.util.types.RegistryItem;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class Modifier extends RegistryItem implements IModifier {
  private final IModifierCore core;
  private final Set<IModifier> conflicts = new HashSet<>();
  private final Map<CostType, IModifierCost> costs;
  private List<Property<SpellBase.ModifierCost>> propertyCache;

  public Modifier(ResourceLocation name, IModifierCore core, Map<CostType, IModifierCost> costs) {
    setRegistryName(name);
    this.costs = costs;
    this.core = core;
  }

  @Override
  public boolean isDisabled() {
    return ModifierRegistry.isDisabled(this);
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

  private ItemStack modifierStack = ItemStack.EMPTY;

  public ItemStack getModifierStack () {
    if (modifierStack.isEmpty()) {
      modifierStack = new ItemStack(ModItems.spell_modifier);
      NBTTagCompound tag = ItemUtil.getOrCreateTag(modifierStack);
      tag.setString("modifier", this.getRegistryName().toString());
    }
    return modifierStack;
  }

  @Override
  public IModifierCore getCore() {
    return core;
  }

  @Override
  public Map<CostType, IModifierCost> getCosts() {
    return costs;
  }

  @Override
  public Set<IModifier> getConflicts() {
    return conflicts;
  }

  private String identifier = null;

  @Override
  public String getIdentifier() {
    if (identifier == null) {
      identifier = getRegistryName().toString();
    }
    return identifier;
  }

  @Override
  public List<Property<SpellBase.ModifierCost>> asProperties() {
    if (propertyCache == null) {
      propertyCache = new ArrayList<>();
      for (IModifierCost entry : getCosts().values()) {
        String name = entry.asPropertyName();
        if (name == null) {
          continue;
        }
        Property<SpellBase.ModifierCost> prop = new Property<>(name, new SpellBase.ModifierCost(entry));
        propertyCache.add(prop);
      }
    }
    return propertyCache;
  }

  @Override
  public void addConflict(IModifier supplier, boolean reverse) {
    conflicts.add(supplier);
    if (reverse) {
      supplier.addConflict(this, false);
    }
  }

  public void addConflicts(IModifier... suppliers) {
    for (IModifier sup : suppliers) {
      addConflict(sup);
    }
  }

  public void replaceCosts (List<SpellBase.ModifierCost> costs) {
    this.costs.clear();
    for (SpellBase.ModifierCost cost : costs) {
      this.costs.put(cost.getType(), cost.asCost());
    }
  }

  @Override
  public Object2DoubleOpenHashMap<Herb> apply(final Object2DoubleOpenHashMap<Herb> costs, CostType phase) {
    if (phase == CostType.NO_COST) {
      return costs;
    }

    final Object2DoubleOpenHashMap<Herb> result = new Object2DoubleOpenHashMap<>(costs);
    for (Map.Entry<CostType, IModifierCost> c : getCosts().entrySet()) {
      IModifierCost cost = c.getValue();
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
      } else if (cost.getCost() == CostType.SPECIFIC_COST_ADJUSTMENT) {
        for (Object2DoubleMap.Entry<Herb> actualCost : costs.object2DoubleEntrySet()) {
          double val = actualCost.getDoubleValue();
          Herb herb = actualCost.getKey();
          result.put(herb, val + cost.getValue());
        }
      } else if (cost.getCost() == CostType.SPECIFIC_COST_MULTIPLIER) {
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
