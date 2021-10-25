package epicsquid.roots.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.item.EntityItem;

public class TileEntityImbuerRenderer extends TileEntityRenderer<TileEntityImbuer> {

  @Override
  public void render(TileEntityImbuer tei, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    if (!tei.inventory.getStackInSlot(1).isEmpty()) {
      GlStateManager.pushMatrix();
      EntityItem item = new EntityItem(Minecraft.getMinecraft().world, x, y, z, tei.inventory.getStackInSlot(1));
      item.hoverStart = 0;
      GlStateManager.translate(x + 0.5, y + 0.3125, z + 0.5);
      GlStateManager.rotate(tei.angle, 0, 1.0f, 0);
      Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, true);
      GlStateManager.popMatrix();
    }
    if (!tei.inventory.getStackInSlot(0).isEmpty()) {
      GlStateManager.pushMatrix();
      EntityItem item = new EntityItem(Minecraft.getMinecraft().world, x, y, z, tei.inventory.getStackInSlot(0));
      item.hoverStart = 0;
      GlStateManager.translate(x + 0.5, y + 0.125, z);
      GlStateManager.rotate(90, 1.0f, 0, 0);
      Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, true);
      GlStateManager.popMatrix();
    }
  }

}