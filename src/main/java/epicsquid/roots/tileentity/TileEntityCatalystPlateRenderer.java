package epicsquid.roots.tileentity;

import epicsquid.roots.Roots;
import epicsquid.roots.block.BlockCatalystPlate;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.Random;

public class TileEntityCatalystPlateRenderer extends TileEntityRenderer<TileEntityCatalystPlate> {

  @Override
  public void render(TileEntityCatalystPlate tei, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    if (!tei.inventory.getStackInSlot(0).isEmpty()) {
      int count = getCount(tei.inventory.getStackInSlot(0));
      ItemRenderer r = Minecraft.getMinecraft().getRenderItem();
      IBlockState state = tei.getWorld().getBlockState(tei.getPos());
      if (state.getBlock() != ModBlocks.catalyst_plate && state.getBlock() != ModBlocks.reinforced_catalyst_plate) {
        Roots.logger.error("Fatal error rendering catalyst plate, block state was " + state.toString() + " when catalyst plate was expected.");
        return;
      }
      EnumFacing f = state.getValue(BlockCatalystPlate.FACING);
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