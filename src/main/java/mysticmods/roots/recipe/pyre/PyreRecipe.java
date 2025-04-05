package mysticmods.roots.recipe.pyre;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.recipe.BaseRecipeData;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class PyreRecipe extends RootsTileRecipe<PyreInventory, PyreBlockEntity, PyreCrafting> {
  public static MapCodec<PyreRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
      BaseRecipeData.CODEC.fieldOf("data").forGetter((o) -> o.data),
      RootsRegistries.RITUALS.byNameCodec().optionalFieldOf("ritual").forGetter((o) -> Optional.ofNullable(o.ritual))
  ).apply(instance, PyreRecipe::new));
  public static StreamCodec<RegistryFriendlyByteBuf, PyreRecipe> STREAM_CODEC = StreamCodec.composite(
      BaseRecipeData.STREAM_CODEC, o -> o.data,
      ByteBufCodecs.optional(ByteBufCodecs.registry(RootsRegistries.Keys.RITUALS)), o -> Optional.ofNullable(o.ritual),
      PyreRecipe::new
  );

  private final Ritual ritual;

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public PyreRecipe(BaseRecipeData data, Optional<Ritual> ritual) {
    super(data);
    this.ritual = ritual.orElse(null);
  }

  @Nullable
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

  @Override
  public void buildCachedOutputs(List<ChanceOutput> list, HolderLookup.Provider provider) {
    if (getResultItem(provider).isEmpty() && ritual != null) {
      list.add(new ChanceOutput(ritual.getIcon(), 1));
    }
    super.buildCachedOutputs(list, provider);
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

  public static class Builder {
    private Ritual ritual = null;

    protected Builder() {
    }

    public static Builder create() {
      return new Builder();
    }

    public Builder ritual(Ritual ritual) {
      this.ritual = ritual;
      return this;
    }

    public Builder ritual(Holder<Ritual> ritual) {
      return ritual(ritual.value());
    }

    public PyreRecipe build(BaseRecipeData data) {
      return new PyreRecipe(data, Optional.ofNullable(this.ritual));
    }

    public PyreRecipe build(BaseRecipeData.Builder data) {
      return build(data.build());
    }
  }
}
