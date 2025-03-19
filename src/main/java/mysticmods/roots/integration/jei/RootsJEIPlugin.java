package mysticmods.roots.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.integration.jei.categories.*;
import mysticmods.roots.integration.jei.categories.ingredient.RootsEntityHelper;
import mysticmods.roots.integration.jei.categories.ingredient.RootsEntityRenderer;
import mysticmods.roots.integration.jei.categories.ingredient.RootsEntityType;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.knife.KnifeRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.recipe.runic.RunicBlockRecipe;
import mysticmods.roots.recipe.runic.RunicEntityRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

@JeiPlugin
public class RootsJEIPlugin implements IModPlugin {
  public static final IIngredientType<RootsEntityType> ENTITY_TYPE = () -> RootsEntityType.class;

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
    registration.addRecipeCategories(new RunicBlockCategory(guiHelper));
    registration.addRecipeCategories(new RunicEntityCategory(guiHelper));
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
    registration.addRecipes(RUNIC_RECIPE_TYPE, ResolvedRecipes.RUNIC_BLOCK.getRecipes().stream().map(RecipeHolder::value)
        .toList());
    registration.addRecipes(RUNIC_ENTITY_RECIPE_TYPE, ResolvedRecipes.RUNIC_ENTITY.getRecipes().stream().map(RecipeHolder::value)
        .toList());
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalysts(GROVE_RECIPE_TYPE, ModBlocks.GROVE_CRAFTER.get(), ModBlocks.GROVE_PEDESTAL.get(), ModBlocks.WILDWOOD_PEDESTAL.get());
    registration.addRecipeCatalysts(MORTAR_RECIPE_TYPE, ModBlocks.MORTAR.get(), ModItems.PESTLE.get());
        registration.addRecipeCatalysts(PYRE_RECIPE_TYPE, ModBlocks.PYRE.get(), ModBlocks.SOUL_PYRE.get(), ModBlocks.REINFORCED_PYRE.get(), ModBlocks.REINFORCED_SOUL_PYRE.get());
    registration.addRecipeCatalysts(KNIFE_RECIPE_TYPE, ModItems.COPPER_KNIFE.get(), ModItems.SILVER_KNIFE.get(), ModItems.IRON_KNIFE.get(), ModItems.GOLD_KNIFE.get(), ModItems.DIAMOND_KNIFE.get(), ModItems.NETHERITE_KNIFE.get(), ModItems.STONE_KNIFE.get(), ModItems.WOODEN_KNIFE.get());
    registration.addRecipeCatalyst(ModItems.RUNIC_SHEARS.get(), RUNIC_RECIPE_TYPE);
    registration.addRecipeCatalyst(ModItems.RUNIC_SHEARS.get(), RUNIC_ENTITY_RECIPE_TYPE);
  }

  @Override
  public void registerIngredients(IModIngredientRegistration registration) {
    registration.register(ENTITY_TYPE, Collections.emptyList(), new RootsEntityHelper(), new RootsEntityRenderer(16), BuiltInRegistries.ENTITY_TYPE.byNameCodec().xmap(RootsEntityType::new, RootsEntityType::entity));
  }

  public static IJeiRuntime runtime = null;

  @Override
  public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
    runtime = jeiRuntime;
  }

  @Override
  public void onRuntimeUnavailable() {
    runtime = null;
  }

  @Nullable
  public static <V> ITypedIngredient<V> getTypedIngredient (V ingredient) {
    if (runtime == null) {
      return null;
    }
    return runtime.getIngredientManager().createTypedIngredient(ingredient).orElse(null);
  }

  @Nullable
  public static ITypedIngredient<ItemStack> createItemIngredient (ItemLike item) {
    return getTypedIngredient(new ItemStack(item));
  }
}
