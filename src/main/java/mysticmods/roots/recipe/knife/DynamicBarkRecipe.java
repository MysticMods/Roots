package mysticmods.roots.recipe.knife;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.recipe.BaseRecipeData;
import mysticmods.roots.api.recipe.WorldCondition;
import mysticmods.roots.api.test.world.AlwaysTrueWorldTest;
import mysticmods.roots.api.test.world.PartialBlockState;
import mysticmods.roots.api.test.world.TagMatchWorldTest;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.recipe.SimpleWorldCrafting;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class DynamicBarkRecipe extends KnifeRecipe {
  public static DynamicBarkRecipe INSTANCE = new DynamicBarkRecipe();

  public static ResourceLocation IDENTIFIER = RootsAPI.rl("bark/dynamic_modded_wood_bark");

  public DynamicBarkRecipe() {
    // TODO: Should this be "AlwaysTrueTest"?
    super(new BaseRecipeData(), null, new PartialBlockState(Blocks.AIR), Collections.emptyList(), Collections.emptyList(), 1);
  }

  private ItemStack newResult = null;

  @Override
  public ItemStack getResultItem(HolderLookup.Provider provider) {
    if (newResult == null) {
      newResult = new ItemStack(ModItems.MIXED_BARK.get(), 2);
    }
    return newResult;
  }

  private WorldCondition barkCondition = null;

  @Override
  public List<WorldCondition> getConditions() {
    if (barkCondition == null) {
      barkCondition = new WorldCondition(new TagMatchWorldTest(BlockTags.LOGS));
    }
    return List.of(barkCondition);
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.DYNAMIC_BARK.get();
  }

  @Override
  public boolean matches(SimpleWorldCrafting pContainer, Level pLevel) {
    return super.matches(pContainer, pLevel) && getStrippedState(pContainer, pContainer.getBlockState()) != null;
  }

  @Override
  public int getPriority() {
    return -1000;
  }

  @Nullable
  protected static BlockState getStrippedState(SimpleWorldCrafting pContainer, BlockState state) {
    BlockState outputState = state.getToolModifiedState(pContainer.getContext(), ItemAbilities.AXE_STRIP, false);
    if (outputState == null) {
      outputState = AxeItem.getAxeStrippingState(state);
    }
    return outputState;
  }

  @Override
  public BlockState modifyState(SimpleWorldCrafting pContainer, BlockState currentState, HolderLookup.Provider provider) {
    BlockState outputState = getStrippedState(pContainer, currentState);
    if (outputState == null) {
      return currentState;
    }

    return outputState;
  }

  @Override
  public boolean isDynamic() {
    return true;
  }

  public static class Serializer implements RecipeSerializer<DynamicBarkRecipe> {
    @Override
    public MapCodec<DynamicBarkRecipe> codec() {
      return MapCodec.unit(INSTANCE);
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, DynamicBarkRecipe> streamCodec() {
      return StreamCodec.unit(INSTANCE);
    }
  }
}
