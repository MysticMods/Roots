package mysticmods.roots.recipe.pyre;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.recipe.ConditionalOutput;
import mysticmods.roots.api.recipe.Grant;
import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.Consumer;

// TODO: Validate that ritual is exclusive with output, conditional output
public class PyreRecipe extends RootsTileRecipe<PyreInventory, PyreBlockEntity, PyreCrafting> {
  private Ritual ritual;

  public PyreRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }

  public Ritual getRitual() {
    return ritual;
  }

  public boolean hasOutput () {
    return (result != null && result.isEmpty()) || !conditionalOutputs.isEmpty();
  }

  public void setRitual(Ritual ritual) {
    this.ritual = ritual;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModRecipes.Serializers.PYRE.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.Types.PYRE.get();
  }

  public static class Serializer extends RootsRecipe.Serializer<PyreInventory, PyreCrafting, PyreRecipe> {
    public Serializer() {
      super(PyreRecipe::new);
    }

    @Override
    protected void fromJsonAdditional(PyreRecipe recipe, ResourceLocation pRecipeId, JsonObject pJson) {
      super.fromJsonAdditional(recipe, pRecipeId, pJson);
      if (GsonHelper.isStringValue(pJson, "ritual")) {
        if (recipe.hasOutput()) {
          throw new JsonSyntaxException("Recipe '" + pRecipeId + "' cannot have both a ritual and an output");
        }
        ResourceLocation ritualName = new ResourceLocation(GsonHelper.getAsString(pJson, "ritual"));
        Ritual ritual = ModRegistries.RITUAL_REGISTRY.get().getValue(ritualName);
        if (ritual == null) {
          throw new JsonSyntaxException("Ritual '" + ritualName + "' does not exist!");
        }
        recipe.setRitual(ritual);
      }
    }

    @Override
    protected void fromNetworkAdditional(PyreRecipe recipe, ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
      super.fromNetworkAdditional(recipe, pRecipeId, pBuffer);
      if (pBuffer.readBoolean()) {
        ResourceLocation ritualName = new ResourceLocation(pBuffer.readUtf());
        Ritual ritual = ModRegistries.RITUAL_REGISTRY.get().getValue(ritualName);
        if (ritual == null) {
          // TODO: something here?
        }
        recipe.setRitual(ritual);
      }
    }

    @Override
    protected void toNetworkAdditional(PyreRecipe recipe, FriendlyByteBuf pBuffer) {
      super.toNetworkAdditional(recipe, pBuffer);
      pBuffer.writeBoolean(recipe.getRitual() != null);
      if (recipe.getRitual() != null) {
        // TODO: getRegistryName is gone in 1.19
        pBuffer.writeUtf(recipe.getRitual().getRegistryName().toString());
      }
    }
  }

  public static class Builder extends RootsRecipe.Builder {
    private Ritual ritual;

    protected Builder(ItemStack result) {
      super(result);
    }

    protected Builder(Ritual ritual) {
      super(null);
      this.ritual = ritual;
    }

    public void setRitual(Ritual ritual) {
      if (!this.conditionalOutputs.isEmpty() || (this.result != null && !this.result.isEmpty())) {
        throw new IllegalStateException("can't set a ritual for a recipe that has an output");
      }
      this.ritual = ritual;
    }



    @Override
    public RecipeSerializer<?> getSerializer() {
      return ModRecipes.Serializers.PYRE.get();
    }

    @Override
    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation recipeName) {
      consumer.accept(new PyreRecipe.Builder.Result(recipeName, result, ingredients, conditionalOutputs, grants, getSerializer(), ritual));
    }


    public static class Result extends RootsRecipe.Builder.Result {
      private final Ritual ritual;

      public Result(ResourceLocation id, ItemStack result, List<Ingredient> ingredients, List<ConditionalOutput> conditionalOutputs, List<Grant> grants, RecipeSerializer<?> serializer, Ritual ritual) {
        super(id, result, ingredients, conditionalOutputs, grants, serializer);
        this.ritual = ritual;
      }

      @Override
      public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);
        if (ritual != null) {
          // TODO: 1.19 has no more getRegistryName
          json.addProperty("ritual", ritual.getRegistryName().toString());
        }
      }
    }
  }

  public static Builder builder(ItemLike item, int count) {
    return new Builder(new ItemStack(item, count));
  }

  public static Builder builder(Ritual ritual) {
    return new Builder(ritual);
  }
}
