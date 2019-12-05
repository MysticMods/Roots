package epicsquid.roots.tileentity;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;

public class TileEntityMortarRenderer extends TileEntityRenderer<TileEntityMortar> {

  @Override
  public void render(TileEntityMortar te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    ArrayList<ItemStack> renderItems = new ArrayList<>();

    for (int i = 0; i < te.inventory.getSlots(); i++) {
      if (!te.inventory.getStackInSlot(i).isEmpty()) {
        renderItems.add(te.inventory.getStackInSlot(i));
      }
    }

    for (int i = 0; i < renderItems.size(); i++) {
      GlStateManager.pushMatrix();
      ItemEntity item = new ItemEntity(Minecraft.getMinecraft().world, x, y, z, renderItems.get(i));
      item.hoverStart = 0;
      Random random = new Random();
      random.setSeed(item.getItem().hashCode());
      GlStateManager.translate(x, y, z);
      GlStateManager.translate(0.475 + random.nextFloat() / 20.0, 0.05 + random.nextFloat() / 20.0, 0.475 + random.nextFloat() / 20.0);
      GlStateManager.scale(0.65, 0.65, 0.65);
      GlStateManager.rotate(random.nextInt(360), 0, 1, 0);
      Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, true);
      GlStateManager.popMatrix();
    }
  }
}