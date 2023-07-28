package mysticmods.roots.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.IDescribedRegistryEntry;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.integration.jei.categories.GroveCategory;
import mysticmods.roots.item.TokenItem;
import mysticmods.roots.recipe.grove.GroveRecipe;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class RootsJEIPlugin implements IModPlugin {
  @Override
  public ResourceLocation getPluginUid() {
    return RootsAPI.rl("jei");
  }

  @Override
  public void registerItemSubtypes(ISubtypeRegistration registration) {
    registration.registerSubtypeInterpreter(ModItems.TOKEN.get(), (itemStack, uidContext) -> {
      if (uidContext == UidContext.Ingredient) {
        TokenItem.Type type = TokenItem.getType(itemStack);
        if (type == null) {
          return IIngredientSubtypeInterpreter.NONE;
        }
        IDescribedRegistryEntry entry = switch (type) {
          case RITUAL -> TokenItem.getRitual(itemStack);
          case SPELL -> TokenItem.getSpell(itemStack);
          case MODIFIER -> TokenItem.getSingleModifier(itemStack);
          default -> null;
        };
        if (entry == null) {
          return "";
        }
        return entry.getDescriptionId();
      } else {
        return IIngredientSubtypeInterpreter.NONE;
      }
    });
  }

  public static final RecipeType<GroveRecipe> GROVE_RECIPE_TYPE = new RecipeType<>(RootsAPI.rl("grove_recipe"), GroveRecipe.class);

  @Override
  public void registerCategories(IRecipeCategoryRegistration registration) {
    IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();

    registration.addRecipeCategories(new GroveCategory(guiHelper));
  }

  @Override
  public void registerRecipes(IRecipeRegistration registration) {
    registration.addRecipes(GROVE_RECIPE_TYPE, ResolvedRecipes.GROVE.getRecipes());
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(ModBlocks.GROVE_CRAFTER.asStack(), GROVE_RECIPE_TYPE);
  }
}
