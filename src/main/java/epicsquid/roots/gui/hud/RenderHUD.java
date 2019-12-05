package epicsquid.roots.gui.hud;

import epicsquid.roots.Roots;
import epicsquid.roots.block.BonfireBlock;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Roots.MODID)
public class RenderHUD {
  private RenderHUD() {
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
    if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

    Minecraft mc = Minecraft.getInstance();
    if (mc.currentScreen instanceof ChatScreen) return;

    RayTraceResult trace = mc.objectMouseOver;
    if (trace == null || trace.getType() != RayTraceResult.Type.BLOCK) return;

    BlockState state = mc.world.getBlockState(new BlockPos(trace.getHitVec()));
    Block block = state.getBlock();

    BlockPos pos = new BlockPos(trace.getHitVec());
    if (block == ModBlocks.mortar) {
      RenderMortar.render(mc, pos, state, event);
    } else if (block instanceof BonfireBlock) {
      RenderBonfire.render(mc, pos, state, event);
    }
  }
}
