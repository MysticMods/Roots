package epicsquid.roots.gui.hud;

import epicsquid.roots.recipe.PyreCraftingRecipe;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.tileentity.TileEntityBonfire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class RenderBonfire {
  public static void render(Minecraft mc, BlockPos pos, IBlockState state, RenderGameOverlayEvent.Post event) {
    World world = mc.world;
    if (!(world.getTileEntity(pos) instanceof TileEntityBonfire)) return;

    TileEntityBonfire te = (TileEntityBonfire) world.getTileEntity(pos);
    if (te == null) return;

    ScaledResolution res = event.getResolution();

    int x = res.getScaledWidth() / 2;
    int y = res.getScaledHeight() / 2;

    if (te.getLastRecipeUsed() != null) {
      PyreCraftingRecipe recipe = te.getLastRecipeUsed();
      ItemStack output = recipe.getResult();
      RenderHelper.enableGUIStandardItemLighting();
      mc.getRenderItem().renderItemIntoGUI(output, x + 40, y);
      if (output.getCount() > 1) {
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, output, x + 40, y, null);
      }
      RenderHelper.disableStandardItemLighting();
      mc.fontRenderer.drawStringWithShadow(I18n.format("roots.info.pyre.repeat1"), x + 40, y + 22, 16777215);
      mc.fontRenderer.drawStringWithShadow(I18n.format("roots.info.pyre.repeat2"), x + 40, y + 34, 16777215);
      mc.fontRenderer.drawStringWithShadow(I18n.format("roots.info.pyre.repeat3"), x + 40, y + 46, 16777215);
    } else if (te.getLastRitualUsed() != null) {
      RitualBase ritual = te.getLastRitualUsed();
      if (ritual.getIcon() != null) {
        mc.getRenderItem().renderItemIntoGUI(new ItemStack(ritual.getIcon()), x + 40, y);
      }
      String s = I18n.format("roots.ritual." + ritual.getName() + ".name");
      mc.fontRenderer.drawStringWithShadow(ritual.getFormat() + s, (float) x + 40, y + 22, 16777215);
      mc.fontRenderer.drawStringWithShadow(I18n.format("roots.info.pyre.repeat1"), x + 40, y + 37, 16777215);
      mc.fontRenderer.drawStringWithShadow(I18n.format("roots.info.pyre.repeat2"), x + 40, y + 49, 16777215);
      mc.fontRenderer.drawStringWithShadow(I18n.format("roots.info.pyre.repeat3"), x + 40, y + 61, 16777215);
    }
  }
}
