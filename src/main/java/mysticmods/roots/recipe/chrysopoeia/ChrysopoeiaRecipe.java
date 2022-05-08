package mysticmods.roots.recipe.chrysopoeia;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.recipe.PlayerOffhandInventoryHandler;
import mysticmods.roots.init.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class ChrysopoeiaRecipe implements Recipe<PlayerOffhandInventoryHandler> {
  private final NonNullList<Ingredient> ingredients;
  private final ItemStack result;
  private final ResourceLocation recipeId;

  public ChrysopoeiaRecipe(NonNullList<Ingredient> ingredients, ItemStack result, ResourceLocation recipeId) {
    this.ingredients = ingredients;
    this.result = result;
    this.recipeId = recipeId;
  }

  @Override
  public boolean matches(PlayerOffhandInventoryHandler pInv, Level pLevel) {
    return ingredients.get(0).test(pInv.getItem(0));
  }

  // TODO:
  @Override
  public ItemStack assemble(PlayerOffhandInventoryHandler pInv) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canCraftInDimensions(int pWidth, int pHeight) {
    return true;
  }

  @Override
  public ItemStack getResultItem() {
    return result.copy();
  }

  @Override
  public ResourceLocation getId() {
    return recipeId;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModRecipes.Serializers.CHRYSOPOEIA.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.Types.SUMMON_CREATURES;
  }

  public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ChrysopoeiaRecipe> {
    @Override
    public ChrysopoeiaRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
      JsonArray incoming = GsonHelper.getAsJsonArray(pJson, "ingredients");
      NonNullList<Ingredient> ingredients = NonNullList.withSize(incoming.size(), Ingredient.EMPTY);
      for (int i = 0; i < incoming.size(); i++) {
        JsonElement element = incoming.get(i);
        if (element.isJsonObject()) {
          ingredients.set(i, Ingredient.fromJson(element.getAsJsonObject()));
        } else {
          throw new JsonSyntaxException("Invalid ingredient at index " + i + " in recipe " + pRecipeId + ", ingredient " + element + " is not a JsonObject");
        }
      }
      ItemStack result;
      if (pJson.has("result")) {
        result = ShapedRecipe.itemStackFromJson(pJson.getAsJsonObject("result"));
      } else {
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
        result = new ItemStack(item, count);
      }

      return new ChrysopoeiaRecipe(ingredients, result, pRecipeId);
    }

    @Nullable
    @Override
    public ChrysopoeiaRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
      int ingCount = pBuffer.readVarInt();
      NonNullList<Ingredient> ingredients = NonNullList.withSize(ingCount, Ingredient.EMPTY);
      for (int i = 0; i < ingCount; i++) {
        ingredients.set(i, Ingredient.fromNetwork(pBuffer));
      }

      ItemStack result = pBuffer.readItem();
      return new ChrysopoeiaRecipe(ingredients, result, pRecipeId);
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, ChrysopoeiaRecipe recipe) {
      pBuffer.writeVarInt(recipe.getIngredients().size());
      for (Ingredient ingredient : recipe.getIngredients()) {
        ingredient.toNetwork(pBuffer);
      }
      pBuffer.writeItem(recipe.getResultItem());
    }
  }
}
