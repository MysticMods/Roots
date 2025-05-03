package mysticmods.roots.recipe.grove;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.recipe.BaseRecipeData;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.blockentity.GroveCrafterBlockEntity;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModAttributes;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.item.PouchItem;
import mysticmods.roots.recipe.PedestalInventoryWrapper;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class GrovePouchRecipe extends GroveRecipe {
  public static MapCodec<GrovePouchRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
      BaseRecipeData.CODEC.fieldOf("data").forGetter((o) -> o.data)
  ).apply(instance, GrovePouchRecipe::new));
  public static StreamCodec<RegistryFriendlyByteBuf, GrovePouchRecipe> STREAM_CODEC = StreamCodec.composite(
      BaseRecipeData.STREAM_CODEC, o -> o.data,
      GrovePouchRecipe::new
  );

  public GrovePouchRecipe(BaseRecipeData data) {
    super(data);
  }

  @Override
  public ItemStack assemble(GroveCrafting arg, HolderLookup.Provider arg2) {
    ItemStack pouch = ItemStack.EMPTY;
    for (int i = 0; i < arg.size(); i++) {
      ItemStack item = arg.getItem(i);
      if (item.is(RootsTags.Items.POUCHES)) {
        pouch = item.copy();
        break;
      }
    }

    ItemStack result = super.assemble(arg, arg2);

    ItemContainerContents contents = pouch.get(((PouchItem) pouch.getItem()).getComponent());
    result.set(((PouchItem) result.getItem()).getComponent(), contents);

    return result;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.GROVE_POUCH_CRAFTING.get();
  }

  public static class Serializer implements RecipeSerializer<GrovePouchRecipe> {
    @Override
    public MapCodec<GrovePouchRecipe> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, GrovePouchRecipe> streamCodec() {
      return STREAM_CODEC;
    }
  }

  public static class Builder {
    protected Builder() {
    }

    public GrovePouchRecipe build(BaseRecipeData data) {
      return new GrovePouchRecipe(data);
    }

    public GrovePouchRecipe build(BaseRecipeData.Builder data) {
      return build(data.build());
    }

    public static Builder create() {
      return new Builder();
    }
  }
}
