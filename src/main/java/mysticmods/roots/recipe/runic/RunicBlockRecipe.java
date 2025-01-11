package mysticmods.roots.recipe.runic;

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
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RunicBlockRecipe extends WorldRecipe<SimpleWorldCrafting> {
  public static MapCodec<RunicBlockRecipe> CODEC = RecordCodecBuilder.mapCodec(
      (instance) -> instance.group(
          BaseRecipeData.CODEC.fieldOf("data").forGetter((o) -> o.data),
          PartialBlockState.CODEC.fieldOf("outputState").forGetter((o) -> o.outputState),
          WorldCondition.CODEC.fieldOf("condition").forGetter((o) -> o.condition),
          Codec.STRING.listOf().fieldOf("skipProperties").forGetter((o) -> o.skipProperties),
          Codec.INT.fieldOf("durabilityCost").forGetter((o) -> o.durabilityCost)
      ).apply(instance, RunicBlockRecipe::new)
  );
  public static StreamCodec<RegistryFriendlyByteBuf, RunicBlockRecipe> STREAM_CODEC = StreamCodec.composite(
      BaseRecipeData.STREAM_CODEC, o -> o.data,
      PartialBlockState.STREAM_CODEC, o -> o.outputState,
      WorldCondition.STREAM_CODEC, o -> o.condition,
      ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), o -> o.skipProperties,
      ByteBufCodecs.VAR_INT, o -> o.durabilityCost,
      RunicBlockRecipe::new
  );

  protected List<String> skipProperties;
  protected int durabilityCost = 1;

  public RunicBlockRecipe() {
    super();
    this.skipProperties = new ArrayList<>();
  }

  public RunicBlockRecipe(BaseRecipeData data, PartialBlockState outputState, WorldCondition condition, List<String> skipProperties, int durabilityCost) {
    super(data, outputState, condition);
    this.skipProperties = skipProperties;
    this.durabilityCost = durabilityCost;
  }

  public int getDurabilityCost() {
    return durabilityCost;
  }

  public List<String> getSkipProperties() {
    return skipProperties;
  }

  @Override
  @Nullable
  public BlockState modifyState(SimpleWorldCrafting pContainer, BlockState state, HolderLookup.Provider provider) {
    return outputState.copyState(state, getSkipProperties());
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
}
