package mysticmods.roots.recipe.chrysopoeia;

import mysticmods.roots.api.recipe.PlayerOffhandInventoryHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ChrysopoeiaRecipe implements IRecipe<PlayerOffhandInventoryHandler> {
  private final NonNullList<Ingredient> ingredients;
  private final ItemStack result;
  private final ResourceLocation recipeId;

  public ChrysopoeiaRecipe(NonNullList<Ingredient> ingredients, ItemStack result, ResourceLocation recipeId) {
    this.ingredients = ingredients;
    this.result = result;
    this.recipeId = recipeId;
  }

  @Override
  public boolean matches(PlayerOffhandInventoryHandler pInv, World pLevel) {
    return ingredients.get(0).test(pInv.getItem(0));
  }

  // TODO:
  @Override
  public ItemStack assemble(PlayerOffhandInventoryHandler pInv) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canCraftInDimensions(int pWidth, int pHeight) {
    return true;
  }

  @Override
  public ItemStack getResultItem() {
    return result.copy();
  }

  @Override
  public ResourceLocation getId() {
    return recipeId;
  }

  // TODO:
  @Override
  public IRecipeSerializer<?> getSerializer() {
    return null;
  }

  @Override
  public IRecipeType<?> getType() {
    return null;
  }
}
