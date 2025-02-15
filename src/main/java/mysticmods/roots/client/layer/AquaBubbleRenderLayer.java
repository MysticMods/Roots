package mysticmods.roots.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModEffects;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class AquaBubbleRenderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
  private static final ResourceLocation TEXTURE = RootsAPI.rl("textures/entity/aqua_bubble.png");
  private final PlayerModel<AbstractClientPlayer> model;

  public AquaBubbleRenderLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
    super(renderer);
    this.model = renderer.getModel();
  }


  @Override
  public void render(
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      AbstractClientPlayer livingEntity,
      float limbSwing,
      float limbSwingAmount,
      float partialTicks,
      float ageInTicks,
      float netHeadYaw,
      float headPitch
  ) {
    if (livingEntity.hasEffect(ModEffects.AQUA_BUBBLE)) {
      float f = (float) livingEntity.tickCount + partialTicks;
      PlayerModel<AbstractClientPlayer> entitymodel = this.model();
      entitymodel.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTicks);
      this.getParentModel().copyPropertiesTo(entitymodel);
      VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.energySwirl(this.getTextureLocation(), this.xOffset(f) % 1.0F, f * 0.01F % 1.0F));
      entitymodel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
      entitymodel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, -8355712);
    }
  }

  protected float xOffset(float tickCount) {
    return tickCount * 0.001F;
  }

  protected ResourceLocation getTextureLocation() {
    return TEXTURE;
  }

  protected PlayerModel<AbstractClientPlayer> model() {
    return model;
  }
}
