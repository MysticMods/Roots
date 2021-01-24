package epicsquid.roots.client.hud;

import epicsquid.roots.recipe.PyreCraftingRecipe;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.tileentity.TileEntityPyre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class RenderPyre {
  public static void render(Minecraft mc, BlockPos pos, IBlockState state, RenderGameOverlayEvent.Post event) {
    World world = mc.world;
    if (!(world.getTileEntity(pos) instanceof TileEntityPyre)) return;

    TileEntityPyre te = (TileEntityPyre) world.getTileEntity(pos);
    if (te == null) return;

    ScaledResolution res = event.getResolution();

    int x = res.getScaledWidth() / 2;
    int y = res.getScaledHeight() / 4;

    PyreCraftingRecipe curRecipe = te.getCurrentRecipe();
    RitualBase curRitual = te.getCurrentRitual();
    PyreCraftingRecipe lastRecipe = te.getLastRecipeUsed();
    RitualBase lastRitual = te.getLastRitualUsed();

    if (curRecipe != null && curRecipe != lastRecipe) {
      ItemStack output = curRecipe.getResult();
      RenderHelper.enableGUIStandardItemLighting();
      mc.getRenderItem().renderItemIntoGUI(output, x + 40, y);
      if (output.getCount() > 1) {
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, output, x + 40, y, null);
      }
      RenderHelper.disableStandardItemLighting();
      mc.fontRenderer.drawStringWithShadow(I18n.format("roots.info.pyre.begin1"), x + 40, y + 22, 16777215);
      mc.fontRenderer.drawStringWithShadow(I18n.format("roots.info.pyre.begin3"), x + 40, y + 34, 16777215);
    } else if (curRitual != null && curRitual != lastRitual) {
      if (curRitual.getIcon() != null) {
        mc.getRenderItem().renderItemIntoGUI(new ItemStack(curRitual.getIcon()), x + 40, y);
      }
      String s = I18n.format("roots.ritual." + curRitual.getName() + ".name");
      mc.fontRenderer.drawStringWithShadow(curRitual.getFormat() + s, (float) x + 40, y + 22, 16777215);
      mc.fontRenderer.drawStringWithShadow(I18n.format("roots.info.pyre.begin1"), x + 40, y + 37, 16777215);
      mc.fontRenderer.drawStringWithShadow(I18n.format("roots.info.pyre.begin2"), x + 40, y + 49, 16777215);
    }

    y = y * 2;

    if (lastRecipe != null) {
      ItemStack output = lastRecipe.getResult();
      RenderHelper.enableGUIStandardItemLighting();
      mc.getRenderItem().renderItemIntoGUI(output, x + 40, y);
      if (output.getCount() > 1) {
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, output, x + 40, y, null);
      }
      RenderHelper.disableStandardItemLighting();
      mc.fontRenderer.drawStringWithShadow(I18n.format("roots.info.pyre.repeat1"), x + 40, y + 22, 16777215);
      mc.fontRenderer.drawStringWithShadow(I18n.format("roots.info.pyre.repeat2"), x + 40, y + 34, 16777215);
      mc.fontRenderer.drawStringWithShadow(I18n.format("roots.info.pyre.repeat4"), x + 40, y + 46, 16777215);
    } else if (lastRitual != null) {
      if (lastRitual.getIcon() != null) {
        mc.getRenderItem().renderItemIntoGUI(new ItemStack(lastRitual.getIcon()), x + 40, y);
      }
      String s = I18n.format("roots.ritual." + lastRitual.getName() + ".name");
      mc.fontRenderer.drawStringWithShadow(lastRitual.getFormat() + s, (float) x + 40, y + 22, 16777215);
      mc.fontRenderer.drawStringWithShadow(I18n.format("roots.info.pyre.repeat1"), x + 40, y + 37, 16777215);
      mc.fontRenderer.drawStringWithShadow(I18n.format("roots.info.pyre.repeat2"), x + 40, y + 49, 16777215);
      mc.fontRenderer.drawStringWithShadow(I18n.format("roots.info.pyre.repeat3"), x + 40, y + 61, 16777215);
    }


  }
}
