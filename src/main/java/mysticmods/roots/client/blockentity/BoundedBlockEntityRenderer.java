package mysticmods.roots.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.api.blockentity.Bounded;
import mysticmods.roots.client.RenderUtil;
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
import net.minecraft.world.phys.shapes.Shapes;
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
    if (Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes()) {
      AABB bounds = pBlockEntity.getAABB();
      if (bounds != null) {
        RenderUtil.renderAABB(pPoseStack, pBufferSource, bounds, pBlockEntity.getBlockPos());
        RenderUtil.renderAABB(pPoseStack, pBufferSource, BlockPos.ZERO, pBlockEntity.getBlockPos(), null, null);
      }
    }
    renderText(pBlockEntity, pPoseStack, pBufferSource, pPackedLight, pPartialTick);
  }

  protected boolean isInsideBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
    Vec3 projectedView = context.getBlockEntityRenderDispatcher().camera.getPosition();
    return minX <= projectedView.x && projectedView.x <= maxX && minY <= projectedView.y && projectedView.y <= maxY && minZ <= projectedView.z && projectedView.z <= maxZ;
  }

  protected Component getTextToRender(T blockEntity) {
    return Component.empty();
  }

  protected void renderText(T blockEntity, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float partialTick) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player == null) {
      return;
    }

    double d0 = mc.player.distanceToSqr(Vec3.atCenterOf(blockEntity.getBlockPos()));
    if (d0 > 4096.0f) {
      return;
    }

    Component displayName = getTextToRender(blockEntity);
    if (displayName.getContents().equals(PlainTextContents.EMPTY)) {
      return;
    }
    poseStack.pushPose();
    poseStack.translate(0.5, 1.8, 0.5);
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

  @Override
  public AABB getRenderBoundingBox(T blockEntity) {
    return renderBounds.move(blockEntity.getBlockPos());
  }
}
