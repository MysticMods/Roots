package mysticmods.roots.api.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.crafting.IRootsCrafting;
import mysticmods.roots.api.recipe.output.ConditionalOutput;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

import static net.minecraft.data.recipes.RecipeBuilder.ROOT_RECIPE_ADVANCEMENT;

public abstract class RootsRecipe<H extends IItemHandler, W extends IRootsCrafting<H>> extends RootsRecipeBase implements IRootsRecipe<H, W>, IRootsRecipeBase {
  protected final NonNullList<Ingredient> ingredients = NonNullList.create();

  public RootsRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }

  @Override
  public void setIngredients(NonNullList<Ingredient> ingredients) {
    this.ingredients.clear();
    this.ingredients.addAll(ingredients);
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
    List<ItemStack> inputs = new ArrayList<>();
    H inv = pContainer.getHandler();
    for (int i = 0; i < inv.getSlots(); i++) {
      ItemStack stack = inv.getStackInSlot(i);
      if (!stack.isEmpty()) {
        inputs.add(stack);
      }
    }

    return RecipeMatcher.findMatches(inputs, ingredients) != null;
  }

  @Override
  public ResourceLocation getId() {
    return recipeId;
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

  // TODO: Abstract more of this out

  public abstract static class Serializer<H extends IItemHandler, W extends IRootsCrafting<H>, R extends RootsRecipe<H, W>> implements RecipeSerializer<R> {

    private final RootsRecipeBuilder<R> builder;

    public Serializer(RootsRecipeBuilder<R> builder) {
      this.builder = builder;
    }

    protected void fromJsonAdditional(R recipe, ResourceLocation pRecipeId, JsonObject pJson) {
    }

    @Override
    public R fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
      R recipe = builder.create(pRecipeId);

      if (GsonHelper.isArrayNode(pJson, "ingredients")) {
        JsonArray incoming = GsonHelper.getAsJsonArray(pJson, "ingredients");
        NonNullList<Ingredient> ingredients = NonNullList.create();
        for (int i = 0; i < incoming.size(); i++) {
          Ingredient ingredient = Ingredient.fromJson(incoming.get(i));
          if (!ingredient.isEmpty()) {
            ingredients.add(ingredient);
          }
        }
        recipe.setIngredients(ingredients);
      }

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

      int ingCount = pBuffer.readVarInt();
      if (ingCount > 0) {
        NonNullList<Ingredient> ingredients = NonNullList.withSize(ingCount, Ingredient.EMPTY);
        for (int i = 0; i < ingCount; i++) {
          ingredients.set(i, Ingredient.fromNetwork(pBuffer));
        }
        recipe.setIngredients(ingredients);
      }

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

  @FunctionalInterface
  public interface RootsRecipeBuilder<R extends RootsRecipe<?, ?>> {
    R create(ResourceLocation recipeId);
  }
}
