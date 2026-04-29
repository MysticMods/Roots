package mysticmods.roots.client;

import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.client.RootsClientAPI;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.item.GramaryItem;
import mysticmods.roots.mixin.client.accessor.AccessorMixinEntityRenderer;
import mysticmods.roots.mixin.client.accessor.AccessorMixinTextureAtlas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber(modid = RootsAPI.MODID, value = Dist.CLIENT)
public class ClientEvents {
  private static Vec2 BLOCKS_ATLAS_SIZE = null;

  public static Vec2 getBlocksAtlasSize() {
    if (BLOCKS_ATLAS_SIZE == null) {
      RootsAPI.LOG.error("Blocks atlas size not initialized, fetching from Minecraft instance.");
      TextureAtlas atlas = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);
      BLOCKS_ATLAS_SIZE = new Vec2(((AccessorMixinTextureAtlas) atlas).rootsGetWidth(), ((AccessorMixinTextureAtlas) atlas).rootsGetHeight());
    }
    return BLOCKS_ATLAS_SIZE;
  }

  @SubscribeEvent
  public static void onRenderNameTag(RenderNameTagEvent event) {
    if (event.getEntity().getType()
        .equals(EntityType.PLAYER) && event.getEntity() instanceof Player player && player.hasEffect(ModEffects.LIGHT_DRIFTER)) {
      event.setCanRender(TriState.TRUE);
    }
  }

  @SubscribeEvent
  public static void onLevelDestroyed(LevelEvent.Unload event) {
    RenderUtil.clearEntityMap();
  }

  @SubscribeEvent
  public static void onClientLogOut(ClientPlayerNetworkEvent.LoggingOut event) {
    RenderUtil.clearEntityMap();
  }

  @SubscribeEvent
  public static void onTextureStitched(TextureAtlasStitchedEvent event) {
    TextureAtlas atlas = event.getAtlas();
    if (atlas.location().equals(TextureAtlas.LOCATION_BLOCKS)) {
      BLOCKS_ATLAS_SIZE = new Vec2(((AccessorMixinTextureAtlas) atlas).rootsGetWidth(), ((AccessorMixinTextureAtlas) atlas).rootsGetHeight());
    }
  }

  @SubscribeEvent
  public static void onRecipeUpdate(RecipesUpdatedEvent event) {
    ResolvedRecipes.reset();
  }

  @SubscribeEvent
  public static void onEntityRender(RenderLivingEvent.Post<?, ?> event) {
    if (RootsClientAPI.isGramaryMode(GramaryItem.GramaryMode.ENTITY_INFO) && event.getEntity().getType()
        .is(RootsTags.Entities.SHOULD_RENDER_HUD)) {

    }

    if (event.getEntity().getData(ModAttachments.HAS_GEAS)) {
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
        poseStack.mulPose(((AccessorMixinEntityRenderer) renderer).rootsGetEntityRenderDispatcher()
            .cameraOrientation());
        poseStack.scale(0.3f * pulse, 0.3f * pulse, 0.3f * pulse);
        poseStack.translate(-0.5f, 0, -0.5f);
        // Re: #1289 this should only execute if the entity has geas
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
