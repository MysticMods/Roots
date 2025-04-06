package mysticmods.roots.recipe.pyre;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.recipe.BaseRecipeData;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.recipe.PedestalInventoryWrapper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;
import java.util.Optional;

// TODO
public class SummonCreaturesRecipe extends RootsTileRecipe<PedestalInventoryWrapper, PyreBlockEntity, PyrePedestalCrafting> {
  public static MapCodec<SummonCreaturesRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
      BaseRecipeData.CODEC.fieldOf("data").forGetter((o) -> o.data),
      BuiltInRegistries.ENTITY_TYPE.byNameCodec().optionalFieldOf("entity").forGetter((o) -> Optional.ofNullable(o.entity))
  ).apply(instance, SummonCreaturesRecipe::new));
  public static StreamCodec<RegistryFriendlyByteBuf, SummonCreaturesRecipe> STREAM_CODEC = StreamCodec.composite(
      BaseRecipeData.STREAM_CODEC, o -> o.data,
      ByteBufCodecs.optional(ByteBufCodecs.registry(Registries.ENTITY_TYPE)), o -> Optional.ofNullable(o.entity),
      SummonCreaturesRecipe::new
  );

  private final EntityType<?> entity;

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public SummonCreaturesRecipe(BaseRecipeData data, Optional<EntityType<?>> entity) {
    super(data);
    this.entity = entity.orElse(null);
  }

  @Nullable
  public EntityType<?> getEntity () {
    return entity;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.SUMMON_CREATURES.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.SUMMON_CREATURES.get();
  }

  @Override
  public String getGroup() {
    return Identifiers.PYRE_RECIPE_GROUP; // TODO: Change to Pyre Pedestal Group
  }

  public static class Serializer implements RecipeSerializer<SummonCreaturesRecipe> {

    @Override
    public MapCodec<SummonCreaturesRecipe> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, SummonCreaturesRecipe> streamCodec() {
      return STREAM_CODEC;
    }
  }

  public static class Builder {
    private EntityType<?> entity;

    protected Builder() {
    }

    protected Builder(EntityType<?> entity) {
      this.entity = entity;
    }

    public SummonCreaturesRecipe build(BaseRecipeData data) {
      return new SummonCreaturesRecipe(data, Optional.ofNullable(entity));
    }

    public SummonCreaturesRecipe build(BaseRecipeData.Builder data) {
      return build(data.build());
    }

    public static Builder create() {
      return new Builder();
    }
  }
}