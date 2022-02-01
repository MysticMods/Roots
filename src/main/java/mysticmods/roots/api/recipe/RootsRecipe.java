package mysticmods.roots.api.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.recipe.processors.RootsProcessor;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;
import noobanidus.libs.noobutil.crafting.Crafting;
import noobanidus.libs.noobutil.ingredient.IngredientStack;
import noobanidus.libs.noobutil.processor.Processor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

  public abstract static class Serializer<H extends IItemHandler, W extends IRootsCrafting<H>, R extends RootsRecipe<H, W>> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<R> {

    private final RootsRecipeBuilder<R> builder;

    public Serializer(RootsRecipeBuilder<R> builder) {
      this.builder = builder;
    }

    @Override
    public R fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
      JsonArray incoming = JSONUtils.getAsJsonArray(pJson, "ingredients");
      NonNullList<Ingredient> ingredients = NonNullList.create();
      for (int i = 0; i < incoming.size(); i++) {
        Ingredient ingredient = Ingredient.fromJson(incoming.get(i));
        if (!ingredient.isEmpty()) {
          ingredients.add(ingredient);
        }
      }
      ItemStack result;
      if (pJson.get("result").isJsonObject()) {
        result = ShapedRecipe.itemFromJson(pJson.getAsJsonObject("result"));
      } else {
        ResourceLocation id = new ResourceLocation(JSONUtils.getAsString(pJson, "result"));
        Item item = ForgeRegistries.ITEMS.getValue(id);
        if (item == null) {
          throw new JsonSyntaxException("Unknown item '" + id + "'");
        }
        int count;
        if (!pJson.has("count")) {
          count = 1;
        } else {
          count = JSONUtils.getAsInt(pJson, "count");
        }
        result = new ItemStack(item, count);
      }


      return builder.create(ingredients, result, pRecipeId);
    }

    @Nullable
    @Override
    public R fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
      int ingCount = pBuffer.readVarInt();
      NonNullList<Ingredient> ingredients = NonNullList.withSize(ingCount, Ingredient.EMPTY);
      for (int i = 0; i < ingCount; i++) {
        ingredients.set(i, Ingredient.fromNetwork(pBuffer));
      }

      ItemStack result = pBuffer.readItem();
      return builder.create(ingredients, result, pRecipeId);
    }

    @Override
    public void toNetwork(PacketBuffer pBuffer, R recipe) {
      pBuffer.writeVarInt(recipe.getIngredients().size());
      for (Ingredient ingredient : recipe.getIngredients()) {
        ingredient.toNetwork(pBuffer);
      }
      pBuffer.writeItem(recipe.getResultItem());
    }
  }

  // TODO: NBT SUPPORT???
  public abstract static class Builder {
    private final int count;
    private final Item result;
    private final List<IngredientStack> ingredients = new ArrayList<>();

    protected Builder(IItemProvider item, int count) {
      this.result = item.asItem();
      this.count = count;
    }

    public abstract IRecipeSerializer<?> getSerializer();

    public Builder addIngredient(ITag<Item> ingredient) {
      addIngredient(ingredient, 1);
      return this;
    }

    public Builder addIngredient(ITag<Item> ingredient, int count) {
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

    public Builder addIngredient(IItemProvider item) {
      addIngredient(item, 1);
      return this;
    }

    public Builder addIngredient(IItemProvider item, int count) {
      addIngredient(new IngredientStack(Ingredient.of(item), count));
      return this;
    }

    public Builder addIngredient(IngredientStack ingredientStack) {
      this.ingredients.add(ingredientStack);
      return this;
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation recipeName) {
      consumer.accept(new Result(recipeName, result, count, ingredients, getSerializer()));
    }

    public static class Result implements IFinishedRecipe {
      private final ResourceLocation id;
      private final Item result;
      private final int count;
      private final List<IngredientStack> ingredients;
      private final IRecipeSerializer<?> serializer;

      public Result(ResourceLocation id, Item result, int count, List<IngredientStack> ingredients, IRecipeSerializer<?> serializer) {
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
      public IRecipeSerializer<?> getType() {
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
