package epicsquid.roots.tileentity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TileEntityFeyCrafterRenderer extends TileEntityRenderer<TileEntityFeyCrafter> {

  @Override
  public void render(TileEntityFeyCrafter te, double x, double y, double z, float partialTicks, int destroyStage) {

    List<ItemStack> items = new ArrayList<>();

    for (int i = 0; i < te.inventory.getSlots(); i++)
      items.add(te.inventory.getStackInSlot(i));

    for (ItemStack item : items) {

      if (item.isEmpty())
        continue;

      GlStateManager.pushMatrix();
      RenderHelper.enableStandardItemLighting();
      //Dunno how much I need this ^^^


      switch (items.indexOf(item)) {
        case 0:
          GlStateManager.translated(x + 0.5, y + 1.1, z + 0.125);
          break;
        case 1:
          GlStateManager.translated(x + 0.13, y + 1.1, z + 0.45);
          break;
        case 2:
          GlStateManager.translated(x + 0.88, y + 1.1, z + 0.45);
          break;
        case 3:
          GlStateManager.translated(x + 0.25, y + 1.1, z + 0.88);
          break;
        case 4:
          GlStateManager.translated(x + 0.69, y + 1.1, z + 0.88);
          break;
      }

      GlStateManager.scaled(0.45, 0.45, 0.45);
      GlStateManager.rotated((te.getWorld().getGameTime() + partialTicks) * 4, 0, 1, 0);

      // Old rotation (Items laid on the leaves)
//      GlStateManager.rotate(90, 1, 0, 0);
//      GlStateManager.rotate(-90, 0, 0, 1);

      // TODO: Camera transforms are deprecated
      // what shoudl be used instead?
      Minecraft.getInstance().getItemRenderer().renderItem(item, ItemCameraTransforms.TransformType.GROUND);

      RenderHelper.disableStandardItemLighting();
      GlStateManager.popMatrix();
    }

  }
}
