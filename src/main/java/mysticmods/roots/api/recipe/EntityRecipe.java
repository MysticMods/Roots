package mysticmods.roots.api.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.crafting.IEntityCrafting;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class EntityRecipe<W extends IEntityCrafting> extends RootsRecipeBase implements IEntityRecipe<W> {

  public EntityRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }

  @Override
  public NonNullList<Ingredient> getBaseIngredients() {
    return NonNullList.create();
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
  }


  @Override
  public ResourceLocation getId() {
    return recipeId;
  }

  public BlockState modifyState(W pContainer, BlockState state) {
    return state;
  }

  @Override
  public ItemStack assemble(W pInv) {
    Level level = pInv.getLevel();
    if (level == null) {
      throw new IllegalStateException("Cannot assemble recipe `" + getId() + "` without a world!");
    }
    if (!level.isClientSide()) {
      BlockPos pos = pInv.getBlockPos();
      BlockState newState = modifyState(pInv, level.getBlockState(pos));
      level.setBlock(pos, newState, 11);
      Player player = pInv.getPlayer();
      if (player != null) {
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(pInv.getPlayer(), newState));
        for (Grant grant : getGrants()) {
          grant.accept((ServerPlayer) player);
        }
      }
    }

    return getResultItem().copy();
  }

  public abstract static class Serializer<W extends IEntityCrafting, R extends EntityRecipe<W>> extends RootsSerializerBase implements RecipeSerializer<R> {

    private final EntityRecipeBuilder<R> builder;

    public Serializer(EntityRecipeBuilder<R> builder) {
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
      // TODO: etc
      toNetworkAdditional(recipe, pBuffer);
    }
  }

  public abstract static class Builder extends RootsRecipeBuilderBase {


    protected Builder() {
    }

    protected boolean allowEmptyOutput() {
      return true;
    }

    protected Builder(ItemStack result) {
      super(result);
    }

    public abstract RecipeSerializer<?> getSerializer();

    public void doSave(Consumer<FinishedRecipe> consumer, ResourceLocation recipeName) {
      consumer.accept(new EntityRecipe.Builder.Result(recipeName, result, entityMatcher, chanceOutputs, grants, levelConditions, playerConditions, getSerializer(), advancement, getAdvancementId(recipeName)));
    }

    public static class Result extends RootsResultBase {

      public Result(ResourceLocation id, ItemStack result, List<ChanceOutput> chanceOutputs, List<Grant> grants, List<LevelCondition> levelConditions, List<PlayerCondition> playerConditions, RecipeSerializer<?> serializer, Advancement.Builder advancementBuilder, ResourceLocation advancementId) {
        super(id, result, Collections.emptyList(), chanceOutputs, grants, levelConditions, playerConditions, serializer, advancementBuilder, advancementId);
      }

      @Override
      public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);

        json.add("outputState", BlockState.CODEC.encodeStart(JsonOps.INSTANCE, outputState).getOrThrow(false, s -> {
          throw new IllegalStateException(s);
        }));
        json.add("condition", Condition.CODEC.encodeStart(JsonOps.INSTANCE, condition).getOrThrow(false, s -> {
          throw new IllegalStateException(s);
        }));
      }
    }
  }

  @FunctionalInterface
  public interface EntityRecipeBuilder<R extends EntityRecipe<?>> {
    R create(ResourceLocation recipeId);
  }

}
