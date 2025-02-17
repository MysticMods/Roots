package mysticmods.roots.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.ClientSetup;
import mysticmods.roots.client.RenderTickHandler;
import mysticmods.roots.client.model.ModelHolder;
import mysticmods.roots.client.model.SproutModel;
import mysticmods.roots.entity.SproutEntity;
import mysticmods.roots.init.ModEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.HashMap;
import java.util.Map;

public class SproutRenderer extends MobRenderer<SproutEntity, SproutModel> {
  public SproutRenderer(EntityRendererProvider.Context context) {
    super(context, new SproutModel(context.bakeLayer(ModelHolder.SPROUT)), 0.15f);
  }

  private static final Map<EntityType<?>, ResourceLocation> textures = new HashMap<>();

  @Override
  public ResourceLocation getTextureLocation(SproutEntity entity) {
    if (textures.isEmpty()) {
      textures.put(ModEntities.GREEN_SPROUT.get(), RootsAPI.rl("textures/entity/sprout_green.png"));
      textures.put(ModEntities.TAN_SPROUT.get(), RootsAPI.rl("textures/entity/sprout_tan.png"));
      textures.put(ModEntities.PURPLE_SPROUT.get(), RootsAPI.rl("textures/entity/sprout_purple.png"));
      textures.put(ModEntities.RED_SPROUT.get(), RootsAPI.rl("textures/entity/sprout_red.png"));
    }
    ResourceLocation result = textures.get(entity.getType());
    if (result == null) {
      result = textures.get(ModEntities.RED_SPROUT.get());
    }

    return result;
  }

  @Override
  public void render(SproutEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
    super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

    if (entity.hasGift() && ClientSetup.GIFT_BOX_MODEL != null) {
      double d0 = this.entityRenderDispatcher.distanceToSqr(entity);
      if (d0 < 180) {
        Vec3 vec3 = entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getViewYRot(partialTicks));
        if (vec3 != null) {
          poseStack.pushPose();
          float ticks = RenderTickHandler.getClientTicks() + partialTicks;
          float bobbingOffset = Mth.sin(ticks * 0.1f) * 0.05f;
          float pulse = 0.05f * Mth.sin(ticks * 0.1f) + 0.95f;
          poseStack.translate(0f, entity.getBbHeight() + entity.getBbHeight() * 0.2 + bobbingOffset, 0);
          poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
          poseStack.scale(0.3f * pulse, 0.3f * pulse, 0.3f * pulse);
          poseStack.translate(-0.5f, 0, -0.5f);
          Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
              poseStack.last(),
              buffer.getBuffer(Sheets.translucentItemSheet()),
              null,
              ClientSetup.GIFT_BOX_MODEL,
              1,
              1,
              1,
              LightTexture.FULL_SKY,
              OverlayTexture.NO_OVERLAY,
              ModelData.EMPTY,
              Sheets.translucentItemSheet());
          poseStack.popPose();
        }
      }
    }
  }
}
