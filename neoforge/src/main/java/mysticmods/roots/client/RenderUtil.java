package mysticmods.roots.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;

public class RenderUtil {
  private static final RenderType TRANSLUCENT = RenderType.entityTranslucent(TextureAtlas.LOCATION_BLOCKS);

  public static void renderItemAsIcon(ItemStack stack, PoseStack poseStack, int pX, int pY, int size, boolean transparent) {
    Minecraft instance = Minecraft.getInstance();
    ItemRenderer itemRenderer = instance.getItemRenderer();
    BakedModel itemBakedModel = itemRenderer.getModel(stack, null, null, 0);
    TextureManager textureManager = instance.getTextureManager();
    textureManager.getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
    RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
    RenderSystem.enableBlend();
    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    poseStack.pushPose();
    poseStack.translate(pX, pY, 100.0F);
    poseStack.translate(8.0D, 8.0D, 0.0D);
    poseStack.scale(1.0F, -1.0F, 1.0F);
    poseStack.scale(size, size, size);
    RenderSystem.applyModelViewMatrix();
    MultiBufferSource.BufferSource bufferSource = instance.renderBuffers().bufferSource();
    boolean flag = !itemBakedModel.usesBlockLight();
    if (flag) {
      Lighting.setupForFlatItems();
    }
    if (transparent) {
      itemRenderer.render(stack, ItemTransforms.TransformType.GUI, false, poseStack, transparentBuffer(bufferSource), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, itemBakedModel);
    } else {
      itemRenderer.render(stack, ItemTransforms.TransformType.GUI, false, poseStack, bufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, itemBakedModel);
    }
    bufferSource.endBatch();
    if (flag) {
      Lighting.setupFor3DItems();
    }

    if (transparent) {
      RenderSystem.depthMask(true);
      RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }
    poseStack.popPose();
    RenderSystem.applyModelViewMatrix();
    if (transparent) {
      RenderSystem.disableBlend();
      RenderSystem.defaultBlendFunc();
    }
  }

  private static MultiBufferSource transparentBuffer(MultiBufferSource.BufferSource bufferSource) {
    return (type) -> new TintWrappedVertexConsumer(bufferSource.getBuffer(TRANSLUCENT), 1.0f, 1.0f, 1.0f, 0.25f);
  }
}
