package mysticmods.roots.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.client.gui.layer.HerbLayer;
import mysticmods.roots.client.particle.Beam;
import mysticmods.roots.client.particle.BeamManager;
import mysticmods.roots.client.particle.bolt.BoltRenderer;
import mysticmods.roots.client.particle.bolt.IBoltEffect;
import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.client.particle.screen.ScreenParticleEngine;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.item.CastingItem;
import mysticmods.roots.mixin.client.accessor.AccessorMixinLevelRenderer;
import mysticmods.roots.mixin.client.accessor.AccessorMixinParticleEngine;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.util.Map;
import java.util.Queue;

@EventBusSubscriber(value = Dist.CLIENT, modid = RootsAPI.MODID, bus = EventBusSubscriber.Bus.GAME)
public class RenderTickHandler {
  private static float clientTicks = 0;

  private static boolean outliningArea = false;
  private static final BoltRenderer boltRenderer = new BoltRenderer();

  private static boolean renderingDelayedParticles = false;

  public static final ImmutableList<ParticleRenderType> RENDER_ORDER = ImmutableList.of(
      RootsParticleRenderTypes.DELAYED_OPAQUE,
      RootsParticleRenderTypes.DELAYED_TRANSLUCENT,
      RootsParticleRenderTypes.DELAYED_TRANSLUCENT_NO_CULL,
      RootsParticleRenderTypes.DELAYED_ADDITIVE
  );

  private static final ImmutableMap<ParticleRenderType, RenderType> PARTICLE_RENDER_TYPES = ImmutableMap.of(
      RootsParticleRenderTypes.DELAYED_OPAQUE, RootsRenderTypes.DELAYED_PARTICLES,
      RootsParticleRenderTypes.DELAYED_TRANSLUCENT, RootsRenderTypes.TRANSLUCENT_DELAYED_PARTICLES,
      RootsParticleRenderTypes.DELAYED_TRANSLUCENT_NO_CULL, RootsRenderTypes.TRANSLUCENT_DELAYED_PARTICLES_NO_CULL,
      RootsParticleRenderTypes.DELAYED_ADDITIVE, RootsRenderTypes.ADDITIVE_DELAYED
  );

  public static boolean isRenderingDelayedParticles() {
    return renderingDelayedParticles;
  }

  public static float getPartialTick() {
    return Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
  }

  public static void renderBolt(Object renderer, IBoltEffect bolt) {
    boltRenderer.update(renderer, bolt, getPartialTick());
  }

  public static void renderBeam(Beam beam) {
    BeamManager.addBeam(beam);
  }

  @SubscribeEvent
  public static void onRenderGui (RenderGuiLayerEvent.Post event) {
    ResourceLocation layer = event.getName();
    if (layer.equals(VanillaGuiLayers.SAVING_INDICATOR)) {
      GuiGraphics graphics = event.getGuiGraphics();
      PoseStack pose = graphics.pose();
      pose.pushPose();
      pose.translate(0, 0, 1000);
      ScreenParticleEngine.render(event.getPartialTick().getGameTimeDeltaPartialTick(false));
      pose.popPose();
    }
  }

  @SubscribeEvent
  public static void onRenderStage(RenderLevelStageEvent event) {
    if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
      clientTicks += event.getPartialTick().getGameTimeDeltaPartialTick(false);
    }
    MultiBufferSource.BufferSource renderer = Minecraft.getInstance().renderBuffers().bufferSource();
    if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
      if (boltRenderer.hasBoltsToRender()) {
        boltRenderer.render(event.getPartialTick()
            .getGameTimeDeltaPartialTick(false), event.getPoseStack(), renderer, event.getCamera().getPosition());
      }

      BeamManager.render(event.getPartialTick()
          .getGameTimeDeltaPartialTick(false), event.getPoseStack(), renderer, event.getCamera().getPosition());
      renderer.endLastBatch();

      renderingDelayedParticles = true;
      var allParticles = ((AccessorMixinParticleEngine) Minecraft.getInstance().particleEngine).rootsGetParticles();

      Frustum frustum = event.getFrustum();
      float partialTick = getPartialTick();
      Camera camera = event.getCamera();

      for (ParticleRenderType type : RENDER_ORDER) {
        Queue<Particle> particles = allParticles.get(type);

        if (particles == null || particles.isEmpty()) {
          continue;
        }

        RenderType renderType = PARTICLE_RENDER_TYPES.get(type);
        if (renderType == null) {
          RootsAPI.LOG.error("No render type found for particle render type: {}", type);
          continue;
        }

        VertexConsumer consumer = renderer.getBuffer(renderType);
        for (Particle particle : particles) {
          if (!frustum.isVisible(particle.getRenderBoundingBox(partialTick))) continue;
          try {
            particle.render(consumer, camera, partialTick);
          } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.forThrowable(throwable, "Rendering Particle");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Particle being rendered");
            crashreportcategory.setDetail("Particle", particle::toString);
            crashreportcategory.setDetail("Particle Type", type::toString);
            renderingDelayedParticles = false;
            throw new ReportedException(crashreport);
          }
        }
        renderer.endBatch(renderType);
      }
      RenderSystem.disableBlend();
    }

    renderingDelayedParticles = false;
  }

  @SubscribeEvent
  public static void onClientTick(ClientTickEvent.Post post) {
    HerbLayer.tick();
    BeamManager.tick();
    // TODO: Check pausing
    ScreenParticleEngine.tick();
  }

  // This is stolen from Mekanism
  @SubscribeEvent
  public static void onBlockHighlight(RenderHighlightEvent.Block event) {
    Minecraft minecraft = Minecraft.getInstance();
    Player player = minecraft.player;
    if (player == null) {
      return;
    }

    BlockHitResult rayTraceResult = event.getTarget();
    if (rayTraceResult.getType() != HitResult.Type.MISS) {
      Level level = player.level();
      BlockPos pos = rayTraceResult.getBlockPos();
      MultiBufferSource buffer = event.getMultiBufferSource();
      Camera info = event.getCamera();
      PoseStack matrix = event.getPoseStack();
      BlockState blockState = level.getBlockState(pos);
      if (!outliningArea) {
        ItemStack stack = player.getMainHandItem();
        if (!stack.isEmpty() && stack.getItem() instanceof CastingItem staff) {
          SpellStorage storage = stack.get(ModAttachments.SPELL_STORAGE);
          if (storage == null) {
            return;
          }

          ISpellInstance spell = storage.getCurrentSpell();
          if (spell == null) {
            return;
          }

          Map<BlockPos, BlockState> blocks = spell.getSpell()
              .getAffectedBlocks(level, player, spell, stack, pos, blockState, rayTraceResult);
          if (!blocks.isEmpty()) {
            outliningArea = true;
            Vec3 renderView = info.getPosition();
            LevelRenderer renderer = event.getLevelRenderer();
            VertexConsumer consumer = buffer.getBuffer(RenderType.lines());
            for (Map.Entry<BlockPos, BlockState> entry : blocks.entrySet()) {
              BlockPos target = entry.getKey();
              if (!pos.equals(target) && !ClientHooks.onDrawHighlight(renderer, info, rayTraceResult.withPosition(target), event.getDeltaTracker(), matrix, buffer)) {
                ((AccessorMixinLevelRenderer) renderer).rootsCallRenderHitOutline(matrix, consumer, player, renderView.x, renderView.y, renderView.z, target, entry.getValue());
              }
            }
            outliningArea = false;
          }
        }
      }
    }
  }

  public static float getClientTicks() {
    return clientTicks;
  }
}
