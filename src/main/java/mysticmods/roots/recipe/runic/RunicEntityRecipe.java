package mysticmods.roots.recipe.runic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.recipe.BaseRecipeData;
import mysticmods.roots.api.recipe.EntityRecipe;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.test.entity.EntityTest;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class RunicEntityRecipe extends EntityRecipe<RunicEntityCrafting> {
  public static final MapCodec<RunicEntityRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
      BaseRecipeData.CODEC.fieldOf("data").forGetter((o) -> o.data),
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

  public RunicEntityRecipe() {
    super();
  }

  public RunicEntityRecipe(BaseRecipeData data, EntityTest test, int cooldown, int durabilityCost) {
    super(data, test);
    this.cooldown = cooldown;
    this.durabilityCost = durabilityCost;
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
}
