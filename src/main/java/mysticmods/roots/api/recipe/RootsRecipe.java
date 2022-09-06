package mysticmods.roots.api.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.recipe.crafting.IRootsCrafting;
import mysticmods.roots.api.recipe.output.ConditionalOutput;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public abstract class RootsRecipe<H extends IItemHandler, W extends IRootsCrafting<H>> implements IRootsRecipe<H, W> {
  protected final NonNullList<Ingredient> ingredients = NonNullList.create();
  protected final ResourceLocation recipeId;
  protected final List<ConditionalOutput> conditionalOutputs = new ArrayList<>();
  protected final List<Grant> grants = new ArrayList<>();
  protected ItemStack result;

  @FunctionalInterface
  public interface RootsRecipeBuilder<R extends RootsRecipe<?, ?>> {
    R create(ResourceLocation recipeId);
  }

  public RootsRecipe(ResourceLocation recipeId) {
    this.recipeId = recipeId;
  }

  public void setIngredients(NonNullList<Ingredient> ingredients) {
    this.ingredients.clear();
    this.ingredients.addAll(ingredients);
  }

  public void setResultItem(ItemStack result) {
    this.result = result;
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

  public void addConditionalOutput(ConditionalOutput output) {
    conditionalOutputs.add(output);
  }

  public void addConditionalOutputs(Collection<ConditionalOutput> outputs) {
    conditionalOutputs.addAll(outputs);
  }

  public void addGrant(Grant grant) {
    grants.add(grant);
  }

  public void addGrants(Collection<Grant> grants) {
    this.grants.addAll(grants);
  }

  public List<ConditionalOutput> getConditionalOutputs() {
    return conditionalOutputs;
  }

  public List<Grant> getGrants() {
    return grants;
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


  // TODO: Check if the ItemStack means that NBT is supported
  public abstract static class Builder {
    protected ItemStack result;
    protected final List<Ingredient> ingredients = new ArrayList<>();
    protected final List<ConditionalOutput> conditionalOutputs = new ArrayList<>();
    protected final List<Grant> grants = new ArrayList<>();

    protected Builder() {
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

    public Builder addIngredient(TagKey<Item> ingredient) {
      addIngredient(Ingredient.of(ingredient));
      return this;
    }

    public Builder addIngredient(ItemLike item) {
      addIngredient(Ingredient.of(item));
      return this;
    }

    public Builder addIngredient(Ingredient ingredient) {
      this.ingredients.add(ingredient);
      return this;
    }

    public Builder addGrant(Grant grant) {
      this.grants.add(grant);
      return this;
    }

    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation recipeName) {
      consumer.accept(new Result(recipeName, result, ingredients, conditionalOutputs, grants, getSerializer()));
    }

    public static class Result implements FinishedRecipe {
      private final ResourceLocation id;
      private final ItemStack result;
      private final List<Ingredient> ingredients;
      private final RecipeSerializer<?> serializer;
      private final List<ConditionalOutput> conditionalOutputs;
      private final List<Grant> grants;

      public Result(ResourceLocation id, ItemStack result, List<Ingredient> ingredients, List<ConditionalOutput> conditionalOutputs, List<Grant> grants, RecipeSerializer<?> serializer) {
        this.id = id;
        this.result = result;
        this.ingredients = ingredients;
        this.serializer = serializer;
        this.conditionalOutputs = conditionalOutputs;
        this.grants = grants;
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
