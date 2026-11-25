package mysticmods.roots.integration.jei.categories.defaulted;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.recipe.transmutation.TransmutationRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class FungalTransmuterCategory extends RootsDefaultCategory<TransmutationRecipe> {
  public FungalTransmuterCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.TRANSMUTATION_RECIPE_TYPE, helper, () -> new ItemStack(ModBlocks.FUNGAL_TRANSMUTER.get()), Component.translatable("roots.jei.fungal_transmuter"));
  }

  @Override
  protected void addDevice(IRecipeLayoutBuilder builder) {
    builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 80, 26)
        .addIngredients(Ingredient.of(ModItems.FUNGAL_TRANSMUTER.get()));
  }
}
