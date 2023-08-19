package mysticmods.roots.api.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class RootsResultBase implements FinishedRecipe {
  protected final ResourceLocation id;
  protected final ItemStack result;
  protected final List<Ingredient> ingredients;
  protected final RecipeSerializer<?> serializer;
  protected final List<ChanceOutput> chanceOutputs;
  protected final List<Grant> grants;
  protected final List<LevelCondition> levelConditions;
  protected final List<PlayerCondition> playerConditions;
  protected final Advancement.Builder advancementBuilder;
  protected final ResourceLocation advancementId;

  public RootsResultBase(ResourceLocation id, ItemStack result, List<Ingredient> ingredients, List<ChanceOutput> chanceOutputs, List<Grant> grants, List<LevelCondition> levelConditions, List<PlayerCondition> playerConditions, RecipeSerializer<?> serializer, Advancement.Builder advancementBuilder, ResourceLocation advancementId) {
    this.id = id;
    this.result = result;
    this.ingredients = ingredients;
    this.serializer = serializer;
    this.chanceOutputs = chanceOutputs;
    this.grants = grants;
    this.levelConditions = levelConditions;
    this.playerConditions = playerConditions;
    this.advancementBuilder = advancementBuilder;
    this.advancementId = advancementId;
  }

  @Override
  public void serializeRecipeData(JsonObject json) {
    if (!this.ingredients.isEmpty()) {
      JsonArray array = new JsonArray();

      for (Ingredient ingredient : this.ingredients) {
        array.add(ingredient.toJson());
      }

      json.add("ingredients", array);
    }
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

    if (!chanceOutputs.isEmpty()) {
      JsonArray outputs = new JsonArray();
      for (ChanceOutput output : chanceOutputs) {
        outputs.add(output.toJson());
      }
      json.add("chance_outputs", outputs);
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
    return null;
    //return advancementBuilder == null ? null : advancementBuilder.serializeToJson();
  }

  @Nullable
  @Override
  public ResourceLocation getAdvancementId() {
    return advancementId;
  }
}
