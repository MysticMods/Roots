package epicsquid.roots.tileentity;

import com.mojang.blaze3d.platform.GlStateManager;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.PyreCraftingRecipe;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class TileEntityBonfireRenderer extends TileEntityRenderer<TileEntityBonfire> {

  @Override
  public void render(TileEntityBonfire tem, double x, double y, double z, float partialTicks, int destroyStage) {
    ArrayList<ItemStack> renderItems = new ArrayList<>();

    for (int i = 0; i < tem.inventory.getSlots(); i++) {
      if (!tem.inventory.getStackInSlot(i).isEmpty()) {
        renderItems.add(tem.inventory.getStackInSlot(i));
      }
    }

    for (int i = 0; i < renderItems.size(); i++) {
      GlStateManager.pushMatrix();
      ItemEntity item = new ItemEntity(Minecraft.getInstance().world, x, y, z, renderItems.get(i));
      ItemUtil.setHoverStart(item, 0);
      float shifted = (float) (tem.getTicker() + partialTicks + i * (360.0 / renderItems.size()));
      Random random = new Random();
      random.setSeed(item.getItem().hashCode());
      GlStateManager.translated(x + 0.5, y + 0.5 + 0.1 * Math.sin(Math.toRadians((shifted * 4.0))), z + 0.5);
      GlStateManager.rotated(shifted, 0, 1, 0);
      GlStateManager.translated(-0.5, 0, 0);
      GlStateManager.rotated(shifted, 0, 1, 0);
      Minecraft.getInstance().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, true);
      GlStateManager.popMatrix();
    }

    PyreCraftingRecipe recipe = ModRecipes.getCraftingRecipe(renderItems);
    if (recipe != null)
      renderResult(tem, x, y, z, recipe.getResult(), 0.8f);
    RitualBase ritual = RitualRegistry.getRitual(tem, Minecraft.getInstance().player);
    if (ritual != null && !ritual.isDisabled())
      renderResult(tem, x, y, z, new ItemStack(ritual.getIcon()), 1f);
  }

  private void renderResult(TileEntityBonfire tem, double x, double y, double z, ItemStack result, float alpha) {
    GlStateManager.enableBlend();
    RenderHelper.enableStandardItemLighting();
    GlStateManager.pushMatrix();
    GlStateManager.translated(x + 0.5, y + 1.5, z + 0.5);
    GlStateManager.scaled(0.5, 0.5, 0.5);

    IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(result, tem.getWorld(), null);
    if (alpha != 1f) {
      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
      GlStateManager.color4f(1F, 1F, 1F, alpha);
    }
    Minecraft.getInstance().getItemRenderer().renderItem(result, model);
    GlStateManager.popMatrix();
    GlStateManager.disableRescaleNormal();
    if (alpha != 1f) {
      GlStateManager.disableBlend();
    }
    GlStateManager.color4f(1F, 1F, 1F, 1F);
  }
}
