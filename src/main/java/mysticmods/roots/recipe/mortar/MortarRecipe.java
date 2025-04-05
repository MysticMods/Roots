package mysticmods.roots.recipe.mortar;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.api.recipe.BaseRecipeData;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.blockentity.MortarBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

public class MortarRecipe extends RootsTileRecipe<MortarInventory, MortarBlockEntity, MortarCrafting> {
  public static MapCodec<MortarRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
      BaseRecipeData.CODEC.fieldOf("data").forGetter(o -> o.data),
      Codec.INT.fieldOf("times").forGetter(o -> o.times)
  ).apply(instance, MortarRecipe::new));
  public static StreamCodec<RegistryFriendlyByteBuf, MortarRecipe> STREAM_CODEC = StreamCodec.composite(
      BaseRecipeData.STREAM_CODEC, o -> o.data,
      ByteBufCodecs.VAR_INT, o -> o.times,
      MortarRecipe::new
  );

  private final int times;

  public MortarRecipe(BaseRecipeData data, int times) {
    super(data);
    this.times = times;
  }

  public int getTimes() {
    return times;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.MORTAR.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.MORTAR.get();
  }

  @Override
  public String getGroup() {
    return Identifiers.MORTAR_RECIPE_GROUP;
  }

  @Override
  public void buildCachedOutputs(List<ChanceOutput> list, HolderLookup.Provider provider) {
    if (getResultItem(provider).isEmpty() && !getUnlocks().isEmpty()) {
      Unlock<?> unlock = getUnlocks().getFirst();
      if (unlock instanceof Unlock.SpellUnlock spellUnlock) {
        list.add(new ChanceOutput(spellUnlock.getIcon(), 1));
      }
    }
    super.buildCachedOutputs(list, provider);
  }

  public static class Serializer implements RecipeSerializer<MortarRecipe> {

    @Override
    public MapCodec<MortarRecipe> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, MortarRecipe> streamCodec() {
      return STREAM_CODEC;
    }
  }

  public static class Builder {
    private int times;

    protected Builder() {
    }

    public static Builder create() {
      return new Builder();
    }

    public Builder times(int times) {
      this.times = times;
      return this;
    }

    public MortarRecipe build(BaseRecipeData data) {
      return new MortarRecipe(data, times);
    }

    public MortarRecipe build(BaseRecipeData.Builder data) {
      return build(data.build());
    }
  }
}
