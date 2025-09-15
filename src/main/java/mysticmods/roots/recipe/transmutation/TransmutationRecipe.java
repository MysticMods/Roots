package mysticmods.roots.recipe.transmutation;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.grove.GroveNumber;
import mysticmods.roots.api.recipe.BaseRecipeData;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.blockentity.FungalTransmuterBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class TransmutationRecipe extends RootsTileRecipe<TransmutationInventory, FungalTransmuterBlockEntity, TransmutationCrafting> {
  public static final MapCodec<TransmutationRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
      BaseRecipeData.CODEC.fieldOf("data").forGetter((o) -> o.data)
  ).apply(instance, TransmutationRecipe::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, TransmutationRecipe> STREAM_CODEC = StreamCodec.composite(BaseRecipeData.STREAM_CODEC, o -> o.data,
      TransmutationRecipe::new);

  private int cachedPowerRequirement = -1;

  public TransmutationRecipe(BaseRecipeData data) {
    super(data);
    cachedPowerRequirement = 0;
    for (GroveNumber number : data.powerRequirements) {
      if (number.grove().is(RootsTags.Groves.FUNGAL)) {
        cachedPowerRequirement += number.value();
      }
    }
    if (cachedPowerRequirement == 0) {
      cachedPowerRequirement = -1;
    }
  }

  public int getPower () {
    return cachedPowerRequirement;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.TRANSMUTATION.get();
  }

  @Override
  public String getGroup() {
    return Identifiers.TRANSMUTATION_RECIPE_GROUP;
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.TRANSMUTATION.get();
  }

  public static Builder create() {
    return new Builder();
  }

  public static class Builder {
    public TransmutationRecipe build(BaseRecipeData.Builder data) {
      return new TransmutationRecipe(data.build());
    }
  }

  public static class Serializer implements RecipeSerializer<TransmutationRecipe> {
    @Override
    public MapCodec<TransmutationRecipe> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, TransmutationRecipe> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
