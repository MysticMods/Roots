package mysticmods.roots.recipe.pyre;

import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.block.entity.PyreBlockEntity;
import mysticmods.roots.init.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class PyreRecipe extends RootsTileRecipe<PyreInventory, PyreBlockEntity, PyreCrafting> {
  public PyreRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }

  @Override
  public boolean matches(PyreCrafting pInv, Level pLevel) {
    return false;
  }

  @Override
  public ItemStack assemble(PyreCrafting pContainer) {
    return result.copy();
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModRecipes.Serializers.PYRE.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.Types.PYRE;
  }

  public static class Serializer extends RootsRecipe.Serializer<PyreInventory, PyreCrafting, PyreRecipe> {
    public Serializer() {
      super(PyreRecipe::new);
    }
  }

  public static class Builder extends RootsRecipe.Builder {
    protected Builder(ItemStack result) {
      super(result);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
      return ModRecipes.Serializers.PYRE.get();
    }
  }

  public static Builder builder (ItemLike item, int count) {
    return new Builder(new ItemStack(item, count));
  }
}
