package epicsquid.roots.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;

public class TileEntityImposerRenderer extends TileEntitySpecialRenderer<TileEntityImposer> {
  @Override
  public void render(TileEntityImposer tei, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    if (!tei.inventory.getStackInSlot(0).isEmpty()) {
      GlStateManager.pushMatrix();
      EntityItem item = new EntityItem(Minecraft.getMinecraft().world, x, y, z, tei.inventory.getStackInSlot(0));
      item.hoverStart = 0;
      float rotation = tei.ticks + partialTicks;
      GlStateManager.translate(x + 0.5, (y + 0.15) + Math.sin(rotation / 20.d) / 19.5, z + 0.5);
      GlStateManager.rotate(-rotation * 0.8f, 0, 1.0f, 0);
      Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, true);
      GlStateManager.popMatrix();
    }
  }
}