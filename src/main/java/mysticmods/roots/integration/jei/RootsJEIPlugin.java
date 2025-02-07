package mysticmods.roots.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.integration.jei.categories.GroveCategory;
import mysticmods.roots.integration.jei.categories.MortarCategory;
import mysticmods.roots.integration.jei.categories.PyreCategory;
import mysticmods.roots.recipe.bark.BarkRecipe;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.recipe.runic.RunicBlockRecipe;
import mysticmods.roots.recipe.runic.RunicEntityRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

@JeiPlugin
public class RootsJEIPlugin implements IModPlugin {
  @Override
  public ResourceLocation getPluginUid() {
    return RootsAPI.rl("jei");
  }

  public static final RecipeType<GroveRecipe> GROVE_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("grove_recipe"), GroveRecipe.class);
  public static final RecipeType<MortarRecipe> MORTAR_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("mortar_recipe"), MortarRecipe.class);
  public static final RecipeType<BarkRecipe> BARK_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("bark_recipe"), BarkRecipe.class);
  public static final RecipeType<PyreRecipe> PYRE_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("pyre_recipe"), PyreRecipe.class);
  public static final RecipeType<RunicBlockRecipe> RUNIC_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("runic_recipe"), RunicBlockRecipe.class);
  public static final RecipeType<RunicEntityRecipe> RUNIC_ENTITY_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("runic_entity_recipe"), RunicEntityRecipe.class);

  @Override
  public void registerCategories(IRecipeCategoryRegistration registration) {
    IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();

    registration.addRecipeCategories(new GroveCategory(guiHelper));
    registration.addRecipeCategories(new MortarCategory(guiHelper));
    registration.addRecipeCategories(new PyreCategory(guiHelper));
  }

  @Override
  public void registerRecipes(IRecipeRegistration registration) {
    registration.addRecipes(GROVE_RECIPE_TYPE, ResolvedRecipes.GROVE.getRecipes().stream().map(RecipeHolder::value)
        .toList());
    registration.addRecipes(MORTAR_RECIPE_TYPE, ResolvedRecipes.MORTAR.getRecipes().stream().map(RecipeHolder::value)
        .toList());
    registration.addRecipes(PYRE_RECIPE_TYPE, ResolvedRecipes.PYRE.getRecipes().stream().map(RecipeHolder::value)
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
  }
}
