package mysticmods.roots.recipe.pyre;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.recipe.output.ConditionalOutput;
import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRecipes;
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
import java.util.function.Consumer;

public class PyreRecipe extends RootsTileRecipe<PyreInventory, PyreBlockEntity, PyreCrafting> {
  private Ritual ritual;
  // TODO: Move these conditions to RootsTileRecipe? or RootsTileConditionRecipe
  private List<LevelCondition> levelConditions = new ArrayList<>();
  private List<PlayerCondition> playerConditions = new ArrayList<>();

  public PyreRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }

  public Ritual getRitual() {
    return ritual;
  }

  public boolean hasOutput() {
    return (result != null && result.isEmpty()) || !conditionalOutputs.isEmpty();
  }

  public void setRitual(Ritual ritual) {
    this.ritual = ritual;
  }

  public void setLevelConditions(List<LevelCondition> levelConditions) {
    this.levelConditions = levelConditions;
  }

  public void setPlayerConditions(List<PlayerCondition> playerConditions) {
    this.playerConditions = playerConditions;
  }

  public List<LevelCondition> getLevelConditions() {
    return levelConditions;
  }

  public List<PlayerCondition> getPlayerConditions() {
    return playerConditions;
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
        Ritual ritual = Registries.RITUAL_REGISTRY.get().getValue(ritualName);
        if (ritual == null) {
          throw new JsonSyntaxException("Ritual '" + ritualName + "' does not exist!");
        }

        if (GsonHelper.isArrayNode(pJson, "level_conditions")) {
          for (JsonElement element : GsonHelper.getAsJsonArray(pJson, "level_conditions")) {
            ResourceLocation condName = new ResourceLocation(element.getAsString());
            LevelCondition condition = Registries.LEVEL_CONDITION_REGISTRY.get().getValue(condName);
            if (condition == null) {
              throw new JsonSyntaxException("Level condition '" + condName + "' does not exist!");
            }
            recipe.getLevelConditions().add(condition);
          }
        }
        if (GsonHelper.isArrayNode(pJson, "player_conditions")) {
          for (JsonElement element : GsonHelper.getAsJsonArray(pJson, "player_conditions")) {
            ResourceLocation condName = new ResourceLocation(element.getAsString());
            PlayerCondition condition = Registries.PLAYER_CONDITION_REGISTRY.get().getValue(condName);
            if (condition == null) {
              throw new JsonSyntaxException("Player condition '" + condName + "' does not exist!");
            }
            recipe.getPlayerConditions().add(condition);
          }
        }

        recipe.setRitual(ritual);
      }
    }

    @Override
    protected void fromNetworkAdditional(PyreRecipe recipe, ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
      super.fromNetworkAdditional(recipe, pRecipeId, pBuffer);
      if (pBuffer.readBoolean()) {
        Ritual ritual = Registries.RITUAL_REGISTRY.get().getValue(pBuffer.readVarInt());
        int levelConditionsSize = pBuffer.readVarInt();
        for (int i = 0; i < levelConditionsSize; i++) {
          LevelCondition condition = Registries.LEVEL_CONDITION_REGISTRY.get().getValue(pBuffer.readVarInt());
          recipe.getLevelConditions().add(condition);
        }
        int playerConditionsSize = pBuffer.readVarInt();
        for (int i = 0; i < playerConditionsSize; i++) {
          PlayerCondition condition = Registries.PLAYER_CONDITION_REGISTRY.get().getValue(pBuffer.readVarInt());
          recipe.getPlayerConditions().add(condition);
        }
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
      pBuffer.writeVarInt(recipe.getLevelConditions().size());
      for (LevelCondition condition : recipe.getLevelConditions()) {
        pBuffer.writeVarInt(Registries.LEVEL_CONDITION_REGISTRY.get().getID(condition));
      }
      pBuffer.writeVarInt(recipe.getPlayerConditions().size());
      for (PlayerCondition condition : recipe.getPlayerConditions()) {
        pBuffer.writeVarInt(Registries.PLAYER_CONDITION_REGISTRY.get().getID(condition));
      }
    }
  }

  public static class Builder extends RootsRecipe.Builder {
    private Ritual ritual;
    private final List<LevelCondition> levelConditions = new ArrayList<>();
    private final List<PlayerCondition> playerConditions = new ArrayList<>();

    protected Builder(ItemStack result) {
      super(result);
    }

    protected Builder(Ritual ritual) {
      super(null);
      this.ritual = ritual;
    }

    public Builder setRitual(Ritual ritual) {
      if (!this.conditionalOutputs.isEmpty() || (this.result != null && !this.result.isEmpty())) {
        throw new IllegalStateException("can't set a ritual for a recipe that has an output");
      }
      this.ritual = ritual;
      return this;
    }

    public Builder addLevelCondition(LevelCondition condition) {
      this.levelConditions.add(condition);
      return this;

    }

    public Builder addPlayerCondition(PlayerCondition condition) {
      this.playerConditions.add(condition);
      return this;
    }

    @Override
    public RootsRecipe.Builder setOutput(ItemStack output) {
      if (this.ritual != null) {
        throw new IllegalStateException("can't add outputs for a recipe that has an associated ritual");
      }
      return super.setOutput(output);
    }

    @Override
    public RootsRecipe.Builder addConditionalOutput(ConditionalOutput output) {
      if (this.ritual != null) {
        throw new IllegalStateException("can't add outputs for a recipe that has an associated ritual");
      }
      return super.addConditionalOutput(output);
    }

    @Override
    public RootsRecipe.Builder addConditionalOutputs(Collection<ConditionalOutput> output) {
      if (this.ritual != null) {
        throw new IllegalStateException("can't add outputs for a recipe that has an associated ritual");
      }
      return super.addConditionalOutputs(output);
    }

    @Override
    public RootsRecipe.Builder addConditionalOutput(ItemStack output, float chance) {
      if (this.ritual != null) {
        throw new IllegalStateException("can't add outputs for a recipe that has an associated ritual");
      }
      return super.addConditionalOutput(output, chance);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
      return ModRecipes.Serializers.PYRE.get();
    }

    @Override
    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation recipeName) {
      consumer.accept(new PyreRecipe.Builder.Result(recipeName, result, ingredients, conditionalOutputs, grants, getSerializer(), ritual, levelConditions, playerConditions));
    }


    public static class Result extends RootsRecipe.Builder.Result {
      private final Ritual ritual;
      private final List<LevelCondition> levelConditions;
      private final List<PlayerCondition> playerConditions;

      public Result(ResourceLocation id, ItemStack result, List<Ingredient> ingredients, List<ConditionalOutput> conditionalOutputs, List<Grant> grants, RecipeSerializer<?> serializer, Ritual ritual, List<LevelCondition> levelConditions, List<PlayerCondition> playerConditions) {
        super(id, result, ingredients, conditionalOutputs, grants, serializer);
        this.ritual = ritual;
        this.levelConditions = levelConditions;
        this.playerConditions = playerConditions;
      }

      @Override
      public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);
        if (ritual != null) {
          // TODO: 1.19 has no more getRegistryName
          json.addProperty("ritual", Registries.RITUAL_REGISTRY.get().getKey(ritual).toString());
        }
        if (!levelConditions.isEmpty()) {
          JsonArray levelConditionsArray = new JsonArray();
          for (LevelCondition condition : levelConditions) {
            levelConditionsArray.add(Registries.LEVEL_CONDITION_REGISTRY.get().getKey(condition).toString());
          }
          json.add("level_conditions", levelConditionsArray);
        }
        if (!playerConditions.isEmpty()) {
          JsonArray playerConditionsArray = new JsonArray();
          for (PlayerCondition condition : playerConditions) {
            playerConditionsArray.add(Registries.PLAYER_CONDITION_REGISTRY.get().getKey(condition).toString());
          }
          json.add("player_conditions", playerConditionsArray);
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
