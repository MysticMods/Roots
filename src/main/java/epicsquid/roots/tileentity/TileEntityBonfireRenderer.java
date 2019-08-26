package epicsquid.roots.tileentity;

import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.PyreCraftingRecipe;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class TileEntityBonfireRenderer extends TileEntitySpecialRenderer<TileEntityBonfire> {

  @Override
  public void render(TileEntityBonfire tem, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
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

    PyreCraftingRecipe recipe = ModRecipes.getCraftingRecipe(renderItems);
    if (recipe != null)
      renderResult(tem, x, y, z, recipe.getResult(), 0.8f);
    RitualBase ritual = RitualRegistry.getRitual(tem, Minecraft.getMinecraft().player);
    if (ritual != null)
      renderResult(tem, x, y, z, new ItemStack(ritual.getIcon()), 1f);
  }

  private void renderResult(TileEntityBonfire tem, double x, double y, double z, ItemStack result, float alpha) {
    GlStateManager.enableBlend();
    RenderHelper.enableStandardItemLighting();
    GlStateManager.pushMatrix();
    GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);
    GlStateManager.scale(0.5, 0.5, 0.5);

    IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(result, tem.getWorld(), null);
    if (alpha != 1f) {
      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
      GlStateManager.color(1F, 1F, 1F, alpha);
    }
    Minecraft.getMinecraft().getRenderItem().renderItem(result, model);
    GlStateManager.popMatrix();
    GlStateManager.disableRescaleNormal();
    if (alpha != 1f) {
      GlStateManager.disableBlend();
    }
    GlStateManager.color(1F, 1F, 1F, 1F);
  }
}
