package mysticmods.roots.api.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.crafting.IWorldCrafting;
import mysticmods.roots.api.recipe.output.ConditionalOutput;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public abstract class WorldRecipe<W extends IWorldCrafting> extends RootsRecipeBase implements IWorldRecipe<W> {
  protected BlockState outputState;
  protected BlockPredicate matcher;

  public WorldRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }

  @Override
  public NonNullList<Ingredient> getBaseIngredients() {
    return NonNullList.create();
  }

  public void setOutputState (BlockState outputState) {
    this.outputState = outputState;
  }

  public BlockState getOutputState () {
    return this.outputState;
  }

  public BlockPredicate getMatcher() {
    return matcher;
  }

  public void setMatcher(BlockPredicate matcher) {
    this.matcher = matcher;
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
    return false;
  }

  @Override
  public ResourceLocation getId() {
    return recipeId;
  }

  public abstract static class Serializer<W extends IWorldCrafting, R extends WorldRecipe<W>> extends RootsSerializerBase implements RecipeSerializer<R> {

    private final WorldRecipe.WorldRecipeBuilder<R> builder;

    public Serializer(WorldRecipe.WorldRecipeBuilder<R> builder) {
      this.builder = builder;
    }

    protected void fromJsonAdditional(R recipe, ResourceLocation pRecipeId, JsonObject pJson) {
    }

    @Override
    public R fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
      R recipe = builder.create(pRecipeId);
      baseFromJson(recipe, pRecipeId, pJson);
      // Matcher, state
      recipe.outputState = BlockState.CODEC.parse(JsonOps.INSTANCE, pJson.get("outputState")).result().orElseThrow(() -> new IllegalStateException("Could not parse output state for recipe " + pRecipeId));
      recipe.matcher = BlockPredicate.CODEC.parse(JsonOps.INSTANCE, pJson.get("matcher")).result().orElseThrow(() -> new IllegalStateException("Could not parse matcher for recipe " + pRecipeId));
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
      // block state, matcher
      recipe.outputState = pBuffer.readWithCodec(BlockState.CODEC);
      recipe.matcher = pBuffer.readWithCodec(BlockPredicate.CODEC);
      fromNetworkAdditional(recipe, pRecipeId, pBuffer);
      return recipe;
    }

    protected void toNetworkAdditional(R recipe, FriendlyByteBuf pBuffer) {
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, R recipe) {
      baseToNetwork(pBuffer, recipe);
      pBuffer.writeWithCodec(BlockState.CODEC, recipe.outputState);
      pBuffer.writeWithCodec(BlockPredicate.CODEC, recipe.matcher);
      // TODO: etc
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

  public abstract static class Builder extends RootsRecipeBuilderBase {
    protected BlockState outputState;
    protected BlockPredicate matcher;

    protected Builder() {
    }

    protected boolean allowEmptyOutput() {
      return true;
    }

    protected Builder(ItemStack result) {
      super(result);
    }

    public Builder setOutputState (BlockState outputState) {
      this.outputState = outputState;
      return this;
    }

    public Builder setMatcher (BlockPredicate matcher) {
      this.matcher = matcher;
      return this;
    }

    public abstract RecipeSerializer<?> getSerializer();

    public void doSave (Consumer<FinishedRecipe> consumer, ResourceLocation recipeName) {
      consumer.accept(new WorldRecipe.Builder.Result(recipeName, result, outputState, matcher, conditionalOutputs, grants, levelConditions, playerConditions, getSerializer(), advancement, getAdvancementId(recipeName)));
    }

    public static class Result extends RootsResultBase {
      protected final BlockState outputState;
      protected final BlockPredicate matcher;

      public Result(ResourceLocation id, ItemStack result, BlockState outputState, BlockPredicate matcher, List<ConditionalOutput> conditionalOutputs, List<Grant> grants, List<LevelCondition> levelConditions, List<PlayerCondition> playerConditions, RecipeSerializer<?> serializer, Advancement.Builder advancementBuilder, ResourceLocation advancementId) {
        super(id, result, Collections.emptyList(), conditionalOutputs, grants, levelConditions, playerConditions, serializer, advancementBuilder, advancementId);
        this.outputState = outputState;
        this.matcher = matcher;
      }

      @Override
      public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);
        BlockState.CODEC.encodeStart(JsonOps.INSTANCE, outputState).resultOrPartial(RootsAPI.LOG::error).ifPresent((p_218610_1_) -> {
          json.add("outputState", p_218610_1_);
        });
        BlockPredicate.CODEC.encodeStart(JsonOps.INSTANCE, matcher).resultOrPartial(RootsAPI.LOG::error).ifPresent((p_218610_1_) -> {
          json.add("matcher", p_218610_1_);
        });
      }
    }
  }

  @FunctionalInterface
  public interface WorldRecipeBuilder<R extends WorldRecipe<?>> {
    R create(ResourceLocation recipeId);
  }
}
