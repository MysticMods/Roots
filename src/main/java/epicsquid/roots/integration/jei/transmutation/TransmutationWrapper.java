package epicsquid.roots.integration.jei.transmutation;

import epicsquid.mysticallib.util.CycleTimer;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.TransmutationRecipe;
import epicsquid.roots.recipe.transmutation.BlockStatePredicate;
import epicsquid.roots.recipe.transmutation.StatePosition;
import epicsquid.roots.recipe.transmutation.WorldBlockStatePredicate;
import epicsquid.roots.util.RenderUtil;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class TransmutationWrapper implements IRecipeWrapper {
  public final TransmutationRecipe recipe;
  private static final CycleTimer timer = new CycleTimer(10);

  public TransmutationWrapper(TransmutationRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    if (recipe == null) {
      return;
    }

    ItemStack result = recipe.getStack();
    IBlockState outputState = recipe.getState().orElse(null);
    WorldBlockStatePredicate condition = recipe.getCondition();
    BlockStatePredicate input = recipe.getStartPredicate();

    IBlockState initial = timer.getCycledItem(input.matchingStates());
    IBlockState cond = timer.getCycledItem(condition.matchingStates());
    timer.onDraw();
    Minecraft mc = Minecraft.getMinecraft();

    if (initial != null && cond != null) {
      if (condition.getPosition() == StatePosition.BELOW) {
        RenderUtil.renderBlock(initial, 26, 47, 10, 20f, 0.4f);
        RenderUtil.renderBlock(cond, 26, 65, -10, 20f, 0.4f);
      } else {
        RenderUtil.renderBlock(cond, 26, 47, 10, 20f, 0.4f);
        RenderUtil.renderBlock(initial, 26, 65, -10, 20f, 0.4f);
      }
    } else if (initial != null) {
      RenderUtil.renderBlock(initial, 26, 57, 10, 20f, 0.4f);
    }

    PlayerEvent.ItemPickupEvent
    if (outputState != null && cond != null) {
      if (condition.getPosition() == StatePosition.BELOW) {
        RenderUtil.renderBlock(outputState, 130, 47, 10, 20f, 0.4f);
        RenderUtil.renderBlock(cond, 130, 65, -10, 20f, 0.4f);
      } else {
        RenderUtil.renderBlock(cond, 130, 47, 10, 20f, 0.4f);
        RenderUtil.renderBlock(outputState, 130, 65, -10, 20f, 0.4f);
      }
    } else if (outputState != null) {
      RenderUtil.renderBlock(outputState, 130, 57, 10, 20f, 0.4f);
    } else {
      RenderHelper.enableGUIStandardItemLighting();
      int i3 = 126;
      int j3 = 32;
      mc.getRenderItem().renderItemIntoGUI(result, i3, j3);
      mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, result, i3, j3, null);
      RenderHelper.disableStandardItemLighting();
    }

    RenderHelper.enableGUIStandardItemLighting();
    mc.getRenderItem().renderItemIntoGUI(new ItemStack(ModItems.ritual_transmutation), 74, 33);
    RenderHelper.disableStandardItemLighting();
  }
}
