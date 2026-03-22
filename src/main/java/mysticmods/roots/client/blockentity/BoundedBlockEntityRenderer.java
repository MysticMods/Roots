package mysticmods.roots.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.api.blockentity.Bounded;
import mysticmods.roots.client.KeyBindings;
import mysticmods.roots.client.RenderUtil;
import mysticmods.roots.client.gui.layer.HudOverlay;
import mysticmods.roots.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4f;

public class BoundedBlockEntityRenderer<T extends BlockEntity & Bounded> implements BlockEntityRenderer<T> {
  protected final BlockEntityRendererProvider.Context context;

  private final AABB renderBounds;

  public BoundedBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    this.context = context;
    this.renderBounds = new AABB(-30, -30, -30, 30, 30, 30);
  }

  @Override
  public void render(T pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
    var pos = pBlockEntity.getBlockPos();
    var highlighted = pos.equals(HudOverlay.getStoredBlockPos());
    if (highlighted) {
      if (ConfigManager.HIGHLIGHT_LAST_BLOCK.getAsBoolean()) {
        pPoseStack.pushPose();
        VoxelShape pShape = pBlockEntity.getBlockState()
            .getCollisionShape(pBlockEntity.getLevel(), pBlockEntity.getBlockPos());
        VertexConsumer pConsumer = pBufferSource.getBuffer(RenderType.lines());
        PoseStack.Pose pose = pPoseStack.last();

        double pX = 0;
        double pY = 0;
        double pZ = 0;
        float pRed, pGreen, pBlue;
        float pAlpha = 1f;

        pRed = 0.4f;
        pGreen = 0.1f;
        pBlue = 0.4f;

        pShape.forAllEdges((pMinX, pMinY, pMinZ, pMaxX, pMaxY, pMaxZ) -> {
          float f = (float) (pMaxX - pMinX);
          float f1 = (float) (pMaxY - pMinY);
          float f2 = (float) (pMaxZ - pMinZ);
          float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
          f /= f3;
          f1 /= f3;
          f2 /= f3;
          pConsumer.addVertex(pose.pose(), (float) (pMinX + pX), (float) (pMinY + pY), (float) (pMinZ + pZ))
              .setColor(pRed, pGreen, pBlue, pAlpha).setNormal(pose, f, f1, f2);
          pConsumer.addVertex(pose.pose(), (float) (pMaxX + pX), (float) (pMaxY + pY), (float) (pMaxZ + pZ))
              .setColor(pRed, pGreen, pBlue, pAlpha).setNormal(pose, f, f1, f2);
        });
        pPoseStack.popPose();
      }
      if (ConfigManager.SHOW_INSERT_IN_GAME_MESSAGE.getAsBoolean() && HudOverlay.shouldShowInsert(pos)) {
        Component overlayMessageString = Component.translatable("roots.hud.fake_menu", KeyBindings.OPEN_FAKE_MENU.getTranslatedKeyMessage(), pBlockEntity.getBlockState()
            .getBlock().getName());
        renderText(pBlockEntity, pPoseStack, pBufferSource, pPackedLight, pPartialTick, 0.6, overlayMessageString);
      }
      if (ConfigManager.SHOW_DELETE_IN_GAME_MESSAGE.getAsBoolean() && HudOverlay.shouldShowDelete(pos)) {
        Component overlayMessageString = Component.translatable("roots.hud.clear", KeyBindings.CLEAR_CONTAINER.getTranslatedKeyMessage(), pBlockEntity.getBlockState()
            .getBlock().getName());
        renderText(pBlockEntity, pPoseStack, pBufferSource, pPackedLight, pPartialTick, 1, overlayMessageString);
      }
    }
    if (Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes()) {
      AABB bounds = pBlockEntity.getAABB();
      if (bounds != null) {
        RenderUtil.renderAABB(pPoseStack, pBufferSource, bounds, pBlockEntity.getBlockPos());
        RenderUtil.renderAABB(pPoseStack, pBufferSource, BlockPos.ZERO, pBlockEntity.getBlockPos(), null, null);
      }
    }
    renderText(pBlockEntity, pPoseStack, pBufferSource, pPackedLight, pPartialTick, 1.8, getTextToRender(pBlockEntity));
  }

  protected Component getTextToRender(T blockEntity) {
    return Component.empty();
  }

  protected void renderText(T blockEntity, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float partialTick, double yOffset, Component displayName) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player == null) {
      return;
    }

    double d0 = mc.player.distanceToSqr(Vec3.atCenterOf(blockEntity.getBlockPos()));
    if (d0 > 4096.0f) {
      return;
    }

    if (displayName.getContents().equals(PlainTextContents.EMPTY)) {
      return;
    }
    poseStack.pushPose();
    poseStack.translate(0.5, yOffset, 0.5);
    poseStack.mulPose(this.context.getEntityRenderer().cameraOrientation()); // ???
    poseStack.scale(0.025F, -0.025F, 0.025F);
    Matrix4f matrix4f = poseStack.last().pose();
    float f = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
    int j = (int) (f * 255.0F) << 24;
    Font font = Minecraft.getInstance().font;
    float f1 = (float) (-font.width(displayName) / 2);
    font.drawInBatch(displayName, f1, (float) 0, 553648127, false, matrix4f, bufferSource, Font.DisplayMode.SEE_THROUGH, j, packedLight);
    font.drawInBatch(displayName, f1, (float) 0, -1, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, packedLight);
    poseStack.popPose();
  }

  @Override
  public int getViewDistance() {
    return 64 * 64;
  }

  @Override
  public boolean shouldRenderOffScreen(T pBlockEntity) {
    return true;
  }

  // TODO: What's the point of having both?
  @Override
  public AABB getRenderBoundingBox(T blockEntity) {
    return renderBounds.move(blockEntity.getBlockPos());
  }
}
