package mysticmods.roots.recipe.knife;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.recipe.BaseRecipeData;
import mysticmods.roots.api.recipe.WorldCondition;
import mysticmods.roots.api.test.world.PartialBlockState;
import mysticmods.roots.api.test.world.WorldTest;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.recipe.SimpleWorldCrafting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class KnifeOffHandRecipe extends KnifeRecipe {
  public static MapCodec<KnifeOffHandRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
      BaseRecipeData.CODEC.codec().optionalFieldOf("data")
          .forGetter(o -> o.data.isEmpty() ? Optional.empty() : Optional.of(o.data)),
      TagKey.codec(Registries.ITEM).fieldOf("offHandTag").forGetter(o -> o.offHandTag),
      WorldTest.CODEC.optionalFieldOf("test").forGetter(o -> Optional.ofNullable(o.test)),
      PartialBlockState.CODEC.optionalFieldOf("outputState").forGetter((o) -> Optional.ofNullable(o.outputState)),
      WorldCondition.LIST_CODEC.fieldOf("condition").forGetter(o -> o.conditions),
      Codec.STRING.listOf().optionalFieldOf("skipProperties")
          .forGetter((o) -> o.skipProperties.isEmpty() ? Optional.empty() : Optional.of(o.skipProperties)),
      Codec.INT.fieldOf("durabilityCost").forGetter((o) -> o.durabilityCost),
      OutputStateMapper.CODEC.optionalFieldOf("stateMapper").forGetter(o -> Optional.ofNullable(o.stateMapper))
  ).apply(instance, KnifeOffHandRecipe::new));
  public static StreamCodec<RegistryFriendlyByteBuf, KnifeOffHandRecipe> STREAM_CODEC = ExtraStreamCodecs.composite(
      BaseRecipeData.STREAM_CODEC, o -> o.data,
      ExtraStreamCodecs.ITEM_TAG_STREAM_CODEC, o -> o.offHandTag,
      ByteBufCodecs.optional(WorldTest.STREAM_CODEC), o -> Optional.ofNullable(o.test),
      ByteBufCodecs.optional(PartialBlockState.STREAM_CODEC), o -> Optional.ofNullable(o.outputState),
      WorldCondition.LIST_STREAM_CODEC, o -> o.conditions,
      ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list())), o -> o.skipProperties.isEmpty() ? Optional.empty() : Optional.of(o.skipProperties),
      ByteBufCodecs.VAR_INT, o -> o.durabilityCost,
      ByteBufCodecs.optional(OutputStateMapper.STREAM_CODEC), o -> Optional.ofNullable(o.stateMapper),
      KnifeOffHandRecipe::new
  );


  private final TagKey<Item> offHandTag;

  public KnifeOffHandRecipe(BaseRecipeData data, TagKey<Item> offHandTag, WorldTest test, PartialBlockState outputState, List<WorldCondition> condition, List<String> skipProperties, int durabilityCost) {
    super(data, test, outputState, condition, skipProperties, durabilityCost);
    this.offHandTag = offHandTag;
  }

  public KnifeOffHandRecipe(BaseRecipeData data, TagKey<Item> offHandTag, WorldTest test, PartialBlockState outputState, List<WorldCondition> condition, List<String> skipProperties, int durabilityCost, OutputStateMapper stateMapper) {
    super(data, test, outputState, condition, skipProperties, durabilityCost, stateMapper);
    this.offHandTag = offHandTag;
  }

  public KnifeOffHandRecipe(BaseRecipeData baseRecipeData, TagKey<Item> offHandTag, Optional<WorldTest> test, Optional<PartialBlockState> partialBlockState, List<WorldCondition> worldCondition, Optional<List<String>> skipProperties, int durabilityCost, Optional<OutputStateMapper> stateMapper) {
    super(baseRecipeData, test, partialBlockState, worldCondition, skipProperties, durabilityCost, stateMapper);
    this.offHandTag = offHandTag;
  }

  public KnifeOffHandRecipe(Optional<BaseRecipeData> baseRecipeData, TagKey<Item> offHandTag, Optional<WorldTest> test, Optional<PartialBlockState> partialBlockState, List<WorldCondition> worldCondition, Optional<List<String>> strings, int durabilityCost, Optional<OutputStateMapper> outputStateMapper) {
    super(baseRecipeData, test, partialBlockState, worldCondition, strings, durabilityCost, outputStateMapper);
    this.offHandTag = offHandTag;
  }

  @Override
  public boolean matches(SimpleWorldCrafting pContainer, Level pLevel) {
    Player player = pContainer.getPlayer();
    if (player == null) {
      return false;
    }
    if (!player.getOffhandItem().is(offHandTag)) {
      return false;
    }
    return super.matches(pContainer, pLevel);
  }

  public TagKey<Item> getOffHandTag() {
    return offHandTag;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.KNIFE_OFF_HAND.get();
  }

  public static class Serializer implements RecipeSerializer<KnifeOffHandRecipe> {

    @Override
    public MapCodec<KnifeOffHandRecipe> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, KnifeOffHandRecipe> streamCodec() {
      return STREAM_CODEC;
    }
  }

  public static class Builder {
    private PartialBlockState outputState;
    private WorldTest test;
    private List<WorldCondition> condition = new ArrayList<>();
    private OutputStateMapper stateMapper;
    private int durabilityCost = 1;
    private final List<String> skipProperties = new ArrayList<>();
    private TagKey<Item> offHandTag;

    protected Builder() {
    }

    protected Builder(PartialBlockState outputState, List<WorldCondition> condition, OutputStateMapper stateMapper) {
      this.outputState = outputState;
      this.condition = condition;
      this.stateMapper = stateMapper;
    }

    public Builder tag(TagKey<Item> offHandTag) {
      this.offHandTag = offHandTag;
      return this;
    }

    public Builder test(WorldTest test) {
      this.test = test;
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

    public KnifeOffHandRecipe build(BaseRecipeData data) {
      if (outputState == null && (stateMapper == null || stateMapper.isEmpty() || test == null) && offHandTag == null) {
        throw new IllegalStateException("Cannot build a bark recipe without an output state or test or state mapper");
      }
      return new KnifeOffHandRecipe(data, offHandTag, test, outputState, condition, skipProperties, durabilityCost, stateMapper);
    }

    public KnifeRecipe build(BaseRecipeData.Builder data) {
      return build(data.build());
    }

    public static Builder create() {
      return new Builder();
    }
  }
}
