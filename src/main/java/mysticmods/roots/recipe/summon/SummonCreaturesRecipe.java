package mysticmods.roots.recipe.summon;

import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.block.entity.PyreBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import noobanidus.libs.noobutil.ingredient.IngredientStack;

public class SummonCreaturesRecipe extends RootsRecipe<SummonCreaturesInventory, PyreBlockEntity, SummonCreaturesCrafting> {
  public SummonCreaturesRecipe(NonNullList<IngredientStack> ingredients, ItemStack result, ResourceLocation recipeId) {
    super(ingredients, result, recipeId);
  }

  // TODO:
  @Override
  public boolean matches(SummonCreaturesCrafting pInv, World pLevel) {
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
