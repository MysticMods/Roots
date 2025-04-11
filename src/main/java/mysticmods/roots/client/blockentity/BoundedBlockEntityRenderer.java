package mysticmods.roots.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.api.blockentity.BoundedBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BoundedBlockEntityRenderer<T extends BlockEntity & BoundedBlockEntity> implements BlockEntityRenderer<T> {
  protected final BlockEntityRendererProvider.Context context;

  private AABB renderBounds = null;

  public BoundedBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    this.context = context;
    this.renderBounds = new AABB(-30, -30, -30, 30, 30, 30);
  }

  @Override
  public void render(T pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
    if (Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes()) {
      AABB bounds = pBlockEntity.getAABB();
      if (bounds != null) {
        pPoseStack.pushPose();
        BlockPos position = pBlockEntity.getBlockPos();
        ColorHelper.Color color = ColorHelper.color(position);
        VoxelShape pShape = Shapes.create(bounds);
        VertexConsumer pConsumer = pBufferSource.getBuffer(RenderType.lines());
        PoseStack.Pose pose = pPoseStack.last();

        pShape.forAllEdges((pMinX, pMinY, pMinZ, pMaxX, pMaxY, pMaxZ) -> {
          float f = (float) (pMaxX - pMinX);
          float f1 = (float) (pMaxY - pMinY);
          float f2 = (float) (pMaxZ - pMinZ);
          float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
          f /= f3;
          f1 /= f3;
          f2 /= f3;
          pConsumer.addVertex(pose.pose(), (float) (pMinX), (float) (pMinY), (float) (pMinZ))
              .setColor(color.r(), color.g(), color.b(), color.a()).setNormal(pose, f, f1, f2);
          pConsumer.addVertex(pose.pose(), (float) (pMaxX), (float) (pMaxY), (float) (pMaxZ))
              .setColor(color.r(), color.g(), color.b(), color.a()).setNormal(pose, f, f1, f2);
        });
        pPoseStack.popPose();
      }
    }
  }

  protected boolean isInsideBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
    Vec3 projectedView = context.getBlockEntityRenderDispatcher().camera.getPosition();
    return minX <= projectedView.x && projectedView.x <= maxX && minY <= projectedView.y && projectedView.y <= maxY && minZ <= projectedView.z && projectedView.z <= maxZ;
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
