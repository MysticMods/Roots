package epicsquid.roots.tileentity;

import epicsquid.roots.recipe.PyreCraftingRecipe;
import epicsquid.roots.ritual.RitualBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class TileEntityPyreRenderer extends TileEntitySpecialRenderer<TileEntityPyre> {

  @Override
  public void render(TileEntityPyre tem, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    ArrayList<ItemStack> renderItems = new ArrayList<>();

    for (int i = 0; i < tem.inventory.getSlots(); i++) {
      if (!tem.inventory.getStackInSlot(i).isEmpty()) {
        renderItems.add(tem.inventory.getStackInSlot(i));
      }
    }

    for (int i = 0; i < renderItems.size(); i++) {
      GlStateManager.pushMatrix();
      EntityItem item = new EntityItem(Minecraft.getMinecraft().world, x, y, z, renderItems.get(i));
      item.hoverStart = 0;
      float shifted = (float) (tem.getTicker() + partialTicks + i * (360.0 / renderItems.size()));
      Random random = new Random();
      random.setSeed(item.getItem().hashCode());
      GlStateManager.translate(x + 0.5, y + 0.5 + 0.1 * Math.sin(Math.toRadians((shifted * 4.0))), z + 0.5);
      GlStateManager.rotate(shifted, 0, 1, 0);
      GlStateManager.translate(-0.5, 0, 0);
      GlStateManager.rotate(shifted, 0, 1, 0);
      Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, true);
      GlStateManager.popMatrix();
    }

    PyreCraftingRecipe recipe = tem.getCurrentRecipe();
    if (recipe != null) {
      renderResult(x, y, z, recipe.getResult(), 0.8f);
    }
    RitualBase ritual = tem.getCurrentRitual();
    if (ritual != null && !ritual.isDisabled()) {
      renderResult(x, y, z, ritual.getItemStack(), 1f);
    }
  }

  private void renderResult(double x, double y, double z, ItemStack result, float alpha) {
    GlStateManager.enableBlend();
    RenderHelper.enableStandardItemLighting();
    GlStateManager.enableRescaleNormal();
    GlStateManager.pushMatrix();
    GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);
    GlStateManager.scale(0.5, 0.5, 0.5);

    Minecraft mc = Minecraft.getMinecraft();
    RenderItem ri = mc.getRenderItem();
    TextureManager tm = mc.getTextureManager();

    IBakedModel bakedmodel = ri.getItemModelWithOverrides(result, null, null);
    if (alpha != 1f) {
      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
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
