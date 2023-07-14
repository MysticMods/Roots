package mysticmods.roots.api.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public abstract class RootsSerializerBase {
  public void baseFromJson(IRootsRecipeBase recipeBase, ResourceLocation pRecipeId, JsonObject pJson) {
    if (GsonHelper.isArrayNode(pJson, "ingredients")) {
      JsonArray incoming = GsonHelper.getAsJsonArray(pJson, "ingredients");
      NonNullList<Ingredient> ingredients = NonNullList.create();
      for (int i = 0; i < incoming.size(); i++) {
        Ingredient ingredient = Ingredient.fromJson(incoming.get(i));
        if (!ingredient.isEmpty()) {
          ingredients.add(ingredient);
        }
      }
      recipeBase.setIngredients(ingredients);
    }

    if (GsonHelper.isObjectNode(pJson, "result")) {
      ItemStack result = CraftingHelper.getItemStack(pJson.getAsJsonObject("result"), true, false);
      recipeBase.setResultItem(result);
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
      recipeBase.setResultItem(new ItemStack(item, count));
    }

    if (GsonHelper.isArrayNode(pJson, "chance_outputs")) {
      List<ChanceOutput> outputs = new ArrayList<>();
      JsonArray chanceOutputs = GsonHelper.getAsJsonArray(pJson, "chance_outputs");
      for (int i = 0; i < chanceOutputs.size(); i++) {
        if (chanceOutputs.get(i).isJsonObject()) {
          outputs.add(ChanceOutput.fromJson(chanceOutputs.get(i)));
        } else {
          throw new JsonSyntaxException("Invalid chance output: " + chanceOutputs.get(i));
        }
      }
      recipeBase.addChanceOutputs(outputs);
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
      recipeBase.addGrants(grants);
    }
    if (GsonHelper.isArrayNode(pJson, "level_conditions")) {
      for (JsonElement element : GsonHelper.getAsJsonArray(pJson, "level_conditions")) {
        ResourceLocation condName = new ResourceLocation(element.getAsString());
        LevelCondition condition = Registries.LEVEL_CONDITION_REGISTRY.get().getValue(condName);
        if (condition == null) {
          throw new JsonSyntaxException("Level condition '" + condName + "' does not exist!");
        }
        recipeBase.getLevelConditions().add(condition);
      }
    }
    if (GsonHelper.isArrayNode(pJson, "player_conditions")) {
      for (JsonElement element : GsonHelper.getAsJsonArray(pJson, "player_conditions")) {
        ResourceLocation condName = new ResourceLocation(element.getAsString());
        PlayerCondition condition = Registries.PLAYER_CONDITION_REGISTRY.get().getValue(condName);
        if (condition == null) {
          throw new JsonSyntaxException("Player condition '" + condName + "' does not exist!");
        }
        recipeBase.getPlayerConditions().add(condition);
      }
    }
  }

  public void baseFromNetwork(IRootsRecipeBase recipeBase, ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
    int ingCount = pBuffer.readVarInt();
    if (ingCount > 0) {
      NonNullList<Ingredient> ingredients = NonNullList.withSize(ingCount, Ingredient.EMPTY);
      for (int i = 0; i < ingCount; i++) {
        ingredients.set(i, Ingredient.fromNetwork(pBuffer));
      }
      recipeBase.setIngredients(ingredients);
    }

    if (pBuffer.readBoolean()) {
      ItemStack result = pBuffer.readItem();
      recipeBase.setResultItem(result);
    }

    int count = pBuffer.readVarInt();
    if (count > 0) {
      for (int i = 0; i < count; i++) {
        recipeBase.addChanceOutput(ChanceOutput.fromNetwork(pBuffer));
      }
    }
    count = pBuffer.readVarInt();
    if (count > 0) {
      for (int i = 0; i < count; i++) {
        recipeBase.addGrant(Grant.fromNetwork(pBuffer));
      }
    }
    int levelConditionsSize = pBuffer.readVarInt();
    for (int i = 0; i < levelConditionsSize; i++) {
      LevelCondition condition = Registries.LEVEL_CONDITION_REGISTRY.get().getValue(pBuffer.readVarInt());
      recipeBase.getLevelConditions().add(condition);
    }
    int playerConditionsSize = pBuffer.readVarInt();
    for (int i = 0; i < playerConditionsSize; i++) {
      PlayerCondition condition = Registries.PLAYER_CONDITION_REGISTRY.get().getValue(pBuffer.readVarInt());
      recipeBase.getPlayerConditions().add(condition);
    }
  }

  public void baseToNetwork(FriendlyByteBuf pBuffer, IRootsRecipeBase recipe) {
    pBuffer.writeVarInt(recipe.getBaseIngredients().size());
    for (Ingredient ingredient : recipe.getBaseIngredients()) {
      ingredient.toNetwork(pBuffer);
    }

    pBuffer.writeBoolean(recipe.getBaseResultItem() != null);
    if (recipe.getBaseResultItem() != null) {
      pBuffer.writeItem(recipe.getBaseResultItem());
    }

    List<ChanceOutput> chanceOutputs = recipe.getChanceOutputs();
    pBuffer.writeVarInt(chanceOutputs.size());
    for (ChanceOutput output : chanceOutputs) {
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
  }
}
