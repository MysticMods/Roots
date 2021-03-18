package epicsquid.roots.tileentity;

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

public class TileEntityFeyCrafterRenderer extends TileEntitySpecialRenderer<TileEntityFeyCrafter> {

  @Override
  public void render(TileEntityFeyCrafter te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

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
          GlStateManager.translate(x + 0.5, y + 1.1, z + 0.125);
          break;
        case 1:
          GlStateManager.translate(x + 0.13, y + 1.1, z + 0.45);
          break;
        case 2:
          GlStateManager.translate(x + 0.88, y + 1.1, z + 0.45);
          break;
        case 3:
          GlStateManager.translate(x + 0.25, y + 1.1, z + 0.88);
          break;
        case 4:
          GlStateManager.translate(x + 0.69, y + 1.1, z + 0.88);
          break;
      }

      GlStateManager.scale(0.45, 0.45, 0.45);
      GlStateManager.rotate((te.getWorld().getTotalWorldTime() + partialTicks) * 4, 0, 1, 0);

      // Old rotation (Items laid on the leaves)
//      GlStateManager.rotate(90, 1, 0, 0);
//      GlStateManager.rotate(-90, 0, 0, 1);

      Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.GROUND);

      RenderHelper.disableStandardItemLighting();
      GlStateManager.popMatrix();

      FeyCraftingRecipe recipe = te.getRecipe();
      if (recipe != null) {
        renderResult((te.getWorld().getTotalWorldTime() + partialTicks) * 4, x, y, z, recipe.getResult(), 0.9f);
      }
    }
  }

  private void renderResult(float ticks, double x, double y, double z, ItemStack result, float alpha) {
    GlStateManager.enableBlend();
    RenderHelper.enableStandardItemLighting();
    GlStateManager.enableRescaleNormal();
    GlStateManager.pushMatrix();
    GlStateManager.translate(x + 0.5, y + 1.55, z + 0.5);
    GlStateManager.scale(0.25, 0.25, 0.25);
    GlStateManager.rotate(ticks, 1, 1, 1);

    Minecraft mc = Minecraft.getMinecraft();
    RenderItem ri = mc.getRenderItem();
    TextureManager tm = mc.getTextureManager();

    IBakedModel bakedmodel = ri.getItemModelWithOverrides(result, null, null);
    if (alpha != 1f) {
      GlStateManager.blendFunc(GlStateManager.SourceFactor.DST_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
      GlStateManager.color(1F, 1F, 1F, alpha);
    }
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
