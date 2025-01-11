package mysticmods.roots.recipe.bark;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.recipe.BaseRecipeData;
import mysticmods.roots.api.recipe.WorldCondition;
import mysticmods.roots.api.recipe.WorldRecipe;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.api.world.PartialBlockState;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.recipe.SimpleWorldCrafting;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class BarkRecipe extends WorldRecipe<SimpleWorldCrafting> {
  public static MapCodec<BarkRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
      BaseRecipeData.CODEC.fieldOf("data").forGetter((o) -> o.data),
      PartialBlockState.CODEC.optionalFieldOf("outputState").forGetter((o) -> Optional.ofNullable(o.outputState)),
      WorldCondition.CODEC.fieldOf("condition").forGetter(o -> o.condition),
      OutputStateMapper.CODEC.optionalFieldOf("stateMapper").forGetter(o -> Optional.ofNullable(o.stateMapper))
  ).apply(instance, BarkRecipe::new));
  public static StreamCodec<RegistryFriendlyByteBuf, BarkRecipe> STREAM_CODEC = StreamCodec.composite(
      BaseRecipeData.STREAM_CODEC, o -> o.data,
      ByteBufCodecs.optional(PartialBlockState.STREAM_CODEC), o -> Optional.ofNullable(o.outputState),
      WorldCondition.STREAM_CODEC, o -> o.condition,
      ByteBufCodecs.optional(OutputStateMapper.STREAM_CODEC), o -> Optional.ofNullable(o.stateMapper),
      BarkRecipe::new
  );

  private OutputStateMapper stateMapper;

  public BarkRecipe() {
    super();
  }

  public BarkRecipe(BaseRecipeData data, PartialBlockState outputState, WorldCondition condition) {
    super(data, outputState, condition);
  }

  public BarkRecipe(BaseRecipeData data, PartialBlockState outputState, WorldCondition condition, OutputStateMapper stateMapper) {
    super(data, outputState, condition);
    this.stateMapper = stateMapper;
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public BarkRecipe(BaseRecipeData baseRecipeData, Optional<PartialBlockState> partialBlockState, WorldCondition worldCondition, Optional<OutputStateMapper> stateMapper) {
    this(baseRecipeData, partialBlockState.orElse(null), worldCondition, stateMapper.orElse(null));
  }

  @Nullable
  public OutputStateMapper getStateMapper() {
    return stateMapper;
  }

  @Override
  public BlockState modifyState(SimpleWorldCrafting pContainer, BlockState currentState, HolderLookup.Provider provider) {
    BlockState newState;
    if (stateMapper.isEmpty()) {
      if (outputState == null) {
        RootsAPI.LOG.error("Invalid recipe '{}': no output state or state mapper", this);
        newState = Blocks.AIR.defaultBlockState();
      } else {
        newState = outputState.copyState(currentState, List.of(RotatedPillarBlock.AXIS.getName()));
      }
    } else {
      Block block = currentState.getBlock();
      Block mappedBlock = stateMapper.get(block);
      if (mappedBlock == null) {
        newState = Blocks.AIR.defaultBlockState();
      } else {
        newState = mappedBlock.defaultBlockState();
      }
    }

    if (currentState.getBlock() instanceof RotatedPillarBlock && newState.getBlock() instanceof RotatedPillarBlock) {
      newState = newState.setValue(RotatedPillarBlock.AXIS, currentState.getValue(RotatedPillarBlock.AXIS));
    }

    return super.modifyState(pContainer, newState, provider);
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.BARK.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.BARK.get();
  }

  @Override
  public String getGroup() {
    return Identifiers.BARK_RECIPE_GROUP;
  }

  public Pair<BaseRecipeData.Builder, Builder> builder() {
    return Pair.of(data.builder(), new Builder(outputState.copy(), condition, stateMapper.copy()));
  }

  public static class Serializer implements RecipeSerializer<BarkRecipe> {

    @Override
    public MapCodec<BarkRecipe> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, BarkRecipe> streamCodec() {
      return STREAM_CODEC;
    }
  }

  public static class Builder {
    private PartialBlockState outputState;
    private WorldCondition condition;
    private OutputStateMapper stateMapper;

    public Builder() {
    }

    public Builder(PartialBlockState outputState, WorldCondition condition, OutputStateMapper stateMapper) {
      this.outputState = outputState;
      this.condition = condition;
      this.stateMapper = stateMapper;
    }

    public Builder stateMapper(OutputStateMapper stateMapper) {
      this.stateMapper = stateMapper;
      return this;
    }

    public Builder stateMapper(Block... blocks) {
      this.stateMapper = new OutputStateMapper(blocks);
      return this;
    }

    public Builder condition(WorldCondition condition) {
      this.condition = condition;
      return this;
    }

    public Builder outputState(PartialBlockState outputState) {
      this.outputState = outputState;
      return this;
    }

    public BarkRecipe build(BaseRecipeData data) {
      if (outputState == null && stateMapper == null || stateMapper.isEmpty()) {
        throw new IllegalStateException("Cannot build a bark recipe without an output state or state mapper");
      }
      return new BarkRecipe(data, outputState, condition, stateMapper);
    }

    public BarkRecipe build(BaseRecipeData.Builder data) {
      return build(data.build());
    }

    public static Builder create() {
      return new Builder();
    }
  }
}
