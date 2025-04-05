package mysticmods.roots.recipe.grove;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.recipe.BaseRecipeData;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.blockentity.GroveCrafterBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.recipe.PedestalInventoryWrapper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class GroveRecipe extends RootsTileRecipe<PedestalInventoryWrapper, GroveCrafterBlockEntity, GroveCrafting> {
  public static MapCodec<GroveRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
      BaseRecipeData.CODEC.fieldOf("data").forGetter((o) -> o.data)
  ).apply(instance, GroveRecipe::new));
  public static StreamCodec<RegistryFriendlyByteBuf, GroveRecipe> STREAM_CODEC = StreamCodec.composite(
      BaseRecipeData.STREAM_CODEC, o -> o.data,
      GroveRecipe::new
  );

  public GroveRecipe(BaseRecipeData data) {
    super(data);
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.GROVE_CRAFTING.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.GROVE.get();
  }

  @Override
  public String getGroup() {
    return Identifiers.GROVE_RECIPE_GROUP;
  }

  public static class Serializer implements RecipeSerializer<GroveRecipe> {

    @Override
    public MapCodec<GroveRecipe> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, GroveRecipe> streamCodec() {
      return STREAM_CODEC;
    }
  }

  public static class Builder {
    protected Builder() {
    }

    public static Builder create() {
      return new Builder();
    }

    public GroveRecipe build(BaseRecipeData data) {
      return new GroveRecipe(data);
    }

    public GroveRecipe build(BaseRecipeData.Builder data) {
      return build(data.build());
    }
  }
}
