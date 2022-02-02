package mysticmods.roots.recipe.summon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.recipe.IBoundlessRecipe;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModRegistries;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import noobanidus.libs.noobutil.ingredient.IngredientStack;
import noobanidus.libs.noobutil.processor.Processor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class SummonCreaturesRecipe implements IBoundlessRecipe<SummonCreaturesCrafting>, net.minecraft.item.crafting.IRecipe<SummonCreaturesCrafting> {
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
  public boolean matches(SummonCreaturesCrafting pInv, World pLevel) {
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
  public IRecipeSerializer<?> getSerializer() {
    return null;
  }

  @Override
  public IRecipeType<?> getType() {
    return null;
  }

  public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SummonCreaturesRecipe> {

    @Override
    public SummonCreaturesRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
      JsonArray incoming = JSONUtils.getAsJsonArray(pJson, "ingredients");
      NonNullList<Ingredient> ingredients = NonNullList.create();
      for (int i = 0; i < incoming.size(); i++) {
        Ingredient ingredient = Ingredient.fromJson(incoming.get(i));
        if (!ingredient.isEmpty()) {
          ingredients.add(ingredient);
        }
      }
      EntityType<?> result;
      if (pJson.has("result")) {
        result = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(JSONUtils.getAsString(pJson, "result")));
        if (result == null) {
          throw new JsonSyntaxException("Entity Type '" + JSONUtils.getAsString(pJson, "result") + "' does not exist");
        }
      } else {
        throw new JsonSyntaxException("Missing entity type for recipe " + pRecipeId);
      }

return new SummonCreaturesRecipe(ingredients, result, pRecipeId);
    }

    @Nullable
    @Override
    public SummonCreaturesRecipe fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
      int ingCount = pBuffer.readVarInt();
      NonNullList<Ingredient> ingredients = NonNullList.withSize(ingCount, Ingredient.EMPTY);
      for (int i = 0; i < ingCount; i++) {
        ingredients.set(i, Ingredient.fromNetwork(pBuffer));
      }

      ResourceLocation rl = pBuffer.readResourceLocation();
      EntityType<?> result = ForgeRegistries.ENTITIES.getValue(rl);

      return new SummonCreaturesRecipe(ingredients, result, pRecipeId);
    }

    @Override
    public void toNetwork(PacketBuffer pBuffer, SummonCreaturesRecipe recipe) {
      pBuffer.writeVarInt(recipe.getIngredients().size());
      for (Ingredient ingredient : recipe.getIngredients()) {
        ingredient.toNetwork(pBuffer);
      }
      pBuffer.writeResourceLocation(Objects.requireNonNull(recipe.getResultEntity().getRegistryName()));
    }
  }

  public static class Builder {
    private final EntityType<?> result;
    private final List<IngredientStack> ingredients = new ArrayList<>();

    protected Builder(EntityType<?> result) {
      this.result = result;
    }

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
      consumer.accept(new Result(recipeName, result, ingredients));
    }

    public static class Result implements IFinishedRecipe {
      private final ResourceLocation id;
      private final ResourceLocation result;
      private final List<IngredientStack> ingredients;

      public Result(ResourceLocation id, EntityType<?> result, List<IngredientStack> ingredients) {
        this.id = id;
        this.result = result.getRegistryName();
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
      public IRecipeSerializer<?> getType() {
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