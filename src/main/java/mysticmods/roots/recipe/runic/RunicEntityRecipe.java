package mysticmods.roots.recipe.runic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.recipe.BaseRecipeData;
import mysticmods.roots.api.recipe.EntityRecipe;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.api.test.entity.EntityTest;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Optional;

public class RunicEntityRecipe extends EntityRecipe<RunicEntityCrafting> {
  public static final MapCodec<RunicEntityRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
      BaseRecipeData.CODEC.codec().optionalFieldOf("data").forGetter((o) -> o.data.isEmpty() ? Optional.empty() : Optional.of(o.data)),
      EntityTest.CODEC.fieldOf("test").forGetter((o) -> o.test),
      Codec.INT.fieldOf("cooldown").forGetter((o) -> o.cooldown),
      Codec.INT.fieldOf("durabilityCost").forGetter((o) -> o.durabilityCost)
  ).apply(instance, RunicEntityRecipe::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, RunicEntityRecipe> STREAM_CODEC = StreamCodec.composite(
      BaseRecipeData.STREAM_CODEC, o -> o.data,
      EntityTest.STREAM_CODEC, o -> o.test,
      ByteBufCodecs.VAR_INT, o -> o.cooldown,
      ByteBufCodecs.VAR_INT, o -> o.durabilityCost,
      RunicEntityRecipe::new
  );

  private int cooldown;
  private int durabilityCost = 1;

  public RunicEntityRecipe(BaseRecipeData data, EntityTest test, int cooldown, int durabilityCost) {
    super(data, test);
    this.cooldown = cooldown;
    this.durabilityCost = durabilityCost;
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public RunicEntityRecipe(Optional<BaseRecipeData> baseRecipeData, EntityTest entityTest, int cooldown, int durabilityCost) {
    this(baseRecipeData.orElseGet(BaseRecipeData::new), entityTest, cooldown, durabilityCost);
  }

  @Override
  public void modifyEntity(RunicEntityCrafting pContainer, HolderLookup.Provider provider) {

  }

  public int getCooldown() {
    return cooldown;
  }

  public int getDurabilityCost() {
    return durabilityCost;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.RUNIC_ENTITY.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.RUNIC_ENTITY.get();
  }

  @Override
  public String getGroup() {
    return Identifiers.RUNIC_ENTITY_RECIPE_GROUP;
  }

  public static class Serializer implements RecipeSerializer<RunicEntityRecipe> {

    @Override
    public MapCodec<RunicEntityRecipe> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, RunicEntityRecipe> streamCodec() {
      return STREAM_CODEC;
    }
  }

  public static class Builder {
    private EntityTest test;
    private int cooldown;
    private int durabilityCost = 1;

    protected Builder() {
    }

    public Builder cooldown(int cooldown) {
      this.cooldown = cooldown;
      return this;
    }

    public Builder durabilityCost(int durabilityCost) {
      this.durabilityCost = durabilityCost;
      return this;
    }

    public Builder test(EntityTest test) {
      this.test = test;
      return this;
    }

    public RunicEntityRecipe build(BaseRecipeData data) {
      return new RunicEntityRecipe(data, test, cooldown, durabilityCost);
    }

    public RunicEntityRecipe build(BaseRecipeData.Builder data) {
      return build(data.build());
    }

    public static Builder create() {
      return new Builder();
    }
  }
}
