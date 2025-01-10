package mysticmods.roots.recipe.bark;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BarkRecipe extends WorldRecipe<SimpleWorldCrafting> {
  public static MapCodec<BarkRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
    BaseRecipeData.CODEC.fieldOf("data").forGetter((o) -> o.data),
    PartialBlockState.CODEC.fieldOf("outputState").forGetter((o) -> o.outputState),
    WorldCondition.CODEC.fieldOf("condition").forGetter((o) -> o.condition)
  ).apply(instance, BarkRecipe::new));
  public static StreamCodec<RegistryFriendlyByteBuf, BarkRecipe> STREAM_CODEC = StreamCodec.composite(
    BaseRecipeData.STREAM_CODEC, o -> o.data,
    PartialBlockState.STREAM_CODEC, o -> o.outputState,
    WorldCondition.STREAM_CODEC, o -> o.condition,
    BarkRecipe::new
  );

  public BarkRecipe() {
    super();
  }

  public BarkRecipe(BaseRecipeData data, PartialBlockState outputState, WorldCondition condition) {
    super(data, outputState, condition);
  }

  @Override
  public BlockState modifyState(SimpleWorldCrafting pContainer, BlockState currentState, HolderLookup.Provider provider) {
    BlockState newState = outputState.build();

    if (currentState.getBlock() instanceof RotatedPillarBlock && outputState.getBlock() instanceof RotatedPillarBlock) {
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
}
