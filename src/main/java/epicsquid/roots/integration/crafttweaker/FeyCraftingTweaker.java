package epicsquid.roots.integration.crafttweaker;

import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.FeyCraftingRecipe;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Map;
import java.util.stream.Stream;

@ZenDocClass("mods.roots.Fey")
@ZenDocAppend({"docs/include/fey_crafting.example.md"})
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Fey")
public class FeyCraftingTweaker {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg="name", info="the name of the recipe; if replacing an existing recipe, be sure to use the same name to ensure Patchouli continuity"),
          @ZenDocArg(arg="output", info="the itemstack produced by this recipe"),
          @ZenDocArg(arg="inputs", info="an array of IIngredients that make up the recipe; must contain 5 items")
      }
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient[] inputs) throws IllegalArgumentException {
    addRecipe(name, output, inputs, 0);
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg="name", info="the name of the recipe; if replacing an existing recipe, be sure to use the same name to ensure Patchouli continuity"),
          @ZenDocArg(arg="output", info="the itemstack produced by this recipe"),
          @ZenDocArg(arg="inputs", info="an array of IIngredients that make up the recipe; must contain 5 items"),
          @ZenDocArg(arg="xp", info="the amount of xp (in levels) to reward the player for crafting this recipe")
      }
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient[] inputs, int xp) throws IllegalArgumentException {
    if (inputs.length != 5) {
      throw new IllegalArgumentException("Fey Crafting Ritual must have 5 items: " + name);
    }
    CraftTweaker.LATE_ACTIONS.add(new Add(name, CraftTweakerMC.getItemStack(output), Stream.of(inputs).map(CraftTweakerMC::getIngredient).toArray(Ingredient[]::new), xp));
  }

  @ZenDocMethod(
      order=3,
      args = {
          @ZenDocArg(arg="output", info="the item produced by the recipe you wish to remove")
      }
  )
  @ZenMethod
  public static void removeRecipe(IItemStack output) {
    ResourceLocation recipeName = null;
    for (Map.Entry<ResourceLocation, FeyCraftingRecipe> r : ModRecipes.getFeyCraftingRecipes().entrySet()) {
      if (output.matches(InputHelper.toIItemStack(r.getValue().getResult()))) {
        recipeName = r.getKey();
        break;
      }
    }

    if (recipeName == null) {
      CraftTweakerAPI.logError("No Fey Crafting recipe found for output: " + LogHelper.getStackDescription(output));
    } else {
      CraftTweaker.LATE_ACTIONS.add(new Remove(recipeName));
    }
  }

  private static class Add extends Action {
    private String name;
    private ItemStack output;
    private Ingredient[] inputs;
    private int xp = 0;

    private Add(String name, ItemStack output, Ingredient[] inputs, int xp) {
      super("Fey Crafting Add");

      this.name = name;
      this.output = output;
      this.inputs = inputs;
      this.xp = xp;
    }

    @Override
    public String describe() {
      return "Adding Fey Crafting Ritual for " + LogHelper.getStackDescription(output);
    }

    @Override
    public void apply() {
      FeyCraftingRecipe recipe = new FeyCraftingRecipe(this.output, this.xp);
      recipe.addIngredients((Object[]) inputs);
      ModRecipes.addFeyCraftingRecipe(new ResourceLocation(Roots.MODID, name), recipe);
    }
  }

  private static class Remove extends Action {
    private ResourceLocation name;

    @Override
    public String describe() {
      return "Removing Fey Crafting recipe " + name.toString();
    }

    private Remove(ResourceLocation recipeName) {
      super("Fey Crafting Remove");
      this.name = recipeName;
    }

    @Override
    public void apply() {
      ModRecipes.removeFeyCraftingRecipe(name);
    }
  }
}

