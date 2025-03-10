package mysticmods.roots.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.integration.jei.categories.GroveCategory;
import mysticmods.roots.integration.jei.categories.KnifeCategory;
import mysticmods.roots.integration.jei.categories.MortarCategory;
import mysticmods.roots.integration.jei.categories.PyreCategory;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.knife.KnifeRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.recipe.runic.RunicBlockRecipe;
import mysticmods.roots.recipe.runic.RunicEntityRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Collections;

@JeiPlugin
public class RootsJEIPlugin implements IModPlugin {
  @Override
  public ResourceLocation getPluginUid() {
    return RootsAPI.rl("jei");
  }

  public static final RecipeType<GroveRecipe> GROVE_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("grove_recipe"), GroveRecipe.class);
  public static final RecipeType<MortarRecipe> MORTAR_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("mortar_recipe"), MortarRecipe.class);
  public static final RecipeType<KnifeRecipe> KNIFE_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("knife_recipe"), KnifeRecipe.class);
  public static final RecipeType<PyreRecipe> PYRE_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("pyre_recipe"), PyreRecipe.class);
  public static final RecipeType<RunicBlockRecipe> RUNIC_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("runic_recipe"), RunicBlockRecipe.class);
  public static final RecipeType<RunicEntityRecipe> RUNIC_ENTITY_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("runic_entity_recipe"), RunicEntityRecipe.class);

  @Override
  public void registerCategories(IRecipeCategoryRegistration registration) {
    IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();

    registration.addRecipeCategories(new GroveCategory(guiHelper));
    registration.addRecipeCategories(new MortarCategory(guiHelper));
    registration.addRecipeCategories(new PyreCategory(guiHelper));
    registration.addRecipeCategories(new KnifeCategory(guiHelper));
  }

  @Override
  public void registerRecipes(IRecipeRegistration registration) {
    registration.addRecipes(GROVE_RECIPE_TYPE, ResolvedRecipes.GROVE.getRecipes().stream().map(RecipeHolder::value)
        .toList());
    registration.addRecipes(MORTAR_RECIPE_TYPE, ResolvedRecipes.MORTAR.getRecipes().stream().map(RecipeHolder::value)
        .toList());
    registration.addRecipes(PYRE_RECIPE_TYPE, ResolvedRecipes.PYRE.getRecipes().stream().map(RecipeHolder::value)
        .toList());
    registration.addRecipes(KNIFE_RECIPE_TYPE, ResolvedRecipes.KNIFE.getRecipes().stream().map(RecipeHolder::value)
        .toList());
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(ModBlocks.GROVE_CRAFTER.get(), GROVE_RECIPE_TYPE);
    registration.addRecipeCatalyst(ModBlocks.MORTAR.get(), MORTAR_RECIPE_TYPE);
    registration.addRecipeCatalyst(ModBlocks.PYRE.get(), PYRE_RECIPE_TYPE);
    registration.addRecipeCatalyst(ModBlocks.SOUL_PYRE.get(), PYRE_RECIPE_TYPE);
    registration.addRecipeCatalyst(ModBlocks.REINFORCED_PYRE.get(), PYRE_RECIPE_TYPE);
    registration.addRecipeCatalyst(ModBlocks.REINFORCED_SOUL_PYRE.get(), PYRE_RECIPE_TYPE);
    registration.addRecipeCatalysts(KNIFE_RECIPE_TYPE, ModItems.COPPER_KNIFE.get(), ModItems.SILVER_KNIFE.get(), ModItems.IRON_KNIFE.get(), ModItems.GOLD_KNIFE.get(), ModItems.DIAMOND_KNIFE.get(), ModItems.NETHERITE_KNIFE.get(), ModItems.STONE_KNIFE.get(), ModItems.WOODEN_KNIFE.get());
  }

  @Override
  public void registerIngredientAliases(IIngredientAliasRegistration registration) {
    IModPlugin.super.registerIngredientAliases(registration);
  }
}
