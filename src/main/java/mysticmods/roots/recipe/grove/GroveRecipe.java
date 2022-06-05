package mysticmods.roots.recipe.grove;

import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.block.entity.GroveCrafterBlockEntity;
import mysticmods.roots.init.ModRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class GroveRecipe extends RootsTileRecipe<GroveCrafterInventory, GroveCrafterBlockEntity, GroveCrafting> {
  public GroveRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }

  @Override
  public boolean matches(GroveCrafting pInv, Level pLevel) {
    return false;
  }

  @Override
  public ItemStack assemble(GroveCrafting pContainer) {
    return result.copy();
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModRecipes.Serializers.GROVE_CRAFTING.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.Types.GROVE;
  }

  public static class Serializer extends RootsRecipe.Serializer<GroveCrafterInventory, GroveCrafting, GroveRecipe> {
    public Serializer() {
      super(GroveRecipe::new);
    }
  }

  public static class Builder extends RootsRecipe.Builder {

    protected Builder(ItemStack result) {
      super(result);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
      return ModRecipes.Serializers.GROVE_CRAFTING.get();
    }
  }

  public static Builder builder (ItemStack stack) {
    return new Builder(stack);
  }
}
