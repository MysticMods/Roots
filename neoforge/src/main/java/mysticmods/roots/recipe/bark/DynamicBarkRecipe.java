package mysticmods.roots.recipe.bark;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.recipe.WorldRecipe;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.neoforge.common.ItemAbilities;

import javax.annotation.Nullable;

public class DynamicBarkRecipe extends BarkRecipe {
  public static DynamicBarkRecipe INSTANCE = new DynamicBarkRecipe(RootsAPI.rl("bark/dynamic_modded_wood_bark"));

  public DynamicBarkRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }

  private ItemStack newResult = null;

  @Override
  public ItemStack getResultItem(HolderLookup.Provider provider) {
    if (newResult == null) {
      newResult = new ItemStack(ModItems.MIXED_BARK.get(), 2);
    }
    return newResult;
  }

  private WorldRecipe.Condition barkCondition = null;

  @Override
  public Condition getCondition() {
    if (barkCondition == null) {
      barkCondition = new WorldRecipe.Condition(new TagMatchTest(BlockTags.LOGS));
    }
    return barkCondition;
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
  public BlockState modifyState(SimpleWorldCrafting pContainer, BlockState currentState) {
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
