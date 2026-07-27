package mysticmods.roots.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.model.DandelionWindsModel;
import mysticmods.roots.client.model.ModelHolder;
import mysticmods.roots.init.ModEffects;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class DandelionWindsRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
  private static final ResourceLocation TEXTURE = RootsAPI.rl("textures/entity/dandelion_winds.png");
  private final DandelionWindsModel<T> model;

  public DandelionWindsRenderLayer(RenderLayerParent<T, M> renderer, EntityModelSet modelSet) {
    super(renderer);
    this.model = new DandelionWindsModel<>(modelSet.bakeLayer(ModelHolder.DANDELION_WINDS));
  }

  @Override
  public void render(
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      T livingEntity,
      float limbSwing,
      float limbSwingAmount,
      float partialTicks,
      float ageInTicks,
      float netHeadYaw,
      float headPitch
  ) {
    if (livingEntity.hasEffect(ModEffects.DANDELION_WINDS)){
      poseStack.pushPose();
      poseStack.translate(0, -1.5, 0);
      poseStack.scale(2, 2, 2);
      float f = (float) livingEntity.tickCount + partialTicks;
      VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.breezeWind(TEXTURE, this.xOffset(f) % 1.0F, 0.0F));
      this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
      this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
      poseStack.popPose();
    }
  }

  protected float xOffset(float tickCount) {
    return tickCount * 0.02F;
  }

  protected ResourceLocation getTextureLocation() {
    return TEXTURE;
  }
}
