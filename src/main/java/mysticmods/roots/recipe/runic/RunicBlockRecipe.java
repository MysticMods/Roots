package mysticmods.roots.recipe.runic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.recipe.BaseRecipeData;
import mysticmods.roots.api.recipe.WorldCondition;
import mysticmods.roots.api.recipe.WorldRecipe;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.api.test.world.PartialBlockState;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.recipe.SimpleWorldCrafting;
import mysticmods.roots.recipe.bark.OutputStateMapper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RunicBlockRecipe extends WorldRecipe<SimpleWorldCrafting> {
  public static MapCodec<RunicBlockRecipe> CODEC = RecordCodecBuilder.mapCodec(
      (instance) -> instance.group(
          BaseRecipeData.CODEC.fieldOf("data").forGetter((o) -> o.data),
          PartialBlockState.CODEC.optionalFieldOf("outputState").forGetter((o) -> Optional.ofNullable(o.outputState)),
          WorldCondition.LIST_CODEC.fieldOf("condition").forGetter(o -> o.conditions),
          OutputStateMapper.CODEC.optionalFieldOf("stateMapper").forGetter(o -> Optional.ofNullable(o.stateMapper)),
          Codec.STRING.listOf().optionalFieldOf("skipProperties").forGetter((o) -> o.skipProperties.isEmpty() ? Optional.empty() : Optional.of(o.skipProperties)),
          Codec.INT.fieldOf("durabilityCost").forGetter((o) -> o.durabilityCost)
      ).apply(instance, RunicBlockRecipe::new)
  );
  public static StreamCodec<RegistryFriendlyByteBuf, RunicBlockRecipe> STREAM_CODEC = StreamCodec.composite(
      BaseRecipeData.STREAM_CODEC, o -> o.data,
      ByteBufCodecs.optional(PartialBlockState.STREAM_CODEC), o -> Optional.ofNullable(o.outputState),
      WorldCondition.LIST_STREAM_CODEC, o -> o.conditions,
      ByteBufCodecs.optional(OutputStateMapper.STREAM_CODEC), o -> Optional.ofNullable(o.stateMapper),
      ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list())), o -> o.skipProperties.isEmpty() ? Optional.empty() : Optional.of(o.skipProperties),
      ByteBufCodecs.VAR_INT, o -> o.durabilityCost,
      RunicBlockRecipe::new
  );

  protected int durabilityCost = 1;
  @Nullable
  private OutputStateMapper stateMapper;

  public RunicBlockRecipe() {
    super();
  }

  public RunicBlockRecipe(BaseRecipeData baseRecipeData, PartialBlockState partialBlockState1, List<WorldCondition> worldCondition, OutputStateMapper outputStateMapper, List<String> strings, int durabilityCost) {
    super(baseRecipeData, partialBlockState1, worldCondition, strings);
    this.stateMapper = outputStateMapper;
    this.durabilityCost = durabilityCost;
  }

  public RunicBlockRecipe(BaseRecipeData baseRecipeData, Optional<PartialBlockState> partialBlockState1, List<WorldCondition> worldCondition, Optional<OutputStateMapper> outputStateMapper, Optional<List<String>> strings, int durabilityCost) {
    super(baseRecipeData, partialBlockState1.orElse(null), worldCondition, strings.orElse(Collections.emptyList()));
    this.stateMapper = outputStateMapper.orElse(null);
    this.durabilityCost = durabilityCost;
  }

  public int getDurabilityCost() {
    return durabilityCost;
  }

  // TODO:
  public List<String> getSkipProperties() {
    return skipProperties;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.RUNIC_BLOCK.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.RUNIC_BLOCK.get();
  }

  @Override
  public String getGroup() {
    return Identifiers.RUNIC_BLOCK_RECIPE_GROUP;
  }

  public static class Serializer implements RecipeSerializer<RunicBlockRecipe> {
    @Override
    public MapCodec<RunicBlockRecipe> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, RunicBlockRecipe> streamCodec() {
      return STREAM_CODEC;
    }
  }

  public static class Builder {
    private final List<String> skipProperties = new ArrayList<>();
    private int durabilityCost;
    private PartialBlockState outputState;
    private final List<WorldCondition> condition = new ArrayList<>();
    private OutputStateMapper stateMapper;

    protected Builder() {
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

    public RunicBlockRecipe build(BaseRecipeData data) {
      return new RunicBlockRecipe(data, outputState, condition, stateMapper, skipProperties, durabilityCost);
    }

    public RunicBlockRecipe build(BaseRecipeData.Builder data) {
      return build(data.build());
    }

    public static Builder create() {
      return new Builder();
    }
  }
}
