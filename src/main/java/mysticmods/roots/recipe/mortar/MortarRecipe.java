package mysticmods.roots.recipe.mortar;

import com.google.gson.JsonObject;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.api.recipe.output.ConditionalOutput;
import mysticmods.roots.blockentity.MortarBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.Consumer;

// TODO: Mixed Mortar Recipe?
public class MortarRecipe extends RootsTileRecipe<MortarInventory, MortarBlockEntity, MortarCrafting> {
  private int times;

  public MortarRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }

  public int getTimes() {
    return times;
  }

  public void setTimes(int times) {
    this.times = times;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.MORTAR.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.MORTAR.get();
  }

  public static class Serializer extends RootsRecipe.Serializer<MortarInventory, MortarCrafting, MortarRecipe> {
    public Serializer() {
      super(MortarRecipe::new);
    }

    @Override
    protected void fromJsonAdditional(MortarRecipe recipe, ResourceLocation pRecipeId, JsonObject pJson) {
      super.fromJsonAdditional(recipe, pRecipeId, pJson);
      recipe.setTimes(pJson.get("times").getAsInt());

    }

    @Override
    protected void fromNetworkAdditional(MortarRecipe recipe, ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
      super.fromNetworkAdditional(recipe, pRecipeId, pBuffer);
      recipe.setTimes(pBuffer.readVarInt());

    }

    @Override
    protected void toNetworkAdditional(MortarRecipe recipe, FriendlyByteBuf pBuffer) {
      super.toNetworkAdditional(recipe, pBuffer);
      pBuffer.writeVarInt(recipe.getTimes());
    }
  }

  public static class Builder extends RootsRecipe.Builder {
    private final int times;

    protected Builder(int times) {
      super();
      this.times = times;
    }

    protected Builder(ItemStack result, int times) {
      super(result);
      this.times = times;
    }

    @Override
    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation recipeName) {
      consumer.accept(new Result(recipeName, result, ingredients, conditionalOutputs, grants, levelConditions, playerConditions, getSerializer(), times));
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
      return ModSerializers.MORTAR.get();
    }

    public static class Result extends RootsRecipe.Builder.Result {
      private final int times;

      public Result(ResourceLocation id, ItemStack result, List<Ingredient> ingredients, List<ConditionalOutput> conditionalOutputs, List<Grant> grants, List<LevelCondition> levelConditions, List<PlayerCondition> playerConditions, RecipeSerializer<?> serializer, int times) {
        super(id, result, ingredients, conditionalOutputs, grants, levelConditions, playerConditions, serializer);
        this.times = times;
      }

      @Override
      public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);
        json.addProperty("times", times);
      }
    }
  }

  public static Builder builder(ItemLike item, int count, int times) {
    return new Builder(new ItemStack(item, count), times);
  }

  public static Builder builder(int times) {
    return new Builder(times);
  }
}
