package epicsquid.roots.integration.jei.transmutation;

import epicsquid.mysticallib.util.CycleTimer;
import epicsquid.roots.Roots;
import epicsquid.roots.recipe.TransmutationRecipe;
import epicsquid.roots.recipe.TransmutationRecipe.*;
import epicsquid.roots.util.RenderUtil;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class TransmutationWrapper implements IRecipeWrapper {
  public final TransmutationRecipe recipe;
  private CycleTimer timer = new CycleTimer(-1);

  public TransmutationWrapper(TransmutationRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    ItemStack result = recipe.getStack();
    IBlockState outputState = recipe.getState().orElse(null);
    WorldBlockStatePredicate condition = recipe.getCondition();
    BlockStatePredicate input = recipe.getStartPredicate();

    // Handle the input side
    IBlockState initial = timer.getCycledItem(input.matchingStates());
    if (initial != null) {
      RenderUtil.renderBlock(initial, 26, 47, 10, 20f, 0.4f);
    }

    if (condition != WorldBlockStatePredicate.TRUE) {
      IBlockState cond = timer.getCycledItem(condition.matchingStates());
      if (cond != null) {
        try {
          RenderUtil.renderBlock(cond, 26, 65, -10, 20f, 0.4f);
        } catch (NullPointerException e) {
          Roots.logger.error("WTF", e);
        }
      }
    }

    if (outputState != null) {
      RenderUtil.renderBlock(outputState, 130, 47, 10, 20f, 0.4f);
    } else {
      RenderHelper.enableGUIStandardItemLighting();
      int i3 = 126;
      int j3 = 32;
      Minecraft mc = Minecraft.getMinecraft();
      mc.getRenderItem().renderItemIntoGUI(result, i3, j3);
      mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, result, i3, j3, null);
      RenderHelper.disableStandardItemLighting();
    }
  }
}
