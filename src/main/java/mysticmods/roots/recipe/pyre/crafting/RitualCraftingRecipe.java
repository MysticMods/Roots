package mysticmods.roots.recipe.pyre.crafting;

import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.block.entity.PyreBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.recipe.pyre.PyreInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class RitualCraftingRecipe extends RootsTileRecipe<PyreInventory, PyreBlockEntity, RitualCrafting> {
  public RitualCraftingRecipe(NonNullList<Ingredient> ingredients, ItemStack result, ResourceLocation recipeId) {
    super(ingredients, result, recipeId);
  }

  @Override
  public boolean matches(RitualCrafting pInv, Level pLevel) {
    return false;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModRecipes.Serializers.RITUAL_CRAFTING.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.Types.RITUAL_CRAFTING;
  }

  public static class Serializer extends RootsRecipe.Serializer<PyreInventory, RitualCrafting, RitualCraftingRecipe> {
    public Serializer() {
      super(RitualCraftingRecipe::new);
    }
  }

  public static class Builder extends RootsRecipe.Builder {
    protected Builder(ItemLike item, int count) {
      super(item, count);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
      return ModRecipes.Serializers.RITUAL_CRAFTING.get();
    }
  }

  public static Builder builder (ItemLike item, int count) {
    return new Builder(item, count);
  }
}
