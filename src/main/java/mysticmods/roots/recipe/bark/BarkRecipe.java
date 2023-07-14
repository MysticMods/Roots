package mysticmods.roots.recipe.bark;

import mysticmods.roots.api.recipe.WorldRecipe;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BarkRecipe extends WorldRecipe<BarkCrafting> {
  public BarkRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }

  @Override
  public ItemStack getBaseResultItem() {
    return getResultItem();
  }

  @Override
  public BlockState modifyState(BarkCrafting pContainer, BlockState currentState) {
    BlockState newState = outputState;

    if (currentState.getBlock() instanceof RotatedPillarBlock && outputState.getBlock() instanceof RotatedPillarBlock) {
      newState = outputState.setValue(RotatedPillarBlock.AXIS, currentState.getValue(RotatedPillarBlock.AXIS));
    }

    return super.modifyState(pContainer, newState);
  }

  @Override
  public void setIngredients(NonNullList<Ingredient> ingredients) {
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.BARK.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.BARK.get();
  }

  public static class Serializer extends WorldRecipe.Serializer<BarkCrafting, BarkRecipe> {
    public Serializer() {
      super(BarkRecipe::new);
    }
  }

  public static class Builder extends WorldRecipe.Builder {
    public Builder() {
    }

    public Builder(ItemStack result) {
      super(result);
    }

    @Override
    protected boolean requireIngredients() {
      return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
      return ModSerializers.BARK.get();
    }
  }

  public static Builder builder(ItemStack stack) {
    return new Builder(stack);
  }

  public static Builder builder(ItemLike item, int count) {
    return new Builder(new ItemStack(item, count));
  }

  public static Builder builder(ItemLike item) {
    return builder(item, 1);
  }
}
