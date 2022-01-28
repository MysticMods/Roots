package mysticmods.roots.recipe.fey;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.block.entity.FeyCrafterBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import noobanidus.libs.noobutil.ingredient.IngredientStack;
import noobanidus.libs.noobutil.processor.IProcessor;
import noobanidus.libs.noobutil.processor.Processor;

import java.util.ArrayList;
import java.util.List;

public class FeyCraftingRecipe extends RootsRecipe<FeyCraftingInventory, FeyCrafterBlockEntity, FeyCrafting> {
  public FeyCraftingRecipe(NonNullList<IngredientStack> ingredients, ItemStack result, ResourceLocation recipeId) {
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

  public static class Serializer extends RootsRecipe.Serializer<FeyCraftingInventory, FeyCrafterBlockEntity, FeyCrafting, FeyCraftingRecipe> {
    public Serializer() {
      super(FeyCraftingRecipe::new);
    }

    @Override
    public List<Processor<FeyCrafting>> parseProcessors(JsonArray processors) {
      List<Processor<FeyCrafting>> processorsResult = new ArrayList<>();
      for (JsonElement element : processors) {
        ResourceLocation procRl = new ResourceLocation(element.getAsString());
        processorsResult.add(getProcessor(procRl));
      }

      return processorsResult;
    }

    @Override
    public Processor<FeyCrafting> getProcessor(ResourceLocation rl) {
      IProcessor<?> proc = ModRegistries.PROCESSOR_REGISTRY.getValue(rl);
      try {
//noinspection unchecked
        return (Processor<FeyCrafting>) proc;
      } catch (ClassCastException e) {
        throw new JsonSyntaxException("Invalid processor type: " + rl);
      }
    }
  }

  public static class Builder extends RootsRecipe.Builder<FeyCraftingInventory, FeyCrafterBlockEntity, FeyCrafting> {
    protected Builder(IItemProvider item, int count) {
      super(item, count);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
      return ModRecipes.Serializers.FEY_CRAFTING.get();
    }
  }
}
