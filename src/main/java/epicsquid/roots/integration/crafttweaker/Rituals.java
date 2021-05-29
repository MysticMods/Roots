package epicsquid.roots.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.integration.crafttweaker.recipes.CTRitualRecipe;
import epicsquid.roots.properties.Property;
import epicsquid.roots.properties.PropertyTable;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;

@ZenDocClass("mods.roots.Rituals")
@ZenDocAppend({"docs/include/rituals.example.md"})
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Rituals")
public class Rituals {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "name", info = "the name of the ritual whose ingredients you wish to modify"),
          @ZenDocArg(arg = "inputs", info = "a list of five ingredients (no more, no less)")
      },
      description = "Modifies the specified Ritual recipe to use the five ingredients specified."
  )
  @ZenMethod
  public static void modifyRitual(String name, IIngredient[] inputs) {
    if (inputs.length != 5) {
      CraftTweakerAPI.logError("Rituals must have 5 thaumcraft.items: " + name);
      return;
    }
    CraftTweaker.LATE_ACTIONS.add(new Modify(name, Arrays.asList(inputs)));
  }

  private static class Modify extends Action {
    private String name;
    private List<IIngredient> inputs;

    private Modify(String name, List<IIngredient> inputs) {
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
      CTRitualRecipe recipe = new CTRitualRecipe(ritual, inputs);
      ritual.setRecipe(recipe);
    }
  }

  @ZenDocMethod(order = 2,
      args = {
          @ZenDocArg(arg = "ritualName", info = "the name of the ritual to be fetched; will prepend `ritual_` if it doesn't start with `ritual_`.")
      },
      description = "Returns a Ritual object which can have its properties modified."
  )
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

  @ZenDocClass("mods.roots.Ritual")
  @ZenDocAppend({"docs/include/ritual.example.md"})
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
    @ZenDocMethod(
        order = 1,
        args = {
            @ZenDocArg(arg = "propertyName", info = "sets propertyName to the specified double value"),
            @ZenDocArg(arg = "value", info = "the value to set propertyName to; if this property is *not* a double, an error will be raised")
        },
        description = "Sets a propertyName to a specified value (throwing an exception if this is an invalid type for that property), then returns the ritual, allowing for chained functions."
    )
    public Ritual setDouble(String propertyName, double value) {
      return set(propertyName, value);
    }

    @ZenMethod
    @ZenDocMethod(
        order = 2,
        args = {
            @ZenDocArg(arg = "propertyName", info = "sets propertyName to the specified float value"),
            @ZenDocArg(arg = "value", info = "the value to set propertyName to; if this property is *not* a float, an error will be raised")
        },
        description = "Sets a propertyName to a specified value (throwing an exception if this is an invalid type for that property), then returns the ritual, allowing for chained functions."
    )
    public Ritual setFloat(String propertyName, float value) {
      return set(propertyName, value);
    }

    @ZenMethod
    @ZenDocMethod(
        order = 3,
        args = {
            @ZenDocArg(arg = "propertyName", info = "sets propertyName to the specified integer value"),
            @ZenDocArg(arg = "value", info = "the value to set propertyName to; if this property is *not* a integer, an error will be raised")
        },
        description = "Sets a propertyName to a specified value (throwing an exception if this is an invalid type for that property), then returns the ritual, allowing for chained functions."
    )
    public Ritual setInteger(String propertyName, int value) {
      return set(propertyName, value);
    }

    @ZenMethod
    @ZenDocMethod(
        order = 4,
        args = {
            @ZenDocArg(arg = "value", info = "the new duration for the ritual")
        },
        description = "Changes the duration of the ritual and returns the Ritual object for further modification. Is shorthand for `setInteger(\"duration\", value)`."
    )
    public Ritual setDuration(int value) {
      return set("duration", value);
    }

    @ZenMethod
    @ZenDocMethod(
        order = 5,
        args = {
            @ZenDocArg(arg = "propertyName", info = "sets propertyName to the specified string value"),
            @ZenDocArg(arg = "value", info = "the value to set propertyName to; if this property is *not* a string, an error will be raised")
        },
        description = "Sets a propertyName to a specified value (throwing an exception if this is an invalid type for that property), then returns the ritual, allowing for chained functions."
    )
    public Ritual setString(String propertyName, String value) {
      return set(propertyName, value);
    }
  }
}
