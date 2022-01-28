package mysticmods.roots.recipe.pyre.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.block.entity.PyreBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModRegistries;
import mysticmods.roots.recipe.pyre.PyreInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import noobanidus.libs.noobutil.ingredient.IngredientStack;
import noobanidus.libs.noobutil.processor.IProcessor;
import noobanidus.libs.noobutil.processor.Processor;

import java.util.ArrayList;
import java.util.List;

public class RitualCraftingRecipe extends RootsRecipe<PyreInventory, PyreBlockEntity, RitualCrafting> {
  public RitualCraftingRecipe(NonNullList<IngredientStack> ingredients, ItemStack result, ResourceLocation recipeId) {
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

  public static class Serializer extends RootsRecipe.Serializer<PyreInventory, PyreBlockEntity, RitualCrafting, RitualCraftingRecipe> {
    public Serializer() {
      super(RitualCraftingRecipe::new);
    }

    @Override
    public List<Processor<RitualCrafting>> parseProcessors(JsonArray processors) {
      List<Processor<RitualCrafting>> processorsResult = new ArrayList<>();
      for (JsonElement element : processors) {
        ResourceLocation procRl = new ResourceLocation(element.getAsString());
        processorsResult.add(getProcessor(procRl));
      }

      return processorsResult;
    }

    @Override
    public Processor<RitualCrafting> getProcessor(ResourceLocation rl) {
      IProcessor<?> proc = ModRegistries.PROCESSOR_REGISTRY.getValue(rl);
      try {
//noinspection unchecked
        return (Processor<RitualCrafting>) proc;
      } catch (ClassCastException e) {
        throw new JsonSyntaxException("Invalid processor type: " + rl);
      }
    }
  }
}
