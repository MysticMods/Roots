package mysticmods.roots.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.model.MeteorModel;
import mysticmods.roots.client.model.ModelHolder;
import mysticmods.roots.entity.projectile.WildfireEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class WildfireRenderer extends EntityRenderer<WildfireEntity> {
  protected final MeteorModel model;

  public WildfireRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.model = new MeteorModel(context.bakeLayer(ModelHolder.METEOR));
  }

  @Override
  public void render(WildfireEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
    poseStack.pushPose();
/*    poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
    poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
    poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));
    poseStack.scale(0.05625F, 0.05625F, 0.05625F);
    poseStack.translate(-4.0F, 0.0F, 0.0F);*/

    poseStack.translate(0.0f, 0.4f, 0.0f);

    poseStack.scale(1.8f, 1.8f, 1.8f);
    this.model.prepareMobModel(entity, 0, 0, partialTicks);
    this.model.setupAnim(entity, 0f, 0f, entity.tickCount + partialTicks, 0, 0);
    Minecraft minecraft = Minecraft.getInstance();
    boolean flag1 = !entity.isInvisibleTo(minecraft.player);
    RenderType rendertype = RenderType.entityCutout(this.getTextureLocation(entity));
    VertexConsumer vertexconsumer = buffer.getBuffer(rendertype);
    this.model.renderToBuffer(poseStack, vertexconsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, flag1 ? 654311423 : -1);
    poseStack.popPose();

    super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
  }

  private static final ResourceLocation TEXTURE = RootsAPI.rl("textures/entity/wildfire.png");

  @Override
  public ResourceLocation getTextureLocation(WildfireEntity entity) {
    return RootsAPI.rl("textures/entity/meteor.png");
  }

  public void vertex(
      PoseStack.Pose pose,
      VertexConsumer consumer,
      int x,
      int y,
      int z,
      float u,
      float v,
      int normalX,
      int normalY,
      int normalZ,
      int packedLight
  ) {
    consumer.addVertex(pose, (float) x, (float) y, (float) z)
        .setColor(-1)
        .setUv(u, v)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setLight(packedLight)
        .setNormal(pose, (float) normalX, (float) normalZ, (float) normalY);
  }
}
