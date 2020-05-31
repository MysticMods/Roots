package epicsquid.roots.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUtil {
  public static void drawEntityOnScreen(int posX, int posY, float scale, float mouseX, float mouseY, EntityLivingBase ent) {
    if (ent.world == null) {
      ent.world = Minecraft.getMinecraft().world;
    }

    GlStateManager.enableColorMaterial();
    GlStateManager.pushMatrix();
    GlStateManager.translate((float) posX, (float) posY, 50.0F);
    GlStateManager.scale(-scale, scale, scale);
    GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
    float f = ent.renderYawOffset;
    float f1 = ent.rotationYaw;
    float f2 = ent.rotationPitch;
    float f3 = ent.prevRotationYawHead;
    float f4 = ent.rotationYawHead;
    GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
    RenderHelper.enableStandardItemLighting();
    GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(-((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
    ent.renderYawOffset = (float) Math.atan((double) (mouseX / 40.0F)) * 20.0F;
    ent.rotationYaw = (float) Math.atan((double) (mouseX / 40.0F)) * 40.0F;
    ent.rotationPitch = -((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F;
    ent.rotationYawHead = ent.rotationYaw;
    ent.prevRotationYawHead = ent.rotationYaw;
    GlStateManager.translate(0.0F, 0.0F, 0.0F);
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    rendermanager.setPlayerViewY(180.0F);
    rendermanager.setRenderShadow(false);
    rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
    rendermanager.setRenderShadow(true);
    ent.renderYawOffset = f;
    ent.rotationYaw = f1;
    ent.rotationPitch = f2;
    ent.prevRotationYawHead = f3;
    ent.rotationYawHead = f4;
    GlStateManager.popMatrix();
    RenderHelper.disableStandardItemLighting();
    GlStateManager.disableRescaleNormal();
    GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
    GlStateManager.disableTexture2D();
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
  }

  public static void renderBlock(IBlockState block, float posX, float posY, float posZ, float rotation, float scale) {
    GlStateManager.enableRescaleNormal();
    GlStateManager.pushMatrix();
    GlStateManager.rotate(-30, 0, 1, 0);
    RenderHelper.enableStandardItemLighting();
    GlStateManager.popMatrix();
    GlStateManager.pushMatrix();
    GlStateManager.translate(posX, posY, 50 + posZ);
    GlStateManager.rotate(20, 1, 0, 0);
    GlStateManager.scale(scale * 50, -(scale * 50), -(scale * 50));
    GlStateManager.translate(0.5f, 0.5f, 0.5f);
    GlStateManager.rotate(rotation, 0, 1, 0);
    GlStateManager.translate(-0.5f, -0.5f, -0.5f);
    Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(block, 1);
    GlStateManager.popMatrix();
    RenderHelper.disableStandardItemLighting();
    GlStateManager.disableRescaleNormal();
  }
}
