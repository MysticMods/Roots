package mysticmods.roots.recipe.bark;

import com.google.gson.JsonObject;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.recipe.WorldRecipe;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.recipe.SimpleWorldCrafting;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nullable;

public class DynamicBarkRecipe extends BarkRecipe {
  public static DynamicBarkRecipe INSTANCE = new DynamicBarkRecipe(new ResourceLocation(RootsAPI.MODID, "bark/dynamic_modded_wood_bark"));

  public DynamicBarkRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }

  private ItemStack newResult = null;

  @Override
  public ItemStack getResultItem() {
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
  protected static BlockState getStrippedState (SimpleWorldCrafting pContainer, BlockState state) {
    BlockState outputState = state.getToolModifiedState(pContainer.getContext(), ToolActions.AXE_STRIP, false);
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

  public static class Serializer extends WorldRecipe.Serializer<SimpleWorldCrafting, BarkRecipe> {

    public Serializer() {
      super(BarkRecipe::new);
    }

    @Override
    public BarkRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
      return INSTANCE;
    }

    @Nullable
    @Override
    public BarkRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
      return INSTANCE;
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, BarkRecipe recipe) {
    }
  }

  public static class Result implements FinishedRecipe {
    @Override
    public void serializeRecipeData(JsonObject pJson) {
    }

    @Override
    public ResourceLocation getId() {
      return INSTANCE.getId();
    }

    @Override
    public RecipeSerializer<?> getType() {
      return ModSerializers.DYNAMIC_BARK.get();
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public JsonObject serializeAdvancement() {
      return null;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public ResourceLocation getAdvancementId() {
      return null;
    }
  }
}
