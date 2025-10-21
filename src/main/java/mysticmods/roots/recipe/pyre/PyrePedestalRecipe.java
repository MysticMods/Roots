package mysticmods.roots.recipe.pyre;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.recipe.BaseRecipeData;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.recipe.PedestalInventoryWrapper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Optional;

public abstract class PyrePedestalRecipe extends RootsTileRecipe<PedestalInventoryWrapper, PyreBlockEntity, PyrePedestalCrafting> {
  public static final Codec<PyrePedestalRecipeHolder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      ResourceLocation.CODEC.fieldOf("id").forGetter(PyrePedestalRecipeHolder::id),
      Recipe.CODEC.optionalFieldOf("recipe").forGetter(o -> Optional.ofNullable(o.value))
  ).apply(instance, PyrePedestalRecipeHolder::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, PyrePedestalRecipeHolder> STREAM_CODEC = StreamCodec.composite(
      ResourceLocation.STREAM_CODEC, PyrePedestalRecipeHolder::id,
      ByteBufCodecs.optional(Recipe.STREAM_CODEC), o -> Optional.ofNullable(o.value()),
      PyrePedestalRecipeHolder::new
  );
  public static final PyrePedestalRecipeHolder NULL = new PyrePedestalRecipeHolder(ResourceLocation.fromNamespaceAndPath("i", "invalid"), (PyrePedestalRecipe) null);

  public PyrePedestalRecipe(BaseRecipeData data) {
    super(data);
  }

  public static <T extends PyrePedestalRecipe> PyrePedestalRecipeHolder of (RecipeHolder<T> holder) {
    return new PyrePedestalRecipeHolder(holder.id(), holder.value());
  }

  public record PyrePedestalRecipeHolder(ResourceLocation id, PyrePedestalRecipe value) {
    public PyrePedestalRecipeHolder(ResourceLocation id, Recipe<?> recipe) {
      this(id, (PyrePedestalRecipe) recipe);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public PyrePedestalRecipeHolder(ResourceLocation id, Optional<Recipe<?>> recipe) {
      this(id, recipe.orElse(null));
    }

    public boolean isEmpty() {
      return value == null;
    }
  }
}
