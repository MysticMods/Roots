package mysticmods.roots.api.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.crafting.IWorldCrafting;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
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
import java.util.Locale;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class WorldRecipe<W extends IWorldCrafting> extends RootsRecipeBase implements IWorldRecipe<W> {
  protected BlockState outputState;
  protected Condition condition;

  public WorldRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }

  @Override
  public NonNullList<Ingredient> getBaseIngredients() {
    return NonNullList.create();
  }

  public void setOutputState(BlockState outputState) {
    this.outputState = outputState;
  }

  public BlockState getOutputState() {
    return this.outputState;
  }

  public Condition getCondition() {
    return condition;
  }

  public void setCondition(Condition condition) {
    this.condition = condition;
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
    return getCondition().test(pContainer.getBlockPos(), pLevel);
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
          grant.grant((ServerPlayer) player);
        }
      }
    }

    return getResultItem().copy();
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
      recipe.outputState = BlockState.CODEC.parse(JsonOps.INSTANCE, pJson.get("output_state")).result().orElseThrow(() -> new IllegalStateException("Could not parse output state for recipe " + pRecipeId));
      recipe.condition = Condition.CODEC.parse(JsonOps.INSTANCE, pJson.get("condition")).result().orElseThrow(() -> new IllegalStateException("Could not parse condition for recipe " + pRecipeId));
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
      recipe.outputState = pBuffer.readWithCodec(BlockState.CODEC);
      recipe.condition = pBuffer.readWithCodec(Condition.CODEC);
      fromNetworkAdditional(recipe, pRecipeId, pBuffer);
      return recipe;
    }

    protected void toNetworkAdditional(R recipe, FriendlyByteBuf pBuffer) {
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, R recipe) {
      baseToNetwork(pBuffer, recipe);
      pBuffer.writeWithCodec(BlockState.CODEC, recipe.outputState);
      pBuffer.writeWithCodec(Condition.CODEC, recipe.condition);
      // TODO: etc
      toNetworkAdditional(recipe, pBuffer);
    }
  }

  public abstract static class Builder extends RootsRecipeBuilderBase {
    protected BlockState outputState;
    protected Condition condition;

    protected Builder() {
    }

    @Override
    protected boolean allowEmptyOutput() {
      return true;
    }

    protected Builder(ItemStack result) {
      super(result);
    }

    public Builder setOutputState(BlockState outputState) {
      this.outputState = outputState;
      return this;
    }

    public Builder setCondition(Condition condition) {
      this.condition = condition;
      return this;
    }

    @Override
    public abstract RecipeSerializer<?> getSerializer();

    @Override
    public void doSave(Consumer<FinishedRecipe> consumer, ResourceLocation recipeName) {
      consumer.accept(new WorldRecipe.Builder.Result(recipeName, result, outputState, condition, chanceOutputs, grants, levelConditions, playerConditions, getSerializer(), advancement, getAdvancementId(recipeName)));
    }

    public static class Result extends RootsResultBase {
      protected final BlockState outputState;
      protected final Condition condition;

      public Result(ResourceLocation id, ItemStack result, BlockState outputState, Condition condition, List<ChanceOutput> chanceOutputs, List<Grant> grants, List<LevelCondition> levelConditions, List<PlayerCondition> playerConditions, RecipeSerializer<?> serializer, Advancement.Builder advancementBuilder, ResourceLocation advancementId) {
        super(id, result, Collections.emptyList(), chanceOutputs, grants, levelConditions, playerConditions, serializer, advancementBuilder, advancementId);
        this.outputState = outputState;
        this.condition = condition;
      }

      @Override
      public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);

        json.add("output_state", BlockState.CODEC.encodeStart(JsonOps.INSTANCE, outputState).getOrThrow(false, s -> {
          throw new IllegalStateException(s);
        }));
        json.add("condition", Condition.CODEC.encodeStart(JsonOps.INSTANCE, condition).getOrThrow(false, s -> {
          throw new IllegalStateException(s);
        }));
      }
    }
  }

  @FunctionalInterface
  public interface WorldRecipeBuilder<R extends WorldRecipe<?>> {
    R create(ResourceLocation recipeId);
  }

  public enum Shift implements Function<BlockPos, BlockPos>, StringRepresentable {
    NONE(null),
    ABOVE(Direction.UP),
    BELOW(Direction.DOWN),
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    EAST(Direction.EAST),
    WEST(Direction.WEST);

    public static final Codec<Shift> CODEC = StringRepresentable.fromEnum(Shift::values);

    private final Direction offset;

    Shift(Direction offset) {
      this.offset = offset;
    }

    @Override
    public BlockPos apply(BlockPos blockPos) {
      return offset == null ? blockPos : blockPos.relative(offset);
    }

    @Override
    public String getSerializedName() {
      return this.name().toLowerCase(Locale.ROOT);
    }
  }

  public static class Condition implements BiPredicate<BlockPos, Level> {
    private final Shift shift;
    private final RuleTest test;

    public static final Codec<Condition> CODEC = RecordCodecBuilder.create((codec) -> codec.group(Shift.CODEC.fieldOf("shift").forGetter((condition) -> condition.shift), RuleTest.CODEC.fieldOf("test").forGetter((condition) -> condition.test)).apply(codec, Condition::new));

    public Condition(Shift shift, RuleTest test) {
      this.shift = shift;
      this.test = test;
    }

    public Condition(RuleTest test) {
      this(Shift.NONE, test);
    }

    @Override
    public boolean test(BlockPos blockPos, Level level) {
      BlockPos pos = shift.apply(blockPos);
      BlockState stateAt = level.getBlockState(pos);
      return test.test(stateAt, level.getRandom());
    }
  }
}
