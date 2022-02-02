package mysticmods.roots.recipe.fey;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.block.entity.FeyCrafterBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import noobanidus.libs.noobutil.ingredient.IngredientStack;
import noobanidus.libs.noobutil.processor.IProcessor;
import noobanidus.libs.noobutil.processor.Processor;

import java.util.ArrayList;
import java.util.List;

public class FeyCraftingRecipe extends RootsTileRecipe<FeyCraftingInventory, FeyCrafterBlockEntity, FeyCrafting> {
  public FeyCraftingRecipe(NonNullList<Ingredient> ingredients, ItemStack result, ResourceLocation recipeId) {
    super(ingredients, result, recipeId);
  }

  @Override
  public boolean matches(FeyCrafting pInv, World pLevel) {
    return false;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return ModRecipes.Serializers.FEY_CRAFTING.get();
  }

  @Override
  public IRecipeType<?> getType() {
    return ModRecipes.Types.FEY_CRAFTING;
  }

  public static class Serializer extends RootsRecipe.Serializer<FeyCraftingInventory, FeyCrafting, FeyCraftingRecipe> {
    public Serializer() {
      super(FeyCraftingRecipe::new);
    }
  }

  public static class Builder extends RootsRecipe.Builder {
    protected Builder(IItemProvider item, int count) {
      super(item, count);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
      return ModRecipes.Serializers.FEY_CRAFTING.get();
    }
  }

  public static Builder builder (IItemProvider item, int count) {
    return new Builder(item, count);
  }
}
