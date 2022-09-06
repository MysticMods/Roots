package mysticmods.roots.recipe.summon;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.recipe.IBoundlessRecipe;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.init.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import noobanidus.libs.noobutil.ingredient.IngredientStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

// TODO: However this is going to work
public class SummonCreaturesRecipe implements IBoundlessRecipe<SummonCreaturesCrafting>, net.minecraft.world.item.crafting.Recipe<SummonCreaturesCrafting> {
  protected final NonNullList<Ingredient> ingredients;
  protected final EntityType<?> result;
  protected final ResourceLocation recipeId;

  public SummonCreaturesRecipe(NonNullList<Ingredient> ingredients, EntityType<?> result, ResourceLocation recipeId) {
    this.ingredients = ingredients;
    this.result = result;
    this.recipeId = recipeId;
  }

  public EntityType<?> getResultEntity() {
    return result;
  }

  // TODO:
  @Override
  public boolean matches(SummonCreaturesCrafting pInv, Level pLevel) {
    return false;
  }

  @Override
  public ItemStack assemble(SummonCreaturesCrafting pInv) {
    return ItemStack.EMPTY;
  }

  @Override
  public ItemStack getResultItem() {
    return ItemStack.EMPTY;
  }

  @Override
  public ResourceLocation getId() {
    return recipeId;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModRecipes.Serializers.SUMMON_CREATURES.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.Types.SUMMON_CREATURES.get();
  }

  public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<SummonCreaturesRecipe> {

    @Override
    public SummonCreaturesRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
      JsonArray incoming = GsonHelper.getAsJsonArray(pJson, "ingredients");
      NonNullList<Ingredient> ingredients = NonNullList.create();
      for (int i = 0; i < incoming.size(); i++) {
        Ingredient ingredient = Ingredient.fromJson(incoming.get(i));
        if (!ingredient.isEmpty()) {
          ingredients.add(ingredient);
        }
      }
      EntityType<?> result;
      if (pJson.has("result")) {
        result = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(GsonHelper.getAsString(pJson, "result")));
        if (result == null) {
          throw new JsonSyntaxException("Entity Type '" + GsonHelper.getAsString(pJson, "result") + "' does not exist");
        }
      } else {
        throw new JsonSyntaxException("Missing entity type for recipe " + pRecipeId);
      }

      return new SummonCreaturesRecipe(ingredients, result, pRecipeId);
    }

    @Nullable
    @Override
    public SummonCreaturesRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
      int ingCount = pBuffer.readVarInt();
      NonNullList<Ingredient> ingredients = NonNullList.withSize(ingCount, Ingredient.EMPTY);
      for (int i = 0; i < ingCount; i++) {
        ingredients.set(i, Ingredient.fromNetwork(pBuffer));
      }

      int resultId = pBuffer.readVarInt();
      EntityType<?> result = Registries.ENTITY_REGISTRY.get().getValue(resultId);

      return new SummonCreaturesRecipe(ingredients, result, pRecipeId);
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, SummonCreaturesRecipe recipe) {
      pBuffer.writeVarInt(recipe.getIngredients().size());
      for (Ingredient ingredient : recipe.getIngredients()) {
        ingredient.toNetwork(pBuffer);
      }
      pBuffer.writeVarInt(Registries.ENTITY_REGISTRY.get().getID(recipe.getResultEntity()));
    }
  }

  public static class Builder {
    private final EntityType<?> result;
    private final List<IngredientStack> ingredients = new ArrayList<>();

    protected Builder(EntityType<?> result) {
      this.result = result;
    }

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
      consumer.accept(new Result(recipeName, result, ingredients));
    }

    public static class Result implements FinishedRecipe {
      private final ResourceLocation id;
      private final ResourceLocation result;
      private final List<IngredientStack> ingredients;

      public Result(ResourceLocation id, EntityType<?> result, List<IngredientStack> ingredients) {
        this.id = id;
        this.result = ForgeRegistries.ENTITIES.getKey(result);
        this.ingredients = ingredients;
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
        json.addProperty("result", this.result.toString());
      }

      @Override
      public ResourceLocation getId() {
        return id;
      }

      @Override
      public RecipeSerializer<?> getType() {
        return ModRecipes.Serializers.SUMMON_CREATURES.get();
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

  public static Builder builder(EntityType<?> result) {
    return new Builder(result);
  }
}