package mysticmods.roots.recipe.bark;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.recipe.BaseRecipeData;
import mysticmods.roots.api.recipe.WorldCondition;
import mysticmods.roots.api.recipe.WorldRecipe;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.api.test.world.PartialBlockState;
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
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BarkRecipe extends WorldRecipe<SimpleWorldCrafting> {
  public static MapCodec<BarkRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
      BaseRecipeData.CODEC.codec().optionalFieldOf("data")
          .forGetter(o -> o.data.isEmpty() ? Optional.empty() : Optional.of(o.data)),
      PartialBlockState.CODEC.optionalFieldOf("outputState").forGetter((o) -> Optional.ofNullable(o.outputState)),
      WorldCondition.LIST_CODEC.fieldOf("condition").forGetter(o -> o.conditions),
      Codec.STRING.listOf().optionalFieldOf("skipProperties")
          .forGetter((o) -> o.skipProperties.isEmpty() ? Optional.empty() : Optional.of(o.skipProperties)),
      Codec.INT.fieldOf("durabilityCost").forGetter((o) -> o.durabilityCost),
      OutputStateMapper.CODEC.optionalFieldOf("stateMapper").forGetter(o -> Optional.ofNullable(o.stateMapper))
  ).apply(instance, BarkRecipe::new));
  public static StreamCodec<RegistryFriendlyByteBuf, BarkRecipe> STREAM_CODEC = StreamCodec.composite(
      BaseRecipeData.STREAM_CODEC, o -> o.data,
      ByteBufCodecs.optional(PartialBlockState.STREAM_CODEC), o -> Optional.ofNullable(o.outputState),
      WorldCondition.LIST_STREAM_CODEC, o -> o.conditions,
      ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list())), o -> o.skipProperties.isEmpty() ? Optional.empty() : Optional.of(o.skipProperties),
      ByteBufCodecs.VAR_INT, o -> o.durabilityCost,
      ByteBufCodecs.optional(OutputStateMapper.STREAM_CODEC), o -> Optional.ofNullable(o.stateMapper),
      BarkRecipe::new
  );

  private int durabilityCost = 1;
  private OutputStateMapper stateMapper;

  public BarkRecipe(BaseRecipeData data, PartialBlockState outputState, List<WorldCondition> condition, List<String> skipProperties, int durabilityCost) {
    super(data, outputState, condition, skipProperties);
    this.durabilityCost = durabilityCost;
  }

  public BarkRecipe(BaseRecipeData data, PartialBlockState outputState, List<WorldCondition> condition, List<String> skipProperties, int durabilityCost, OutputStateMapper stateMapper) {
    super(data, outputState, condition, skipProperties);
    this.stateMapper = stateMapper;
    this.durabilityCost = durabilityCost;
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public BarkRecipe(BaseRecipeData baseRecipeData, Optional<PartialBlockState> partialBlockState, List<WorldCondition> worldCondition, Optional<List<String>> skipProperties, int durabilityCost, Optional<OutputStateMapper> stateMapper) {
    this(baseRecipeData, partialBlockState.orElse(null), worldCondition, skipProperties.orElse(Collections.emptyList()), durabilityCost, stateMapper.orElse(null));
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public BarkRecipe(Optional<BaseRecipeData> baseRecipeData, Optional<PartialBlockState> partialBlockState, List<WorldCondition> worldCondition, Optional<List<String>> strings, int durabilityCost, Optional<OutputStateMapper> outputStateMapper) {
    super(baseRecipeData.orElse(new BaseRecipeData()), partialBlockState.orElse(null), worldCondition, strings.orElse(Collections.emptyList()));
    this.stateMapper = outputStateMapper.orElse(null);
    this.durabilityCost = durabilityCost;
  }

  @Override
  public boolean hasOtherOutput(HolderLookup.Provider provider) {
    return true;
  }

  @Nullable
  public OutputStateMapper getStateMapper() {
    return stateMapper;
  }

  @Override
  public BlockState modifyState(SimpleWorldCrafting pContainer, BlockState currentState, HolderLookup.Provider provider) {
    List<String> propertiesToSkip = new ArrayList<>(skipProperties);
    propertiesToSkip.add(RotatedPillarBlock.AXIS.getName());
    BlockState newState;
    boolean propertiesCopied = false;
    if (stateMapper == null || stateMapper.isEmpty()) {
      if (outputState == null) {
        RootsAPI.LOG.error("Invalid recipe '{}': no output state or state mapper", this);
        newState = currentState;
      } else {
        newState = outputState.copyState(currentState, propertiesToSkip);
        propertiesCopied = true;
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

    if (!propertiesCopied) {
      for (Property<?> property : currentState.getProperties()) {
        if (!propertiesToSkip.contains(property.getName()) && newState.hasProperty(property)) {
          newState = PartialBlockState.uncheckedSet(property, currentState.getValue(property), newState);
        }
      }
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
    private List<WorldCondition> condition = new ArrayList<>();
    private OutputStateMapper stateMapper;
    private int durabilityCost = 1;
    private final List<String> skipProperties = new ArrayList<>();

    protected Builder() {
    }

    protected Builder(PartialBlockState outputState, List<WorldCondition> condition, OutputStateMapper stateMapper) {
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
      this.condition.add(condition);
      return this;
    }

    public Builder outputState(PartialBlockState outputState) {
      this.outputState = outputState;
      return this;
    }

    public Builder durabilityCost(int durabilityCost) {
      this.durabilityCost = durabilityCost;
      return this;
    }


    public Builder skipProperties(String... properties) {
      Collections.addAll(skipProperties, properties);
      return this;
    }

    public Builder skipProperty(String property) {
      skipProperties.add(property);
      return this;
    }

    public Builder skipProperties(Property<?>... properties) {
      for (Property<?> property : properties) {
        skipProperties.add(property.getName());
      }
      return this;
    }

    public Builder skipProperty(Property<?> property) {
      skipProperties.add(property.getName());
      return this;
    }

    public BarkRecipe build(BaseRecipeData data) {
      if (outputState == null && stateMapper == null || stateMapper.isEmpty()) {
        throw new IllegalStateException("Cannot build a bark recipe without an output state or state mapper");
      }
      return new BarkRecipe(data, outputState, condition, skipProperties, durabilityCost, stateMapper);
    }

    public BarkRecipe build(BaseRecipeData.Builder data) {
      return build(data.build());
    }

    public static Builder create() {
      return new Builder();
    }
  }
}
