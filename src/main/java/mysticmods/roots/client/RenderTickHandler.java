package mysticmods.roots.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.item.CastingItem;
import mysticmods.roots.mixin.AccessorMixinLevelRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
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
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.Map;

@EventBusSubscriber(value = Dist.CLIENT, modid = RootsAPI.MODID, bus = EventBusSubscriber.Bus.GAME)
public class RenderTickHandler {
  private static float clientTicks = 0;

  private static boolean outliningArea = false;

  @SubscribeEvent
  public static void onRenderStage(RenderLevelStageEvent event) {
    if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
      clientTicks += event.getPartialTick().getGameTimeDeltaPartialTick(false);
    }
  }

  public static void onClientTick(ClientTickEvent.Post post) {
    Minecraft minecraft = Minecraft.getInstance();
    Level level;
    //noinspection ConstantValue
    if (minecraft.player != null && ((level = minecraft.player.level()) != null) && minecraft.gameMode != null) {
    }
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
                ((AccessorMixinLevelRenderer) renderer).callRenderHitOutline(matrix, consumer, player, renderView.x, renderView.y, renderView.z, target, entry.getValue());
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
