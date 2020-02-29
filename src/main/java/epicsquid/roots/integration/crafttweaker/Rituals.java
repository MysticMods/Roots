package epicsquid.roots.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.ritual.conditions.ConditionItems;
import epicsquid.roots.ritual.conditions.ICondition;
import epicsquid.roots.util.types.Property;
import epicsquid.roots.util.types.PropertyTable;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import net.minecraft.item.crafting.Ingredient;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;

@ZenDocClass("mods.roots.Ritual")
@ZenDocAppend({"docs/include/ritual.example.md"})
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Rituals")
public class Rituals {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "name", info = "the name of the ritual whose ingredients you wish to modify"),
          @ZenDocArg(arg = "inputs", info = "a list of five ingredients (no more, no less)")
      }
  )
  @ZenMethod
  public static void modifyRitual(String name, IIngredient[] inputs) {
    if (inputs.length != 5) {
      CraftTweakerAPI.logError("Rituals must have 5 items: " + name);
      return;
    }
    CraftTweaker.LATE_ACTIONS.add(new Modify(name, Stream.of(inputs).map(CraftTweakerMC::getIngredient).toArray(Ingredient[]::new)));
  }

  private static class Modify extends Action {
    private String name;
    private Ingredient[] inputs;

    private Modify(String name, Ingredient[] inputs) {
      super("Ritual Modification");

      this.name = name;
      this.inputs = inputs;
    }

    @Override
    public String describe() {
      return "Modifying Ritual named: " + name;
    }

    @Override
    public void apply() {
      RitualBase ritual = RitualRegistry.getRitual(name);
      if (ritual == null || ritual.isDisabled()) {
        CraftTweakerAPI.logError("Invalid or disabled ritual or no ritual by the name of \"" + name + "\" exists.");
        return;
      }
      ConditionItems newRecipe = new ConditionItems((Object[]) inputs);
      List<ICondition> conditions = ritual.getConditions();
      ListIterator<ICondition> iterator = conditions.listIterator();
      while (iterator.hasNext()) {
        ICondition cond = iterator.next();
        if (cond instanceof ConditionItems) {
          iterator.remove();
          iterator.add(newRecipe);
          return;
        }
      }
      // If we haven't returned already then, for some reason, the above code failed to find it.
      ritual.addCondition(newRecipe);
    }
  }

  @ZenMethod
  public static Ritual getRitual(String ritualName) {
    if (!ritualName.startsWith("ritual_")) {
      ritualName = "ritual_" + ritualName;
    }
    RitualBase ritual = RitualRegistry.getRitual(ritualName);
    if (ritual == null) {
      return null;
    }

    return new Ritual(ritual);
  }

  @ZenRegister
  @ZenClass("mods." + Roots.MODID + ".Ritual")
  public static class Ritual {
    private RitualBase original;

    public Ritual(RitualBase original) {
      this.original = original;
    }

    public RitualBase getOriginal() {
      return original;
    }

    public <T> Ritual set(String propertyName, T value) {
      PropertyTable table = original.getProperties();
      try {
        Property<T> prop = table.get(propertyName, value);
        table.set(prop, value);
      } catch (ClassCastException error) {
        CraftTweakerAPI.logError("Invalid type for property '" + propertyName + "' for Ritual " + original.getName(), error);
      }
      return this;
    }

    @ZenMethod
    public Ritual setDouble(String propertyName, double value) {
      return set(propertyName, value);
    }

    @ZenMethod
    public Ritual setFloat(String propertyName, float value) {
      return set(propertyName, value);
    }

    @ZenMethod
    public Ritual setInteger(String propertyName, int value) {
      return set(propertyName, value);
    }

    @ZenMethod
    public Ritual setCooldown(int value) {
      return set("cooldown", value);
    }

    @ZenMethod
    public Ritual setDuration(int value) {
      return set("duration", value);
    }

    @ZenMethod
    public Ritual setString(String propertyName, String value) {
      return set(propertyName, value);
    }
  }
}
