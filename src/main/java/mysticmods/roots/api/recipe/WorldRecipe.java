package mysticmods.roots.api.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.crafting.IWorldCrafting;
import mysticmods.roots.api.recipe.output.ConditionalOutput;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.util.SetUtils;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

import static net.minecraft.data.recipes.RecipeBuilder.ROOT_RECIPE_ADVANCEMENT;

public abstract class WorldRecipe<W extends IWorldCrafting> extends RootsRecipeBase implements IWorldRecipe<W> {
  protected BlockState outputState;
  protected BlockPredicate matcher;

  public WorldRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }

  public void setOutputState (BlockState outputState) {
    this.outputState = outputState;
  }

  public BlockState getOutputState () {
    return this.outputState;
  }

  public BlockPredicate getMatcher() {
    return matcher;
  }

  public void setMatcher(BlockPredicate matcher) {
    this.matcher = matcher;
  }

  // TODO: Ensure that not copying this item won't cause problems
  @Override
  public ItemStack getResultItem() {
    if (result == null) {
      return ItemStack.EMPTY;
    }
    return result;
  }

  @Override
  public boolean matches(W pContainer, Level pLevel) {
    return false;
  }

  @Override
  public ResourceLocation getId() {
    return recipeId;
  }

  public abstract static class Serializer<W extends IWorldCrafting, R extends WorldRecipe<W>> implements RecipeSerializer<R> {

    private final WorldRecipe.WorldRecipeBuilder<R> builder;

    public Serializer(WorldRecipe.WorldRecipeBuilder<R> builder) {
      this.builder = builder;
    }

    protected void fromJsonAdditional(R recipe, ResourceLocation pRecipeId, JsonObject pJson) {
    }

    @Override
    public R fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
      R recipe = builder.create(pRecipeId);

      if (GsonHelper.isObjectNode(pJson, "result")) {
        ItemStack result = CraftingHelper.getItemStack(pJson.getAsJsonObject("result"), true, false);
        recipe.setResultItem(result);
      } else if (GsonHelper.isStringValue(pJson, "result")) {
        ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(pJson, "result"));
        Item item = ForgeRegistries.ITEMS.getValue(id);
        if (item == null) {
          throw new JsonSyntaxException("Unknown item '" + id + "'");
        }
        int count;
        if (!pJson.has("count")) {
          count = 1;
        } else {
          count = GsonHelper.getAsInt(pJson, "count");
        }
        recipe.setResultItem(new ItemStack(item, count));
      }

      if (GsonHelper.isArrayNode(pJson, "conditional_outputs")) {
        List<ConditionalOutput> outputs = new ArrayList<>();
        JsonArray conditionalOutputs = GsonHelper.getAsJsonArray(pJson, "conditional_outputs");
        for (int i = 0; i < conditionalOutputs.size(); i++) {
          if (conditionalOutputs.get(i).isJsonObject()) {
            outputs.add(ConditionalOutput.fromJson(conditionalOutputs.get(i)));
          } else {
            throw new JsonSyntaxException("Invalid conditional output: " + conditionalOutputs.get(i));
          }
        }
        recipe.addConditionalOutputs(outputs);
      }
      if (GsonHelper.isArrayNode(pJson, "grants")) {
        List<Grant> grants = new ArrayList<>();
        JsonArray thisGrants = GsonHelper.getAsJsonArray(pJson, "grants");
        for (int i = 0; i < thisGrants.size(); i++) {
          if (thisGrants.get(i).isJsonObject()) {
            grants.add(Grant.fromJson(thisGrants.get(i)));
          } else {
            throw new JsonSyntaxException("Invalid grant: " + thisGrants.get(i));
          }
        }
        recipe.addGrants(grants);
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

      fromJsonAdditional(recipe, pRecipeId, pJson);
      return recipe;
    }

    protected void fromNetworkAdditional(R recipe, ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
    }

    @Nullable
    @Override
    public R fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
      R recipe = builder.create(pRecipeId);

      if (pBuffer.readBoolean()) {
        ItemStack result = pBuffer.readItem();
        recipe.setResultItem(result);
      }

      int count = pBuffer.readVarInt();
      if (count > 0) {
        for (int i = 0; i < count; i++) {
          recipe.addConditionalOutput(ConditionalOutput.fromNetwork(pBuffer));
        }
      }
      count = pBuffer.readVarInt();
      if (count > 0) {
        for (int i = 0; i < count; i++) {
          recipe.addGrant(Grant.fromNetwork(pBuffer));
        }
      }
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


      fromNetworkAdditional(recipe, pRecipeId, pBuffer);
      return recipe;
    }

    protected void toNetworkAdditional(R recipe, FriendlyByteBuf pBuffer) {
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, R recipe) {
      pBuffer.writeVarInt(recipe.getIngredients().size());
      for (Ingredient ingredient : recipe.getIngredients()) {
        ingredient.toNetwork(pBuffer);
      }

      pBuffer.writeBoolean(recipe.getResultItem() != null);
      if (recipe.getResultItem() != null) {
        pBuffer.writeItem(recipe.getResultItem());
      }

      List<ConditionalOutput> conditionalOutputs = recipe.getConditionalOutputs();
      pBuffer.writeVarInt(conditionalOutputs.size());
      for (ConditionalOutput output : conditionalOutputs) {
        output.toNetwork(pBuffer);
      }

      List<Grant> grants = recipe.getGrants();
      pBuffer.writeVarInt(grants.size());
      for (Grant grant : grants) {
        grant.toNetwork(pBuffer);
      }

      pBuffer.writeVarInt(recipe.getLevelConditions().size());
      for (LevelCondition condition : recipe.getLevelConditions()) {
        pBuffer.writeVarInt(Registries.LEVEL_CONDITION_REGISTRY.get().getID(condition));
      }
      pBuffer.writeVarInt(recipe.getPlayerConditions().size());
      for (PlayerCondition condition : recipe.getPlayerConditions()) {
        pBuffer.writeVarInt(Registries.PLAYER_CONDITION_REGISTRY.get().getID(condition));
      }

      toNetworkAdditional(recipe, pBuffer);
    }
  }

  @Override
  public ItemStack assemble(W pInv) {
    Player player = pInv.getPlayer();
    if (player != null && player.level.isClientSide()) {
      for (Grant grant : getGrants()) {
        grant.accept((ServerPlayer) player);
      }
    }

    return getResultItem().copy();
  }

  public abstract static class Builder {
    protected final Advancement.Builder advancement = Advancement.Builder.advancement();
    protected ItemStack result;
    protected final List<ConditionalOutput> conditionalOutputs = new ArrayList<>();
    protected final List<Grant> grants = new ArrayList<>();
    protected final List<LevelCondition> levelConditions = new ArrayList<>();
    protected final List<PlayerCondition> playerConditions = new ArrayList<>();

    protected Builder() {
    }

    protected boolean allowEmptyOutput() {
      return true;
    }

    protected Builder(ItemStack result) {
      this.result = result;
    }

    public abstract RecipeSerializer<?> getSerializer();

    public Builder setOutput(ItemStack output) {
      this.result = output;
      return this;
    }

    public Builder addConditionalOutput(ConditionalOutput output) {
      this.conditionalOutputs.add(output);
      return this;
    }

    public Builder addConditionalOutputs(Collection<ConditionalOutput> output) {
      this.conditionalOutputs.addAll(output);
      return this;
    }

    public Builder addConditionalOutput(ItemStack output, float chance) {
      return addConditionalOutput(new ConditionalOutput(output, chance));
    }

    public Builder addGrant(Grant grant) {
      this.grants.add(grant);
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

    public Builder unlockedBy(String criterionName, CriterionTriggerInstance pCriterionTrigger) {
      this.advancement.addCriterion(criterionName, pCriterionTrigger);
      return this;
    }

    protected boolean hasOutput() {
      return (result != null && !result.isEmpty()) || !conditionalOutputs.isEmpty();
    }

    protected void validate(ResourceLocation recipeName) {
      if (!hasOutput() && !allowEmptyOutput() && grants.isEmpty()) {
        throw new IllegalStateException("No output, conditional output or grants defined for recipe " + recipeName);
      }
      if (advancement.getCriteria().isEmpty()) {
        throw new IllegalStateException("No way of obtaining recipe " + recipeName);
      }
    }

    protected String getFolderName(ResourceLocation recipeName) {
      if (result != null) {
        return result.getItem().getItemCategory().getRecipeFolderName();
      } else if (!conditionalOutputs.isEmpty()) {
        return conditionalOutputs.get(0).getOutput().getItem().getItemCategory().getRecipeFolderName();
      } else if (!grants.isEmpty()) {
        return grants.get(0).getId().getNamespace();
      } else {
        return recipeName.getNamespace();
      }
    }

    protected ResourceLocation getAdvancementId(ResourceLocation recipeName) {
      return new ResourceLocation(recipeName.getNamespace(), "recipes/" + getFolderName(recipeName) + "/" + recipeName.getPath());
    }


    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation recipeName) {
      validate(recipeName);
      advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeName)).rewards(AdvancementRewards.Builder.recipe(recipeName)).requirements(RequirementsStrategy.OR);
      consumer.accept(new WorldRecipe.Builder.Result(recipeName, result, conditionalOutputs, grants, levelConditions, playerConditions, getSerializer(), advancement, getAdvancementId(recipeName)));
    }

    public static class Result implements FinishedRecipe {
      private final ResourceLocation id;
      private final ItemStack result;
      private final RecipeSerializer<?> serializer;
      private final List<ConditionalOutput> conditionalOutputs;
      private final List<Grant> grants;
      private final List<LevelCondition> levelConditions;
      private final List<PlayerCondition> playerConditions;
      private final Advancement.Builder advancementBuilder;
      private final ResourceLocation advancementId;

      public Result(ResourceLocation id, ItemStack result, List<ConditionalOutput> conditionalOutputs, List<Grant> grants, List<LevelCondition> levelConditions, List<PlayerCondition> playerConditions, RecipeSerializer<?> serializer, Advancement.Builder advancementBuilder, ResourceLocation advancementId) {
        this.id = id;
        this.result = result;
        this.serializer = serializer;
        this.conditionalOutputs = conditionalOutputs;
        this.grants = grants;
        this.levelConditions = levelConditions;
        this.playerConditions = playerConditions;
        this.advancementBuilder = advancementBuilder;
        this.advancementId = advancementId;
      }

      @Override
      public void serializeRecipeData(JsonObject json) {
        if (result != null) {
          JsonObject item = new JsonObject();
          item.addProperty("item", ForgeRegistries.ITEMS.getKey(result.getItem()).toString());
          if (result.getCount() > 1) {
            item.addProperty("count", result.getCount());
          }
          if (result.hasTag()) {
            CompoundTag tag = result.getTag();
            if (tag != null) {
              item.addProperty("nbt", tag.toString());
            }
          }
          json.add("result", item);
        }

        if (!conditionalOutputs.isEmpty()) {
          JsonArray outputs = new JsonArray();
          for (ConditionalOutput output : conditionalOutputs) {
            outputs.add(output.toJson());
          }
          json.add("conditional_outputs", outputs);
        }
        if (!grants.isEmpty()) {
          JsonArray grants = new JsonArray();
          for (Grant grant : this.grants) {
            grants.add(grant.toJson());
          }
          json.add("grants", grants);
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

      @Override
      public ResourceLocation getId() {
        return id;
      }

      @Override
      public RecipeSerializer<?> getType() {
        return serializer;
      }

      @Nullable
      @Override
      public JsonObject serializeAdvancement() {
        return advancementBuilder == null ? null : advancementBuilder.serializeToJson();
      }

      @Nullable
      @Override
      public ResourceLocation getAdvancementId() {
        return advancementId;
      }
    }
  }

  @FunctionalInterface
  public interface WorldRecipeBuilder<R extends WorldRecipe<?>> {
    R create(ResourceLocation recipeId);
  }
}
