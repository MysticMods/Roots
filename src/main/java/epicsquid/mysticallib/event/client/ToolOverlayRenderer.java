package epicsquid.mysticallib.event.client;

import epicsquid.mysticallib.MysticalLib;
import epicsquid.mysticallib.item.tool.IEffectiveTool;
import epicsquid.mysticallib.item.tool.ISizedTool;
import epicsquid.mysticallib.util.BreakUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber(value= Side.CLIENT, modid= MysticalLib.MODID)
public class ToolOverlayRenderer {
  @SubscribeEvent(priority = EventPriority.LOW)
  public static void renderExtraBlockStuff(RenderWorldLastEvent event) {
    Minecraft mc = Minecraft.getMinecraft();
    PlayerController controller = mc.playerController;

    if (controller == null) {
      return;
    }

    PlayerEntity player = MysticalLib.proxy.getPlayer();
    if (player == null) {
      return;
    }
    if (player.isSneaking()) {
      return;
    }
    ItemStack tool = player.getHeldItemMainhand();
    Item toolItem = tool.getItem();
    if (toolItem instanceof ISizedTool) {
      Entity renderEntity = Minecraft.getMinecraft().getRenderViewEntity();
      if (renderEntity == null) {
        return;
      }

      RayTraceResult ray = BreakUtil.rayTrace(player.world, player);
      if (ray == null || ray.typeOfHit != RayTraceResult.Type.BLOCK) {
        return;
      }

      final Set<BlockPos> positions = BreakUtil.nearbyBlocks(tool, ray.getBlockPos(), player);
      if (positions.isEmpty()) {
        return;
      }
      for (BlockPos position : positions) {
        event.getContext().drawSelectionBox(player, new RayTraceResult(RayTraceResult.Type.BLOCK, new Vec3d(0, 0, 0), ray.sideHit, position), 0, mc.getRenderPartialTicks());
      }

      final boolean drawDamage = toolItem instanceof IEffectiveTool && ((IEffectiveTool) toolItem).displayBreak();

      if (controller.getIsHittingBlock() && drawDamage) {
        drawBlockDamage(player.world, Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), player, positions, ray.getBlockPos());
      }
    }
  }

  private static void preRenderDamagedBlocks() {
    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    GlStateManager.enableBlend();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
    GlStateManager.doPolygonOffset(-1.0F, -10.0F);
    GlStateManager.enablePolygonOffset();
    GlStateManager.alphaFunc(516, 0.1F);
    GlStateManager.enableAlpha();
    GlStateManager.pushMatrix();
  }

  private static void postRenderDamagedBlocks() {
    GlStateManager.disableAlpha();
    GlStateManager.doPolygonOffset(0.0F, 0.0F);
    GlStateManager.disablePolygonOffset();
    GlStateManager.enableAlpha();
    GlStateManager.depthMask(true);
    GlStateManager.popMatrix();
  }

  public static void drawBlockDamage(World world, Tessellator tessellator, BufferBuilder bufferBuilder, PlayerEntity player, Set<BlockPos> positions, BlockPos origin) {
    final Minecraft mc = Minecraft.getMinecraft();
    float partialTicks = mc.getRenderPartialTicks();
    DestroyBlockProgress progress = null;

    for (Map.Entry<Integer, DestroyBlockProgress> entry : mc.renderGlobal.damagedBlocks.entrySet()) {
      if (entry.getValue().getPosition().equals(origin)) {
        progress = entry.getValue();
        break;
      }
    }

    if (progress == null) {
      return;
    }

    double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) partialTicks;
    double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) partialTicks;
    double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) partialTicks;

    Minecraft.getMinecraft().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
    preRenderDamagedBlocks();
    bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
    bufferBuilder.setTranslation(-d0, -d1, -d2);
    bufferBuilder.noColor();

    for (BlockPos blockpos : positions) {
      TileEntity te = world.getTileEntity(blockpos);
      boolean hasBreak = te != null && te.canRenderBreaking();

      if (!hasBreak) {
        BlockState state = world.getBlockState(blockpos);
        if (state.getMaterial() != Material.AIR) {
          mc.getBlockRendererDispatcher().renderBlockDamage(state, blockpos, mc.renderGlobal.destroyBlockIcons[progress.getPartialBlockDamage()], world);
        }
      }
    }

    tessellator.draw();
    bufferBuilder.setTranslation(0.0D, 0.0D, 0.0D);
    postRenderDamagedBlocks();
  }
}
