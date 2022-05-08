package mysticmods.roots.api.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.recipe.processors.RootsProcessor;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import noobanidus.libs.noobutil.ingredient.IngredientStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class RootsRecipe<H extends IItemHandler, W extends IRootsCrafting<H>> implements IRootsRecipe<H, W> {
  protected final NonNullList<Ingredient> ingredients;
  protected final ItemStack result;
  protected final List<RootsProcessor<? super W>> processors = new ArrayList<>();
  protected final ResourceLocation recipeId;

  @FunctionalInterface
  public interface RootsRecipeBuilder<R extends RootsRecipe<?, ?>> {
    R create(NonNullList<Ingredient> ingredients, ItemStack result, ResourceLocation recipeId);
  }

  public RootsRecipe(NonNullList<Ingredient> ingredients, ItemStack result, ResourceLocation recipeId) {
    this.ingredients = ingredients;
    this.result = result;
    this.recipeId = recipeId;
  }

  @Override
  public ItemStack getResultItem() {
    return result.copy();
  }

  @Override
  public ResourceLocation getId() {
    return recipeId;
  }

  public abstract static class Serializer<H extends IItemHandler, W extends IRootsCrafting<H>, R extends RootsRecipe<H, W>> extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<R> {

    private final RootsRecipeBuilder<R> builder;

    public Serializer(RootsRecipeBuilder<R> builder) {
      this.builder = builder;
    }

    protected void fromJsonAdditional (R recipe, ResourceLocation pRecipeId, JsonObject pJson) {
    }

    @Override
    public R fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
      JsonArray incoming = GsonHelper.getAsJsonArray(pJson, "ingredients");
      NonNullList<Ingredient> ingredients = NonNullList.create();
      for (int i = 0; i < incoming.size(); i++) {
        Ingredient ingredient = Ingredient.fromJson(incoming.get(i));
        if (!ingredient.isEmpty()) {
          ingredients.add(ingredient);
        }
      }
      ItemStack result;
      if (pJson.get("result").isJsonObject()) {
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

      R recipe = builder.create(ingredients, result, pRecipeId);
      fromJsonAdditional(recipe, pRecipeId, pJson);
      return recipe;
    }

    protected void fromNetworkAdditional (R recipe, ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
    }

    @Nullable
    @Override
    public R fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
      int ingCount = pBuffer.readVarInt();
      NonNullList<Ingredient> ingredients = NonNullList.withSize(ingCount, Ingredient.EMPTY);
      for (int i = 0; i < ingCount; i++) {
        ingredients.set(i, Ingredient.fromNetwork(pBuffer));
      }

      ItemStack result = pBuffer.readItem();
      R recipe = builder.create(ingredients, result, pRecipeId);
      fromNetworkAdditional(recipe, pRecipeId, pBuffer);
      return recipe;
    }

    protected void toNetworkAdditional (R recipe, FriendlyByteBuf pBuffer) {
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, R recipe) {
      pBuffer.writeVarInt(recipe.getIngredients().size());
      for (Ingredient ingredient : recipe.getIngredients()) {
        ingredient.toNetwork(pBuffer);
      }
      pBuffer.writeItem(recipe.getResultItem());
      toNetworkAdditional(recipe, pBuffer);
    }
  }

  // TODO: NBT SUPPORT???
  public abstract static class Builder {
    protected final int count;
    protected final Item result;
    protected final List<IngredientStack> ingredients = new ArrayList<>();

    protected Builder(ItemLike item, int count) {
      this.result = item.asItem();
      this.count = count;
    }

    public abstract RecipeSerializer<?> getSerializer();

    public Builder addIngredient(TagKey<Item> ingredient) {
      addIngredient(ingredient, 1);
      return this;
    }

    public Builder addIngredient(TagKey<Item> ingredient, int count) {
      addIngredient(Ingredient.of(ingredient), count);
      return this;
    }

    public Builder addIngredient(Ingredient ingredient) {
      addIngredient(ingredient, 1);
      return this;
    }

    public Builder addIngredient(Ingredient ingredient, int count) {
      addIngredient(new IngredientStack(ingredient, count));
      return this;
    }

    public Builder addIngredient(ItemLike item) {
      addIngredient(item, 1);
      return this;
    }

    public Builder addIngredient(ItemLike item, int count) {
      addIngredient(new IngredientStack(Ingredient.of(item), count));
      return this;
    }

    public Builder addIngredient(IngredientStack ingredientStack) {
      this.ingredients.add(ingredientStack);
      return this;
    }

    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation recipeName) {
      consumer.accept(new Result(recipeName, result, count, ingredients, getSerializer()));
    }

    public static class Result implements FinishedRecipe {
      private final ResourceLocation id;
      private final Item result;
      private final int count;
      private final List<IngredientStack> ingredients;
      private final RecipeSerializer<?> serializer;

      public Result(ResourceLocation id, Item result, int count, List<IngredientStack> ingredients, RecipeSerializer<?> serializer) {
        this.id = id;
        this.result = result;
        this.count = count;
        this.ingredients = ingredients;
        this.serializer = serializer;
        if (this.ingredients.isEmpty()) {
          throw new IllegalArgumentException("ingredients for recipe " + id + " cannot be empty");
        }
      }

      @Override
      public void serializeRecipeData(JsonObject json) {
        JsonArray array = new JsonArray();

        for (IngredientStack ingredient : this.ingredients) {
          array.add(ingredient.serialize());
        }

        json.add("ingredients", array);
        JsonObject item = new JsonObject();
        item.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result).toString());
        if (count > 1) {
          item.addProperty("count", count);
        }
        json.add("result", item);
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
      }

      @Nullable
      @Override
      public ResourceLocation getAdvancementId() {
        return null;
      }
    }
  }
}
