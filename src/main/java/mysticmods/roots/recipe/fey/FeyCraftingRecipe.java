package mysticmods.roots.recipe.fey;

import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.block.entity.FeyCrafterBlockEntity;
import mysticmods.roots.init.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class FeyCraftingRecipe extends RootsTileRecipe<FeyCraftingInventory, FeyCrafterBlockEntity, FeyCrafting> {
  public FeyCraftingRecipe(NonNullList<Ingredient> ingredients, ItemStack result, ResourceLocation recipeId) {
    super(ingredients, result, recipeId);
  }

  @Override
  public boolean matches(FeyCrafting pInv, Level pLevel) {
    return false;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModRecipes.Serializers.FEY_CRAFTING.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.Types.FEY_CRAFTING;
  }

  public static class Serializer extends RootsRecipe.Serializer<FeyCraftingInventory, FeyCrafting, FeyCraftingRecipe> {
    public Serializer() {
      super(FeyCraftingRecipe::new);
    }
  }

  public static class Builder extends RootsRecipe.Builder {
    protected Builder(ItemLike item, int count) {
      super(item, count);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
      return ModRecipes.Serializers.FEY_CRAFTING.get();
    }
  }

  public static Builder builder (ItemLike item, int count) {
    return new Builder(item, count);
  }
}
