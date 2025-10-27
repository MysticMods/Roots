package mysticmods.roots.recipe.pyre;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.recipe.BaseRecipeData;
import mysticmods.roots.api.recipe.ComplexEntityType;
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
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;

public class SummonCreaturesRecipe extends PyrePedestalRecipe {
  public static MapCodec<SummonCreaturesRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
      BaseRecipeData.CODEC.fieldOf("data").forGetter((o) -> o.data),
      ComplexEntityType.CODEC.fieldOf("entity").forGetter(SummonCreaturesRecipe::getEntity)
  ).apply(instance, SummonCreaturesRecipe::new));
  public static StreamCodec<RegistryFriendlyByteBuf, SummonCreaturesRecipe> STREAM_CODEC = StreamCodec.composite(
      BaseRecipeData.STREAM_CODEC, o -> o.data,
      ComplexEntityType.STREAM_CODEC, o -> o.entity,
      SummonCreaturesRecipe::new
  );

  private final ComplexEntityType entity;

  public SummonCreaturesRecipe(BaseRecipeData data, ComplexEntityType entity) {
    super(data);
    this.entity = entity;
  }

  @NotNull
  public ComplexEntityType getEntity() {
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
    private ComplexEntityType entity;

    protected Builder() {
    }

    public Builder entity(EntityType<?> entity) {
      this.entity = new ComplexEntityType(entity, null);
      return this;
    }

    public Builder entity(ComplexEntityType entity) {
      this.entity = entity;
      return this;
    }

    public SummonCreaturesRecipe build(BaseRecipeData data) {
      if (entity == null) {
        throw new IllegalArgumentException("Entity cannot be null");
      }
      return new SummonCreaturesRecipe(data, entity);
    }

    public SummonCreaturesRecipe build(BaseRecipeData.Builder data) {
      return build(data.build());
    }

    public static Builder create() {
      return new Builder();
    }
  }
}