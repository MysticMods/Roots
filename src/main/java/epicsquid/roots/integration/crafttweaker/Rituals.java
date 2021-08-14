package epicsquid.roots.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.integration.crafttweaker.recipes.CTRitualRecipe;
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
      CraftTweakerAPI.logError("Rituals must have 5 items: " + name);
      return;
    }
    CraftTweaker.LATE_ACTIONS.add(new Modify(name, Arrays.asList(inputs)));
  }

  private static class Modify extends Action {
    private final String name;
    private final List<IIngredient> inputs;

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

}
