package mysticmods.roots.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.api.blockentity.BoundedBlockEntity;
import mysticmods.roots.client.Model3D;
import mysticmods.roots.client.RootsRenderer;
import mysticmods.roots.util.EnumUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BoundedBlockEntityRenderer<T extends BlockEntity & BoundedBlockEntity> implements BlockEntityRenderer<T> {
  protected final BlockEntityRendererProvider.Context context;

  private final Model3D model = null;
  // TODO: dynamic bounds?
  private AABB bounds = null;

  private static final int[] colors = new int[EnumUtils.DIRECTIONS.length];

  static {
    colors[Direction.DOWN.ordinal()] = RootsRenderer.getColorARGB(255, 255, 255, 0.62F);
    colors[Direction.UP.ordinal()] = RootsRenderer.getColorARGB(255, 255, 255, 0.62F);
    colors[Direction.NORTH.ordinal()] = RootsRenderer.getColorARGB(255, 255, 255, 0.6F);
    colors[Direction.SOUTH.ordinal()] = RootsRenderer.getColorARGB(255, 255, 255, 0.6F);
    colors[Direction.WEST.ordinal()] = RootsRenderer.getColorARGB(255, 255, 255, 0.58F);
    colors[Direction.EAST.ordinal()] = RootsRenderer.getColorARGB(255, 255, 255, 0.58F);
  }

  public BoundedBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    this.context = context;
  }

  @Override
  public void render(T pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
    if (bounds == null) {
      BoundingBox box = pBlockEntity.getBoundingBox();
      if (box != null) {
        bounds = AABB.of(box);
      }
    }
    if (Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes()) {
      if (bounds != null) {
        pPoseStack.pushPose();
        BlockPos position = pBlockEntity.getBlockPos();
        VoxelShape pShape = Shapes.create(bounds.move(-position.getX(), -position.getY(), -position.getZ()));
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
          pConsumer.vertex(pose.pose(), (float) (pMinX), (float) (pMinY), (float) (pMinZ)).color(1f, 0.5f, 0.25f, 1f).normal(pose.normal(), f, f1, f2).endVertex();
          pConsumer.vertex(pose.pose(), (float) (pMaxX), (float) (pMaxY), (float) (pMaxZ)).color(1f, 0.5f, 0.25f, 1f).normal(pose.normal(), f, f1, f2).endVertex();
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
    return 64 * 4;
  }

  @Override
  public boolean shouldRenderOffScreen(T pBlockEntity) {
    return true;
  }
}
