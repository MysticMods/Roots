package epicsquid.roots.tileentity;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

public class TileEntityBonfireRenderer extends TileEntitySpecialRenderer<TileEntityBonfire> {

  @Override
  public void render(TileEntityBonfire tem, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    ArrayList<ItemStack> renderItems = new ArrayList<>();

    for (int i = 0; i < tem.inventory.getSlots(); i++) {
      if (tem.inventory.getStackInSlot(i) != ItemStack.EMPTY) {
        renderItems.add(tem.inventory.getStackInSlot(i));
      }
    }

    for (int i = 0; i < renderItems.size(); i++) {
      GL11.glPushMatrix();
      EntityItem item = new EntityItem(Minecraft.getMinecraft().world, x, y, z, renderItems.get(i));
      item.hoverStart = 0;
      double shifted = tem.getTicker() + partialTicks + i * (360.0 / renderItems.size());
      Random random = new Random();
      random.setSeed(item.getItem().hashCode());
      GL11.glTranslated(x + 0.5, y + 0.5 + 0.1 * Math.sin(Math.toRadians((shifted * 4.0))), z + 0.5);
      GL11.glRotated(shifted, 0, 1, 0);
      GL11.glTranslated(-0.5, 0, 0);
      GL11.glRotated(shifted, 0, 1, 0);
      Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, true);
      GL11.glPopMatrix();
    }
  }
}
