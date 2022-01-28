package mysticmods.roots.recipe.summon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.recipe.IBoundlessRecipe;
import mysticmods.roots.api.recipe.IIngredientStackRecipe;
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

public class SummonCreaturesRecipe implements IBoundlessRecipe<SummonCreaturesCrafting>, IIngredientStackRecipe<SummonCreaturesCrafting> {
  protected final NonNullList<IngredientStack> ingredients;
  protected final EntityType<?> result;
  protected final List<Processor<SummonCreaturesCrafting>> processors = new ArrayList<>();
  protected final ResourceLocation recipeId;

  public SummonCreaturesRecipe(NonNullList<IngredientStack> ingredients, EntityType<?> result, ResourceLocation recipeId) {
    this.ingredients = ingredients;
    this.result = result;
    this.recipeId = recipeId;
  }

  @Override
  public NonNullList<IngredientStack> getIngredientStacks() {
    return ingredients;
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

  @Override
  public List<Processor<SummonCreaturesCrafting>> getProcessors() {
    return processors;
  }

  @Override
  public void addProcessor(Processor<SummonCreaturesCrafting> processor) {
    this.processors.add(processor);
  }

  public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SummonCreaturesRecipe> {

    @Override
    public SummonCreaturesRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
      JsonArray incoming = JSONUtils.getAsJsonArray(pJson, "ingredients");
      NonNullList<IngredientStack> ingredients = NonNullList.withSize(incoming.size(), IngredientStack.EMPTY);
      for (int i = 0; i < incoming.size(); i++) {
        JsonElement element = incoming.get(i);
        if (element.isJsonObject()) {
          ingredients.set(i, IngredientStack.deserialize(element.getAsJsonObject()));
        } else {
          throw new JsonSyntaxException("Invalid ingredient at index " + i + " in recipe " + pRecipeId + ", ingredient " + element + " is not a JsonObject");
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

      SummonCreaturesRecipe recipe = new SummonCreaturesRecipe(ingredients, result, pRecipeId);

      if (pJson.has("processors")) {
        JsonArray jsonProcessors = JSONUtils.getAsJsonArray(pJson, "processors");
        for (JsonElement element : jsonProcessors) {
          try {
            //noinspection unchecked
            recipe.addProcessor((Processor<SummonCreaturesCrafting>) ModRegistries.PROCESSOR_REGISTRY.getValue(new ResourceLocation(element.getAsString())));
          } catch (ClassCastException e) {
            throw new JsonSyntaxException("Processor '" + element.getAsString() + "' is not a valid processor for recipe " + pRecipeId);
          }
        }
      }

      return recipe;
    }

    @Nullable
    @Override
    public SummonCreaturesRecipe fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
      int ingCount = pBuffer.readVarInt();
      NonNullList<IngredientStack> ingredients = NonNullList.withSize(ingCount, IngredientStack.EMPTY);
      for (int i = 0; i < ingCount; i++) {
        ingredients.set(i, IngredientStack.read(pBuffer));
      }

      ResourceLocation rl = pBuffer.readResourceLocation();
      EntityType<?> result = ForgeRegistries.ENTITIES.getValue(rl);

      SummonCreaturesRecipe recipe = new SummonCreaturesRecipe(ingredients, result, pRecipeId);

      int procCount = pBuffer.readVarInt();
      for (int i = 0; i < procCount; i++) {
        rl = pBuffer.readResourceLocation();
        //noinspection unchecked
        recipe.addProcessor((Processor<SummonCreaturesCrafting>) ModRegistries.PROCESSOR_REGISTRY.getValue(rl));
      }

      return recipe;
    }

    @Override
    public void toNetwork(PacketBuffer pBuffer, SummonCreaturesRecipe recipe) {
      pBuffer.writeVarInt(recipe.getIngredientStacks().size());
      for (IngredientStack ingredient : recipe.getIngredientStacks()) {
        ingredient.write(pBuffer);
      }
      pBuffer.writeResourceLocation(Objects.requireNonNull(recipe.getResultEntity().getRegistryName()));
      pBuffer.writeVarInt(recipe.getProcessors().size());
      for (Processor<SummonCreaturesCrafting> processor : recipe.getProcessors()) {
        pBuffer.writeResourceLocation(Objects.requireNonNull(processor.getRegistryName()));
      }
    }
  }

  public static class Builder {
    private final EntityType<?> result;
    private final List<IngredientStack> ingredients = new ArrayList<>();
    private final List<Processor<?>> processors = new ArrayList<>();

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

    public Builder addProcessor(Processor<SummonCreaturesCrafting> processor) {
      this.processors.add(processor);
      return this;
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation recipeName) {
      consumer.accept(new Result(recipeName, result, ingredients, processors));
    }

    public static class Result implements IFinishedRecipe {
      private final ResourceLocation id;
      private final ResourceLocation result;
      private final List<IngredientStack> ingredients;
      private final List<Processor<?>> processors;

      public Result(ResourceLocation id, EntityType<?> result, List<IngredientStack> ingredients, List<Processor<?>> processors) {
        this.id = id;
        this.result = result.getRegistryName();
        this.ingredients = ingredients;
        this.processors = processors;
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

        JsonArray processors = new JsonArray();
        for (Processor<?> processor : this.processors) {
          processors.add(Objects.requireNonNull(processor.getRegistryName()).toString());
        }
        json.add("processors", processors);
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
}