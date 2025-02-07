package mysticmods.roots.recipe.summon;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.recipe.BaseRecipeData;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

// TODO: However this is going to work
public class SummonCreaturesRecipe extends RootsTileRecipe<SummonCreaturesInventory, PyreBlockEntity, SummonCreaturesCrafting> {
  public static MapCodec<SummonCreaturesRecipe> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          BaseRecipeData.CODEC.forGetter(SummonCreaturesRecipe::getData),
          BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("result").forGetter(SummonCreaturesRecipe::getEntity)
      ).apply(instance, SummonCreaturesRecipe::new)
  );
  public static StreamCodec<RegistryFriendlyByteBuf, SummonCreaturesRecipe> STREAM_CODEC = StreamCodec.composite(
      BaseRecipeData.STREAM_CODEC, SummonCreaturesRecipe::getData,
      ByteBufCodecs.registry(Registries.ENTITY_TYPE), SummonCreaturesRecipe::getEntity,
      SummonCreaturesRecipe::new
  );

  private final EntityType<?> result;

  public SummonCreaturesRecipe(BaseRecipeData data, EntityType<?> result) {
    super(data);
    this.result = result;
  }

  public EntityType<?> getEntity() {
    return result;
  }

  // TODO:
  @Override
  public boolean matches(SummonCreaturesCrafting pInv, Level pLevel) {
    return false;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.SUMMON_CREATURES.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.SUMMON_CREATURES.get();
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

    public Builder entity(EntityType<?> entity) {
      this.entity = entity;
      return this;
    }

    public SummonCreaturesRecipe build(BaseRecipeData data) {
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
