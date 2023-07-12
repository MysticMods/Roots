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

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return this.ingredients;
  }

  @Override
  public ItemStack getBaseResultItem() {
    return getResultItem();
  }

  @Override
  public NonNullList<Ingredient> getBaseIngredients() {
    return getIngredients();
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

  public abstract static class Serializer<H extends IItemHandler, W extends IRootsCrafting<H>, R extends RootsRecipe<H, W>> extends RootsSerializerBase implements RecipeSerializer<R> {

    private final RootsRecipeBuilder<R> builder;

    public Serializer(RootsRecipeBuilder<R> builder) {
      this.builder = builder;
    }

    protected void fromJsonAdditional(R recipe, ResourceLocation pRecipeId, JsonObject pJson) {
    }

    @Override
    public R fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
      R recipe = builder.create(pRecipeId);
      baseFromJson(recipe, pRecipeId, pJson);
      fromJsonAdditional(recipe, pRecipeId, pJson);
      return recipe;
    }

    protected void fromNetworkAdditional(R recipe, ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
    }

    @Nullable
    @Override
    public R fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
      R recipe = builder.create(pRecipeId);
      baseFromNetwork(recipe, pRecipeId, pBuffer);
      fromNetworkAdditional(recipe, pRecipeId, pBuffer);
      return recipe;
    }

    protected void toNetworkAdditional(R recipe, FriendlyByteBuf pBuffer) {
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, R recipe) {
      baseToNetwork(pBuffer, recipe);
      toNetworkAdditional(recipe, pBuffer);
    }
  }

  @FunctionalInterface
  public interface RootsRecipeBuilder<R extends RootsRecipe<?, ?>> {
    R create(ResourceLocation recipeId);
  }
}
