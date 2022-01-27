package mysticmods.roots.recipe.pyre.crafting;

import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.block.entity.PyreBlockEntity;
import mysticmods.roots.recipe.pyre.PyreInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import noobanidus.libs.noobutil.ingredient.IngredientStack;

public class RitualCraftingRecipe extends RootsRecipe<PyreInventory, PyreBlockEntity, RitualCrafting> {
  public RitualCraftingRecipe(NonNullList<IngredientStack> ingredients, ItemStack result, ResourceLocation recipeId) {
    super(ingredients, result, recipeId);
  }

  // TODO:
  @Override
  public boolean matches(RitualCrafting pInv, World pLevel) {
    return false;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return null;
  }

  @Override
  public IRecipeType<?> getType() {
    return null;
  }
}
