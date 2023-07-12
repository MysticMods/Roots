package mysticmods.roots.recipe.mortar;

import com.google.gson.JsonObject;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.RootsRecipeBuilderBase;
import mysticmods.roots.api.recipe.RootsResultBase;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.api.recipe.output.ConditionalOutput;
import mysticmods.roots.blockentity.MortarBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static net.minecraft.data.recipes.RecipeBuilder.ROOT_RECIPE_ADVANCEMENT;

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

  public static class Builder extends RootsRecipeBuilderBase {
    protected final int times;

    protected Builder(int times) {
      super();
      this.times = times;
    }

    protected Builder(ItemStack result, int times) {
      super(result);
      this.times = times;
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation recipeName) {
      validate(recipeName);
      advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeName)).rewards(AdvancementRewards.Builder.recipe(recipeName)).requirements(RequirementsStrategy.OR);
      consumer.accept(new Result(recipeName, result, ingredients, conditionalOutputs, grants, levelConditions, playerConditions, getSerializer(), advancement, getAdvancementId(recipeName), times));
    }

    @Override
    protected boolean allowEmptyOutput() {
      return true;
    }

    @Override
    protected boolean requireIngredients() {
      return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
      return ModSerializers.MORTAR.get();
    }

    public static class Result extends RootsResultBase {
      private final int times;

      public Result(ResourceLocation id, ItemStack result, List<Ingredient> ingredients, List<ConditionalOutput> conditionalOutputs, List<Grant> grants, List<LevelCondition> levelConditions, List<PlayerCondition> playerConditions, RecipeSerializer<?> serializer, Advancement.Builder advancementBuilder, ResourceLocation advancementId, int times) {
        super(id, result, ingredients, conditionalOutputs, grants, levelConditions, playerConditions, serializer, advancementBuilder, advancementId);
        this.times = times;
      }

      @Override
      public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);
        json.addProperty("times", times);
      }
    }
  }

  public static class MultiBuilder extends Builder {
    protected final int count;

    protected MultiBuilder(ItemStack result, int times, int count) {
      super(result, times);
      if (count <= 1) {
        throw new IllegalArgumentException("Multi-recipe builder count must be greater than 1");
      }
      this.count = count;
    }

    @Override
    protected void validate(ResourceLocation recipeName) {
      if (ingredients.size() != 1) {
        throw new IllegalStateException("Multi-recipe '" + recipeName + "' must have exactly one ingredient");
      }
      if (!conditionalOutputs.isEmpty()) {
        throw new IllegalStateException("Multi-recipe '" + recipeName + "' can't have conditional outputs");
      }
      if (!grants.isEmpty()) {
        throw new IllegalStateException("Multi-recipe '" + recipeName + "' can't have grants");
      }
      super.validate(recipeName);
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation recipeName) {
      validate(recipeName);
      Ingredient ingredient = ingredients.get(0);
      int baseCount = result.getCount();
      for (int i = 1; i < count + 1; i++) {
        ResourceLocation thisRecipeName = new ResourceLocation(recipeName.getNamespace(), recipeName.getPath() + "_" + i);
        ItemStack thisResult = result.copy();
        thisResult.setCount(i * baseCount);
        List<Ingredient> thisIngredients = new ArrayList<>();
        for (int j = 0; j < i; j++) {
          thisIngredients.add(ingredient);
        }
        Advancement.Builder thisAdvancement = Advancement.Builder.advancement();
        for (Map.Entry<String, Criterion> entry : advancement.getCriteria().entrySet()) {
          thisAdvancement.addCriterion(entry.getKey(), entry.getValue());
        }
        thisAdvancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(thisRecipeName)).rewards(AdvancementRewards.Builder.recipe(thisRecipeName)).requirements(RequirementsStrategy.OR);
        consumer.accept(new MortarRecipe.Builder.Result(thisRecipeName, thisResult, thisIngredients, conditionalOutputs, grants, levelConditions, playerConditions, getSerializer(), thisAdvancement, getAdvancementId(thisRecipeName), times * i));
      }
    }
  }

  public static Builder builder(ItemLike item, int count, int times) {
    return new Builder(new ItemStack(item, count), times);
  }

  public static Builder builder(int times) {
    return new Builder(times);
  }

  public static MultiBuilder multiBuilder(ItemLike item, int count, int times) {
    return new MultiBuilder(new ItemStack(item), times, count);
  }

  public static MultiBuilder multiBuilder(ItemStack item, int times) {
    return new MultiBuilder(item, times, 5);
  }

  public static MultiBuilder multiBuilder (ItemStack item, int times, int counts) {
    return new MultiBuilder(item, times, counts);
  }

  public static MultiBuilder multiBuilder(ItemLike item, int times) {
    return multiBuilder(item, 5, times);
  }
}
