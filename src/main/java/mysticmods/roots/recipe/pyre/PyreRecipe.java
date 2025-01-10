package mysticmods.roots.recipe.pyre;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.recipe.BaseRecipeData;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class PyreRecipe extends RootsTileRecipe<PyreInventory, PyreBlockEntity, PyreCrafting> {
  public static MapCodec<PyreRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
      BaseRecipeData.CODEC.fieldOf("data").forGetter((o) -> o.data),
      RootsRegistries.RITUALS.byNameCodec().fieldOf("ritual").forGetter((o) -> o.ritual)
  ).apply(instance, PyreRecipe::new));
  public static StreamCodec<RegistryFriendlyByteBuf, PyreRecipe> STREAM_CODEC = StreamCodec.composite(
      BaseRecipeData.STREAM_CODEC, o -> o.data,
      ByteBufCodecs.registry(RootsRegistries.Keys.RITUALS), o -> o.ritual,
      PyreRecipe::new
  );

  private Ritual ritual;

  public PyreRecipe() {
    super();
  }

  public PyreRecipe(BaseRecipeData data, Ritual ritual) {
    super(data);
    this.ritual = ritual;
  }

  public Ritual getRitual() {
    return ritual;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.PYRE.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.PYRE.get();
  }

  @Override
  public String getGroup() {
    return Identifiers.PYRE_RECIPE_GROUP;
  }

  public static class Serializer implements RecipeSerializer<PyreRecipe> {
    @Override
    public MapCodec<PyreRecipe> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, PyreRecipe> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
