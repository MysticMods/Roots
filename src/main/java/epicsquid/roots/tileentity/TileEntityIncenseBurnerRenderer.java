package epicsquid.roots.tileentity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;

import java.util.Random;

public class TileEntityIncenseBurnerRenderer extends TileEntityRenderer<TileEntityIncenseBurner> {

  @Override
  public void render(TileEntityIncenseBurner tei, double x, double y, double z, float partialTicks, int destroyStage) {
    if (!tei.inventory.getStackInSlot(0).isEmpty()) {
      ItemRenderer r = Minecraft.getInstance().getItemRenderer();
      GlStateManager.pushMatrix();
      GlStateManager.translated(x + 0.5, y + 0.575, z + 0.35);
      GlStateManager.rotated(90f, 1.0f, 0, 0);
      Random random = new Random();
      random.setSeed(tei.inventory.getStackInSlot(0).hashCode() + 256);
      GlStateManager.disableLighting();
      r.renderItem(tei.inventory.getStackInSlot(0), ItemCameraTransforms.TransformType.GROUND);
      GlStateManager.enableLighting();
      GlStateManager.popMatrix();
    }
  }


}