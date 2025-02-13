package mysticmods.roots.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.model.MeteorModel;
import mysticmods.roots.client.model.ModelHolder;
import mysticmods.roots.entity.projectile.MeteorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class MeteorRenderer extends EntityRenderer<MeteorEntity> implements RenderLayerParent<MeteorEntity, MeteorModel> {
  protected final MeteorModel model;

  public MeteorRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.model = new MeteorModel(context.bakeLayer(ModelHolder.METEOR));
  }

  public static final ResourceLocation TEXTURE = RootsAPI.rl("textures/entity/meteor.png");

  @Override
  public MeteorModel getModel() {
    return model;
  }

  @Override
  public ResourceLocation getTextureLocation(MeteorEntity entity) {
    return TEXTURE;
  }

  @Override
  protected boolean shouldShowName(MeteorEntity entity) {
    return false;
  }

  @Override
  protected float getShadowRadius(MeteorEntity entity) {
    return 0f;
  }

  @Override
  public void render(MeteorEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
    poseStack.pushPose();

    float f6 = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
    this.model.prepareMobModel(entity, 0f, 0f, partialTicks);
    this.model.setupAnim(entity, 0f, 0f, entity.tickCount + partialTicks, 0f, f6);
    Minecraft minecraft = Minecraft.getInstance();
    boolean flag1 = !entity.isInvisibleTo(minecraft.player);
    RenderType rendertype = RenderType.entitySolid(this.getTextureLocation(entity));
    VertexConsumer vertexconsumer = buffer.getBuffer(rendertype);
    this.model.renderToBuffer(poseStack, vertexconsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, flag1 ? 654311423 : -1);
    poseStack.popPose();
    super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
  }
}
