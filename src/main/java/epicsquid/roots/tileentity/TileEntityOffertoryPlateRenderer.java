package epicsquid.roots.tileentity;

import java.util.Random;

import epicsquid.roots.block.BlockOffertoryPlate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileEntityOffertoryPlateRenderer extends TileEntitySpecialRenderer<TileEntityOffertoryPlate> {

  @Override
  public void render(TileEntityOffertoryPlate tei, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    if (!tei.inventory.getStackInSlot(0).isEmpty()) {
      int count = getCount(tei.inventory.getStackInSlot(0));
      RenderItem r = Minecraft.getMinecraft().getRenderItem();
      EnumFacing f = tei.getWorld().getBlockState(tei.getPos()).getValue(BlockOffertoryPlate.FACING);
      for (int i = 0; i < count; i++) {
        GlStateManager.pushMatrix();
        GlStateManager
            .translate(x + 0.5, y + 0.8125 + 0.0625 * (double) i + 0.0625 * (tei.inventory.getStackInSlot(0).getItem() instanceof ItemBlock ? 1.0 : 0),
                z + 0.5);
        GlStateManager.rotate(180 - f.getHorizontalAngle(), 0, 1, 0);
        GlStateManager.rotate(67.5f, 1.0f, 0, 0);
        Random random = new Random();
        random.setSeed(tei.inventory.getStackInSlot(0).hashCode() + 256 * i);
        GlStateManager.translate(0.125f * (random.nextFloat() - 0.5f), -0.1875 + 0.125f * (random.nextFloat() - 0.5f), 0);
        r.renderItem(tei.inventory.getStackInSlot(0), TransformType.GROUND);
        GlStateManager.popMatrix();
      }
    }
  }

  public int getCount(ItemStack s) {
    if (s.getCount() == 64) {
      return 5;
    }
    if (s.getCount() > 33) {
      return 4;
    }
    if (s.getCount() > 16) {
      return 3;
    }
    if (s.getCount() >= 2) {
      return 2;
    }
    if (s.getCount() == 1) {
      return 1;
    }
    return 0;
  }

}