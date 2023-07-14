package mysticmods.roots.recipe.pyre;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.RootsRecipeBuilderBase;
import mysticmods.roots.api.recipe.RootsResultBase;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
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
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static net.minecraft.data.recipes.RecipeBuilder.ROOT_RECIPE_ADVANCEMENT;

public class PyreRecipe extends RootsTileRecipe<PyreInventory, PyreBlockEntity, PyreCrafting> {
  private Ritual ritual;

  public PyreRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }

  public Ritual getRitual() {
    return ritual;
  }

  public boolean hasOutput() {
    return (result != null && result.isEmpty()) || !chanceOutputs.isEmpty();
  }

  public void setRitual(Ritual ritual) {
    this.ritual = ritual;
  }


  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.PYRE.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.PYRE.get();
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
        Ritual ritual = Registries.RITUAL_REGISTRY.get().getValue(ritualName);
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
        Ritual ritual = Registries.RITUAL_REGISTRY.get().getValue(pBuffer.readVarInt());
        recipe.setRitual(ritual);
      }
    }

    @Override
    protected void toNetworkAdditional(PyreRecipe recipe, FriendlyByteBuf pBuffer) {
      super.toNetworkAdditional(recipe, pBuffer);
      pBuffer.writeBoolean(recipe.getRitual() != null);
      if (recipe.getRitual() != null) {
        pBuffer.writeVarInt(Registries.RITUAL_REGISTRY.get().getID(recipe.getRitual()));
      }
    }
  }

  public static class Builder extends RootsRecipeBuilderBase {
    protected Ritual ritual;

    protected Builder(ItemStack result) {
      super(result);
    }

    protected Builder(Ritual ritual) {
      super(null);
      this.ritual = ritual;
    }

    public Builder setRitual(Ritual ritual) {
      if (!this.chanceOutputs.isEmpty() || (this.result != null && !this.result.isEmpty())) {
        throw new IllegalStateException("can't set a ritual for a recipe that has an output");
      }
      this.ritual = ritual;
      return this;
    }


    @Override
    public Builder setOutput(ItemStack output) {
      if (this.ritual != null) {
        throw new IllegalStateException("can't add outputs for a recipe that has an associated ritual");
      }
      return (Builder) super.setOutput(output);
    }

    @Override
    public Builder addChanceOutput(ChanceOutput output) {
      if (this.ritual != null) {
        throw new IllegalStateException("can't add outputs for a recipe that has an associated ritual");
      }
      return (Builder) super.addChanceOutput(output);
    }

    @Override
    public Builder addChanceOutputs(Collection<ChanceOutput> output) {
      if (this.ritual != null) {
        throw new IllegalStateException("can't add outputs for a recipe that has an associated ritual");
      }
      return (Builder) super.addChanceOutputs(output);
    }

    @Override
    public Builder addChanceOutput(ItemStack output, float chance) {
      if (this.ritual != null) {
        throw new IllegalStateException("can't add outputs for a recipe that has an associated ritual");
      }
      return (Builder) super.addChanceOutput(output, chance);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
      return ModSerializers.PYRE.get();
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
    protected void validate(ResourceLocation recipeName) {
      super.validate(recipeName);
      if (!hasOutput() && ritual == null) {
        throw new IllegalStateException("Recipe '" + recipeName + "' must have either an output or a ritual");
      }
    }

    @Override
    protected String getFolderName(ResourceLocation recipeName) {
      if (ritual != null) {
        return Registries.RITUAL_REGISTRY.get().getKey(ritual).getNamespace();
      }
      return super.getFolderName(recipeName);
    }

    @Override
    public void doSave(Consumer<FinishedRecipe> consumer, ResourceLocation recipeName) {
      consumer.accept(new PyreRecipe.Builder.Result(recipeName, result, ingredients, chanceOutputs, grants, levelConditions, playerConditions, getSerializer(), advancement, getAdvancementId(recipeName), ritual));
    }


    public static class Result extends RootsResultBase {
      private final Ritual ritual;

      public Result(ResourceLocation id, ItemStack result, List<Ingredient> ingredients, List<ChanceOutput> chanceOutputs, List<Grant> grants, List<LevelCondition> levelConditions, List<PlayerCondition> playerConditions, RecipeSerializer<?> serializer, Advancement.Builder builder, ResourceLocation advancementId, Ritual ritual) {
        super(id, result, ingredients, chanceOutputs, grants, levelConditions, playerConditions, serializer, builder, advancementId);
        this.ritual = ritual;
      }

      @Override
      public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);
        if (ritual != null) {
          json.addProperty("ritual", Registries.RITUAL_REGISTRY.get().getKey(ritual).toString());
        }
      }
    }
  }

  public static class MultiBuilder extends Builder {
    protected final int count;

    protected MultiBuilder(ItemStack result, int count) {
      super(result);
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
      if (ritual != null) {
        throw new IllegalStateException("Multi-recipe '" + recipeName + "' can't have an associated ritual");
      }
      if (!chanceOutputs.isEmpty()) {
        throw new IllegalStateException("Multi-recipe '" + recipeName + "' can't have chance outputs");
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
        consumer.accept(new PyreRecipe.Builder.Result(thisRecipeName, thisResult, thisIngredients, chanceOutputs, grants, levelConditions, playerConditions, getSerializer(), thisAdvancement, getAdvancementId(thisRecipeName), ritual));
      }
    }
  }

  public static Builder builder(ItemLike item, int count) {
    return new Builder(new ItemStack(item, count));
  }

  public static Builder builder(Ritual ritual) {
    return new Builder(ritual);
  }

  public static Builder builder(ItemLike item) {
    return builder(item, 1);
  }

  public static MultiBuilder multiBuilder(ItemLike item, int count) {
    return new MultiBuilder(new ItemStack(item), count);
  }

  public static MultiBuilder multiBuilder(ItemStack item) {
    return new MultiBuilder(item, 5);
  }

  public static MultiBuilder multiBuilder(ItemStack item, int counts) {
    return new MultiBuilder(item, counts);
  }
}
