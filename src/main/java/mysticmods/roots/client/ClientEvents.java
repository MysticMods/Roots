package mysticmods.roots.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.mixin.AccessorMixinEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.model.data.ModelData;

@EventBusSubscriber(modid = RootsAPI.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientEvents {
  @SubscribeEvent
  public static void onRecipeUpdate(RecipesUpdatedEvent event) {
    ResolvedRecipes.reset();
  }

  @SubscribeEvent
  public static void onEntityRender (RenderLivingEvent.Post<?, ?> event) {
    if (event.getEntity().hasEffect(ModEffects.GEAS)) {
      LivingEntityRenderer<?, ?> renderer = event.getRenderer();
      LivingEntity entity = event.getEntity();
      PoseStack poseStack = event.getPoseStack();
      MultiBufferSource buffer = event.getMultiBufferSource();
      float partialTicks = event.getPartialTick();
        Vec3 vec3 = entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getViewYRot(partialTicks));
        if (vec3 != null) {
          poseStack.pushPose();
          float ticks = RenderTickHandler.getClientTicks() + partialTicks;
          float bobbingOffset = Mth.sin(ticks * 0.1f) * 0.05f;
          float pulse = 0.05f * Mth.sin(ticks * 0.1f) + 0.95f;
          poseStack.translate(0f, entity.getBbHeight() + entity.getBbHeight() * 0.2 + bobbingOffset, 0);
          poseStack.mulPose(((AccessorMixinEntityRenderer)renderer).getEntityRenderDispatcher().cameraOrientation());
          poseStack.scale(0.3f * pulse, 0.3f * pulse, 0.3f * pulse);
          poseStack.translate(-0.5f, 0, -0.5f);
          Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
              poseStack.last(),
              buffer.getBuffer(Sheets.translucentItemSheet()),
              null,
              ClientSetup.GEAS_MODEL,
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
