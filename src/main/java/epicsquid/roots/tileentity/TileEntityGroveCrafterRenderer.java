package epicsquid.roots.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TileEntityGroveCrafterRenderer extends TileEntitySpecialRenderer<TileEntityGroveCrafter> {

  @Override
  public void render(TileEntityGroveCrafter te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

    List<ItemStack> items = new ArrayList<>();

    for (int i = 0; i < te.inventory.getSlots(); i++)
        items.add(te.inventory.getStackInSlot(i));

    for (ItemStack item : items) {

      if (item.isEmpty())
        continue;

      GlStateManager.pushMatrix();
      RenderHelper.enableStandardItemLighting();
      //Dunno how much I need this ^^^


      switch (items.indexOf(item))
      {
        case 0:
          GlStateManager.translate(x + 0.9, y + 1.2, z + 0.5);
          break;
        case 1:
          GlStateManager.translate(x + 0.55, y + 1.2, z + 0.15);
          break;
        case 2:
          GlStateManager.translate(x + 0.55, y + 1.2, z + 0.85);
          break;
        case 3:
          GlStateManager.translate(x + 0.10, y + 1.2, z + 0.25);
          break;
        case 4:
          GlStateManager.translate(x + 0.1, y + 1.2, z + 0.7);
          break;
      }

      //TODO Fix item disappearing in particular player view angles
      GlStateManager.scale(0.20, 0.20, 0.20);
      GlStateManager.rotate((te.getWorld().getTotalWorldTime() + partialTicks) * 4, 0, 1, 0);

      // Old rotation (Items laid on the leaves)
//      GlStateManager.rotate(90, 1, 0, 0);
//      GlStateManager.rotate(-90, 0, 0, 1);

      IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(item, te.getWorld(), null);
      Minecraft.getMinecraft().getRenderItem().renderItem(item, model);

      RenderHelper.disableStandardItemLighting();
      GlStateManager.popMatrix();
    }

  }
}
