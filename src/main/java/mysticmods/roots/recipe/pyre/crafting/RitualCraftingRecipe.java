package mysticmods.roots.recipe.pyre.crafting;

import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.block.entity.PyreBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.recipe.pyre.PyreInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RitualCraftingRecipe extends RootsTileRecipe<PyreInventory, PyreBlockEntity, RitualCrafting> {
  public RitualCraftingRecipe(NonNullList<Ingredient> ingredients, ItemStack result, ResourceLocation recipeId) {
    super(ingredients, result, recipeId);
  }

  @Override
  public boolean matches(RitualCrafting pInv, World pLevel) {
    return false;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return ModRecipes.Serializers.RITUAL_CRAFTING.get();
  }

  @Override
  public IRecipeType<?> getType() {
    return ModRecipes.Types.RITUAL_CRAFTING;
  }

  public static class Serializer extends RootsRecipe.Serializer<PyreInventory, RitualCrafting, RitualCraftingRecipe> {
    public Serializer() {
      super(RitualCraftingRecipe::new);
    }
  }

  public static class Builder extends RootsRecipe.Builder {
    protected Builder(IItemProvider item, int count) {
      super(item, count);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
      return ModRecipes.Serializers.RITUAL_CRAFTING.get();
    }
  }

  public static Builder builder (IItemProvider item, int count) {
    return new Builder(item, count);
  }
}
