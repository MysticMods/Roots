package epicsquid.roots.tileentity;

import com.mojang.blaze3d.platform.GlStateManager;
import epicsquid.mysticallib.util.ItemUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.item.ItemEntity;

public class TileEntityImbuerRenderer extends TileEntityRenderer<TileEntityImbuer> {

  @Override
  public void render(TileEntityImbuer tei, double x, double y, double z, float partialTicks, int destroyStage) {
    if (!tei.inventory.getStackInSlot(1).isEmpty()) {
      GlStateManager.pushMatrix();
      ItemEntity item = new ItemEntity(Minecraft.getInstance().world, x, y, z, tei.inventory.getStackInSlot(1));
      ItemUtil.setHoverStart(item, 0);
      GlStateManager.translated(x + 0.5, y + 0.3125, z + 0.5);
      GlStateManager.rotated(tei.angle, 0, 1.0f, 0);
      Minecraft.getInstance().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, true);
      GlStateManager.popMatrix();
    }
    if (!tei.inventory.getStackInSlot(0).isEmpty()) {
      GlStateManager.pushMatrix();
      ItemEntity item = new ItemEntity(Minecraft.getInstance().world, x, y, z, tei.inventory.getStackInSlot(0));
      ItemUtil.setHoverStart(item, 0);
      GlStateManager.translated(x + 0.5, y + 0.125, z);
      GlStateManager.rotated(90, 1.0f, 0, 0);
      Minecraft.getInstance().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, true);
      GlStateManager.popMatrix();
    }
  }

}