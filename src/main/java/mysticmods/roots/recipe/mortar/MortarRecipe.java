package mysticmods.roots.recipe.mortar;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.recipe.IBoundlessRecipe;
import mysticmods.roots.api.recipe.IIngredientStackRecipe;
import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.block.entity.FeyCrafterBlockEntity;
import mysticmods.roots.block.entity.MortarBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModRegistries;
import mysticmods.roots.recipe.fey.FeyCrafting;
import mysticmods.roots.recipe.fey.FeyCraftingInventory;
import mysticmods.roots.recipe.fey.FeyCraftingRecipe;
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

public class MortarRecipe extends RootsRecipe<MortarInventory, MortarBlockEntity, MortarCrafting> {
  public MortarRecipe(NonNullList<IngredientStack> ingredients, ItemStack result, ResourceLocation recipeId) {
    super(ingredients, result, recipeId);
  }

  @Override
  public boolean matches(MortarCrafting pInv, World pLevel) {
    return false;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return ModRecipes.Serializers.MORTAR.get();
  }

  @Override
  public IRecipeType<?> getType() {
    return ModRecipes.Types.MORTAR;
  }

  public static class Serializer extends RootsRecipe.Serializer<MortarInventory, MortarBlockEntity, MortarCrafting, MortarRecipe> {
    public Serializer() {
      super(MortarRecipe::new);
    }

    @Override
    public List<Processor<MortarCrafting>> parseProcessors(JsonArray processors) {
      List<Processor<MortarCrafting>> processorsResult = new ArrayList<>();
      for (JsonElement element : processors) {
        ResourceLocation procRl = new ResourceLocation(element.getAsString());
        processorsResult.add(getProcessor(procRl));
      }

      return processorsResult;
    }

    @Override
    public Processor<MortarCrafting> getProcessor(ResourceLocation rl) {
      IProcessor<?> proc = ModRegistries.PROCESSOR_REGISTRY.getValue(rl);
      try {
//noinspection unchecked
        return (Processor<MortarCrafting>) proc;
      } catch (ClassCastException e) {
        throw new JsonSyntaxException("Invalid processor type: " + rl);
      }
    }
  }

  public static class Builder extends RootsRecipe.Builder<MortarInventory, MortarBlockEntity, MortarCrafting> {
    protected Builder(IItemProvider item, int count) {
      super(item, count);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
      return ModRecipes.Serializers.MORTAR.get();
    }
  }
}
