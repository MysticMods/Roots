package epicsquid.roots.util;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;

public class EntityRenderHelper {
  public static void drawEntityOnScreen(int posX, int posY, float scale, float mouseX, float mouseY, LivingEntity ent) {
    if (ent.world == null) {
      ent.world = Minecraft.getInstance().world;
    }

    GlStateManager.enableColorMaterial();
    GlStateManager.pushMatrix();
    GlStateManager.translatef((float) posX, (float) posY, 50.0F);
    GlStateManager.scalef(-scale, scale, scale);
    GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
    float f = ent.renderYawOffset;
    float f1 = ent.rotationYaw;
    float f2 = ent.rotationPitch;
    float f3 = ent.prevRotationYawHead;
    float f4 = ent.rotationYawHead;
    GlStateManager.rotatef(135.0F, 0.0F, 1.0F, 0.0F);
    RenderHelper.enableStandardItemLighting();
    GlStateManager.rotatef(-135.0F, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotatef(-((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
    ent.renderYawOffset = (float) Math.atan((double) (mouseX / 40.0F)) * 20.0F;
    ent.rotationYaw = (float) Math.atan((double) (mouseX / 40.0F)) * 40.0F;
    ent.rotationPitch = -((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F;
    ent.rotationYawHead = ent.rotationYaw;
    ent.prevRotationYawHead = ent.rotationYaw;
    GlStateManager.translatef(0.0F, 0.0F, 0.0F);
    EntityRendererManager rendermanager = Minecraft.getInstance().getRenderManager();
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
    GlStateManager.activeTexture(GLX.GL_TEXTURE1);
    GlStateManager.disableTexture();
    GlStateManager.activeTexture(GLX.GL_TEXTURE0);
  }
}
