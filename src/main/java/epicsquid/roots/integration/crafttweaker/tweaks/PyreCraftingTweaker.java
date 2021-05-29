package epicsquid.roots.integration.crafttweaker.tweaks;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.crafttweaker.Action;
import epicsquid.roots.integration.crafttweaker.recipes.CTPyreCraftingRecipe;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;

@ZenDocClass("mods.roots.Pyre")
@ZenDocAppend({"docs/include/pyre.example.md"})
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Pyre")
public class PyreCraftingTweaker {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "name", info = "the name of the recipe being added; if replacing an existing game recipe, ensure the correct name is used"),
          @ZenDocArg(arg = "output", info = "the output of this recipe"),
          @ZenDocArg(arg = "inputs", info = "a list of five ingredients (no more, no less)")
      },
      description = "Adds a Pyre thaumcraft.crafting recipe that produces output after the standard amount of time, with the specified input ingredients (with potential transformers)."
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient[] inputs) {
    addRecipe(name, output, inputs, 0);
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "name", info = "the name of the recipe being added; if replacing an existing game recipe, ensure the correct name is used"),
          @ZenDocArg(arg = "output", info = "the output of this recipe"),
          @ZenDocArg(arg = "inputs", info = "a list of five ingredients"),
          @ZenDocArg(arg = "xp", info = "the amount of xp in levels that is granted after thaumcraft.crafting")
      },
      description = "Adds a Pyre thaumcraft.crafting recipe that produces output after the standard amount of time, with the specified input ingredients (with potential transformers). Allows for the specification of an amount of experience to be generated once the craft is finished."
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient[] inputs, int xp) {
    if (inputs.length != 5) {
      CraftTweakerAPI.logError("Pyre Crafting Ritual must have 5 thaumcraft.items: " + name);
      return;
    }
    CraftTweaker.LATE_ACTIONS.add(new Add(name, CraftTweakerMC.getItemStack(output), Arrays.asList(inputs), xp));
  }

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "output", info = "the output of the recipe to remove")
      },
      description = "Removes a Pyre thaumcraft.crafting recipe based on its output."
  )
  @ZenMethod
  public static void removeRecipe(IItemStack output) {
    CraftTweaker.LATE_ACTIONS.add(new Remove(CraftTweakerMC.getItemStack(output)));
  }

  private static class Add extends Action {
    private String name;
    private ItemStack output;
    private List<IIngredient> inputs;
    private int xp;

    private Add(String name, ItemStack output, List<IIngredient> inputs, int xp) {
      super("Pyre Crafting Ritual Add");

      this.name = name;
      this.output = output;
      this.inputs = inputs;
      this.xp = xp;
    }

    @Override
    public String describe() {
      return "Adding Pyre Crafting Ritual for " + output;
    }

    @Override
    public void apply() {
      CTPyreCraftingRecipe recipe = new CTPyreCraftingRecipe(this.output, this.inputs, this.xp);
      recipe.setName(name);
      ModRecipes.addPyreCraftingRecipe(new ResourceLocation(Roots.MODID, name), recipe);
    }
  }

  private static class Remove extends Action {
    private ItemStack output;

    @Override
    public String describe() {
      return "Removing Pyre Altar Crafting recipe " + output;
    }

    private Remove(ItemStack output) {
      super("Pyre Crafting Ritual");
      this.output = output;
    }

    @Override
    public void apply() {
      if (!ModRecipes.removePyreCraftingRecipe(output)) {
        CraftTweakerAPI.logError("Couldn't properly remove Pyre Crafting Recipe: " + output.toString());
      }
    }
  }
}
