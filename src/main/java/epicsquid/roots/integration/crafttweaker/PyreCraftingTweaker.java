package epicsquid.roots.integration.crafttweaker;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.PyreCraftingRecipe;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.stream.Stream;

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
      }
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
          @ZenDocArg(arg = "xp", info = "the amount of xp in levels that is granted after crafting")
      }
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient[] inputs, int xp) {
    if (inputs.length != 5) {
      CraftTweakerAPI.logError("Pyre Crafting Ritual must have 5 items: " + name);
      return;
    }
    CraftTweaker.LATE_ACTIONS.add(new Add(name, CraftTweakerMC.getItemStack(output), Stream.of(inputs).map(CraftTweakerMC::getIngredient).toArray(Ingredient[]::new), xp));
  }

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "output", info = "the output of the recipe to remove")
      }
  )
  @ZenMethod
  public static void removeRecipe(IItemStack output) {
    CraftTweaker.LATE_ACTIONS.add(new Remove(CraftTweakerMC.getItemStack(output)));
  }

  private static class Add extends BaseAction {
    private String name;
    private ItemStack output;
    private Ingredient[] inputs;
    private int xp = 0;

    private Add(String name, ItemStack output, Ingredient[] inputs, int xp) {
      super("Pyre Crafting Ritual Add");

      this.name = name;
      this.output = output;
      this.inputs = inputs;
      this.xp = xp;
    }

    @Override
    public String describe() {
      return "Adding Pyre Crafting Ritual for " + LogHelper.getStackDescription(output);
    }

    @Override
    public void apply() {
      PyreCraftingRecipe recipe = new PyreCraftingRecipe(this.output, this.xp);
      recipe.addIngredients((Object[]) inputs);
      recipe.setName(name);
      ModRecipes.addPyreCraftingRecipe(new ResourceLocation(Roots.MODID, name), recipe);
    }
  }

  private static class Remove extends BaseAction {
    private ItemStack output;

    @Override
    public String describe() {
      return "Removing Pyre Altar Crafting recipe " + LogHelper.getStackDescription(output);
    }

    private Remove(ItemStack output) {
      super("Pyre Crafting Ritual");
      this.output = output;
    }

    @Override
    public void apply() {
      ModRecipes.removePyreCraftingRecipe(output);
    }
  }
}
