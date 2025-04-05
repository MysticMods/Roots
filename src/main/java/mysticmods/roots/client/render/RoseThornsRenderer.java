package mysticmods.roots.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.model.ModelHolder;
import mysticmods.roots.client.model.RoseThornsModel;
import mysticmods.roots.entity.other.RoseThornsEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RoseThornsRenderer extends EntityRenderer<RoseThornsEntity> {
  public static final ResourceLocation TEXTURE = RootsAPI.rl("textures/entity/rose_thorns.png");
  protected final RoseThornsModel model;

  public RoseThornsRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.model = new RoseThornsModel(context.bakeLayer(ModelHolder.ROSE_THORNS));
  }

  public RoseThornsModel getModel() {
    return model;
  }

  @Override
  public ResourceLocation getTextureLocation(RoseThornsEntity entity) {
    return TEXTURE;
  }

  @Override
  protected boolean shouldShowName(RoseThornsEntity entity) {
    return false;
  }

  @Override
  protected float getShadowRadius(RoseThornsEntity entity) {
    return 0f;
  }

  @Override
  public void render(RoseThornsEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
    poseStack.pushPose();

    float f6 = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
    this.model.prepareMobModel(entity, 0f, 0f, partialTicks);
    this.model.setupAnim(entity, 0f, 0f, entity.tickCount + partialTicks, 0f, f6);
    poseStack.mulPose(Axis.ZP.rotationDegrees(-180f));
    poseStack.translate(0f, -1.5f, 0f);
    Minecraft minecraft = Minecraft.getInstance();
    boolean flag1 = !entity.isInvisibleTo(minecraft.player);
    RenderType rendertype = RenderType.entityCutout(this.getTextureLocation(entity));
    VertexConsumer vertexconsumer = buffer.getBuffer(rendertype);
    this.model.renderToBuffer(poseStack, vertexconsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, flag1 ? 654311423 : -1);
    poseStack.popPose();
    super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
  }
}
