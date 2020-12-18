package epicsquid.roots.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import epicsquid.roots.Roots;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.modifiers.Cost;
import epicsquid.roots.modifiers.CostType;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import epicsquid.roots.util.zen.ZenDocProperty;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Costs")
@ZenDocAppend({"docs/include/costs.example.md"})
@ZenDocClass("mods." + Roots.MODID + ".Costs")
@SuppressWarnings("unused")
public class Costs {
  @ZenProperty
  @ZenDocProperty(order = 1, description = "No Cost")
  public static final Cost no_cost = new Cost("no_cost", CostType.NO_COST);
  @ZenProperty
  @ZenDocProperty(order = 2, description = "Additional Cost")
  public static final Cost additional_cost = new Cost("additional_cost", CostType.ADDITIONAL_COST);
  @ZenProperty
  @ZenDocProperty(order = 3, description = "Modifies all existing costs")
  public static final Cost all_cost_multiplier = new Cost("all_cost_multiplier", CostType.ALL_COST_MULTIPLIER);

  @ZenRegister
  @ZenClass("mods." + Roots.MODID + ".Cost")
  public static class Cost {
    private String herbName;
    private CostType original;

    public Cost(String herbName, CostType type) {
      this.herbName = herbName;
      this.original = type;
    }

    @ZenMethod
    @ZenDocMethod(order = 1, description = "returns the string name of the cost")
    public String getCostName() {
      return herbName;
    }

    public CostType getOriginal() {
      return original;
    }
  }
}
