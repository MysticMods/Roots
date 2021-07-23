package epicsquid.roots.tileentity;

import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.recipe.FeyCraftingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TileEntityRunicCrafterRenderer extends TileEntitySpecialRenderer<TileEntityRunicCrafter> {
  public static ItemStack GROVE_STONE = ItemStack.EMPTY;

  @Override
  public void render(TileEntityRunicCrafter te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    if (!te.hasValidGroveStone()) {
      if (GROVE_STONE.isEmpty()) {
        GROVE_STONE = new ItemStack(ModBlocks.grove_stone);
      }
      renderResult((te.getWorld().getTotalWorldTime() + partialTicks) * 2.6f, x, y, z, GROVE_STONE, 0.99f);
    } else {
      ItemStack pedestal = te.pedestal.getStackInSlot(0);
      if (!pedestal.isEmpty()) {
        renderResult((te.getWorld().getTotalWorldTime() + partialTicks) * 2.6f, x, y, z, pedestal, 0.99f);
      }
      List<ItemStack> items = new ArrayList<>();

      for (int i = 0; i < te.inventory.getSlots(); i++)
        items.add(te.inventory.getStackInSlot(i));

      for (ItemStack item : items) {

        if (item.isEmpty())
          continue;

        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();

        switch (items.indexOf(item)) {
          case 0:
            GlStateManager.translate(x + 0.68, y + 0.62, z + 0.125);
            break;
          case 1:
            GlStateManager.translate(x + 0.3, y + 0.75, z + 0.1);
            break;
          case 2:
            GlStateManager.translate(x + 0.95, y + 0.72, z + 0.55);
            break;
          case 3:
            GlStateManager.translate(x + 0.07, y + 0.72, z + 0.58);
            break;
          case 4:
            GlStateManager.translate(x + 0.45, y + 0.78, z + 0.95);
            break;
        }

        GlStateManager.scale(0.45, 0.45, 0.45);
        GlStateManager.rotate((te.getWorld().getTotalWorldTime() + partialTicks) * 4, 0, 1, 0);

        Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.GROUND);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
      }
    }
  }

  private void renderResult(float ticks, double x, double y, double z, ItemStack result, float alpha) {
    GlStateManager.enableBlend();
    RenderHelper.enableStandardItemLighting();
    GlStateManager.enableRescaleNormal();
    GlStateManager.pushMatrix();
    GlStateManager.translate(x + 0.5, y + 1.1, z + 0.5);
    GlStateManager.scale(0.3, 0.3, 0.3);
    GlStateManager.rotate(ticks, 0, 1, 0);

    Minecraft mc = Minecraft.getMinecraft();
    RenderItem ri = mc.getRenderItem();
    TextureManager tm = mc.getTextureManager();

    IBakedModel bakedmodel = ri.getItemModelWithOverrides(result, null, null);
    tm.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    tm.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
    GlStateManager.pushMatrix();
    ri.renderItem(result, bakedmodel);
    GlStateManager.cullFace(GlStateManager.CullFace.BACK);
    GlStateManager.popMatrix();
    GlStateManager.disableRescaleNormal();
    GlStateManager.disableBlend();
    tm.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    tm.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();

    GlStateManager.popMatrix();
    GlStateManager.color(1F, 1F, 1F, 1F);
  }
}
