package epicsquid.roots.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TileEntityGroveCrafterRenderer extends TileEntitySpecialRenderer<TileEntityGroveCrafter> {

  @Override
  public void render(TileEntityGroveCrafter te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

    List<ItemStack> items = new ArrayList<>();

    for (int i = 0; i < te.inventory.getSlots(); i++)
        items.add(te.inventory.getStackInSlot(i));

    for (ItemStack item : items) {

      if (item.isEmpty())
        continue;

      GlStateManager.pushMatrix();
      RenderHelper.enableStandardItemLighting();
      //Dunno how much I need this ^^^


      switch (items.indexOf(item))
      {
        case 0:
          GlStateManager.translate(x + 0.5, y + 1.1, z + 0.125);
          break;
        case 1:
          GlStateManager.translate(x + 0.13, y + 1.1, z + 0.45);
          break;
        case 2:
          GlStateManager.translate(x + 0.88, y + 1.1, z + 0.45);
          break;
        case 3:
          GlStateManager.translate(x + 0.25, y + 1.1, z + 0.88);
          break;
        case 4:
          GlStateManager.translate(x + 0.69, y + 1.1, z + 0.88);
          break;
      }

      GlStateManager.scale(0.45, 0.45, 0.45);
      GlStateManager.rotate((te.getWorld().getTotalWorldTime() + partialTicks) * 4, 0, 1, 0);

      // Old rotation (Items laid on the leaves)
//      GlStateManager.rotate(90, 1, 0, 0);
//      GlStateManager.rotate(-90, 0, 0, 1);

      Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.GROUND);

      RenderHelper.disableStandardItemLighting();
      GlStateManager.popMatrix();
    }

  }
}
