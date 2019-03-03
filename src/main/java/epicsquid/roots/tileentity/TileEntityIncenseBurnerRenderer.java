package epicsquid.roots.tileentity;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class TileEntityIncenseBurnerRenderer extends TileEntitySpecialRenderer<TileEntityIncenseBurner> {

    @Override
    public void render(TileEntityIncenseBurner tei, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (tei.inventory.getStackInSlot(0) != ItemStack.EMPTY) {
            RenderItem r = Minecraft.getMinecraft().getRenderItem();
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5, y + 0.575, z + 0.35);
            GlStateManager.rotate(90f, 1.0f, 0, 0);
            Random random = new Random();
            random.setSeed(tei.inventory.getStackInSlot(0).hashCode() + 256);
            GlStateManager.disableLighting();
            r.renderItem(tei.inventory.getStackInSlot(0), ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }


}