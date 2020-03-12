package epicsquid.roots.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;

public class TileEntityMunifierRenderer extends TileEntitySpecialRenderer<TileEntityMunifier> {
  @Override
  public void render(TileEntityMunifier tei, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    if (!tei.inventory.getStackInSlot(0).isEmpty()) {
      GlStateManager.pushMatrix();
      EntityItem item = new EntityItem(Minecraft.getMinecraft().world, x, y, z, tei.inventory.getStackInSlot(0));
      item.hoverStart = 0;
      GlStateManager.translate(x, y - 0.5, z);
      //+ 0.75, y, z + 0.5);
/*      GlStateManager.rotate(45, 0, 0, 1.0f);
      GlStateManager.translate(1.125 + (0.125 / 2), 0, 0.5);*/
      Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, true);
      GlStateManager.popMatrix();
    }
  }
}