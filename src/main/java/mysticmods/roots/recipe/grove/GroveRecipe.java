package mysticmods.roots.recipe.grove;

import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.blockentity.GroveCrafterBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

public class GroveRecipe extends RootsTileRecipe<GroveInventoryWrapper, GroveCrafterBlockEntity, GroveCrafting> {
  public GroveRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }

  @Override
  public ItemStack assemble(GroveCrafting pContainer) {
    return super.assemble(pContainer);
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.GROVE_CRAFTING.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.GROVE.get();
  }

  public static class Serializer extends RootsRecipe.Serializer<GroveInventoryWrapper, GroveCrafting, GroveRecipe> {
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
      return ModSerializers.GROVE_CRAFTING.get();
    }
  }

  public static Builder builder(ItemStack stack) {
    return new Builder(stack);
  }

  public static Builder builder(ItemLike item, int count) {
    return new Builder(new ItemStack(item, count));
  }

  public static Builder builder (ItemLike item) {
    return builder(item, 1);
  }
}
