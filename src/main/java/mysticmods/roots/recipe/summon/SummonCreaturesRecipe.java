package mysticmods.roots.recipe.summon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.block.entity.PyreBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModRegistries;
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

public class SummonCreaturesRecipe extends RootsRecipe<SummonCreaturesInventory, PyreBlockEntity, SummonCreaturesCrafting> {
  public SummonCreaturesRecipe(NonNullList<IngredientStack> ingredients, ItemStack result, ResourceLocation recipeId) {
    super(ingredients, result, recipeId);
  }

  @Override
  public boolean matches(SummonCreaturesCrafting pInv, World pLevel) {
    return false;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return ModRecipes.Serializers.SUMMON_CREATURES.get();
  }

  @Override
  public IRecipeType<?> getType() {
    return ModRecipes.Types.SUMMON_CREATURES;
  }

  public static class Serializer extends RootsRecipe.Serializer<SummonCreaturesInventory, PyreBlockEntity, SummonCreaturesCrafting, SummonCreaturesRecipe> {
    public Serializer() {
      super(SummonCreaturesRecipe::new);
    }

    @Override
    public List<Processor<SummonCreaturesCrafting>> parseProcessors(JsonArray processors) {
      List<Processor<SummonCreaturesCrafting>> processorsResult = new ArrayList<>();
      for (JsonElement element : processors) {
        ResourceLocation procRl = new ResourceLocation(element.getAsString());
        processorsResult.add(getProcessor(procRl));
      }

      return processorsResult;
    }

    @Override
    public Processor<SummonCreaturesCrafting> getProcessor(ResourceLocation rl) {
      IProcessor<?> proc = ModRegistries.PROCESSOR_REGISTRY.getValue(rl);
      try {
//noinspection unchecked
        return (Processor<SummonCreaturesCrafting>) proc;
      } catch (ClassCastException e) {
        throw new JsonSyntaxException("Invalid processor type: " + rl);
      }
    }
  }

}
