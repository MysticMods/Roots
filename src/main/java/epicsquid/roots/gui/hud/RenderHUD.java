package epicsquid.roots.gui.hud;

import epicsquid.roots.Roots;
import epicsquid.roots.block.BlockBonfire;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Roots.MODID)
public class RenderHUD {
  private RenderHUD() {
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
    if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

    Minecraft mc = Minecraft.getMinecraft();
    if (mc.currentScreen instanceof GuiChat) return;

    RayTraceResult trace = mc.objectMouseOver;
    if (trace == null || trace.typeOfHit != RayTraceResult.Type.BLOCK) return;

    IBlockState state = mc.world.getBlockState(trace.getBlockPos());
    Block block = state.getBlock();

    if (block == ModBlocks.mortar) {
      RenderMortar.render(mc, trace.getBlockPos(), state, event);
    } else if (block instanceof BlockBonfire) {
      RenderBonfire.render(mc, trace.getBlockPos(), state, event);
    }
  }
}
