package mysticmods.roots.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.blockentity.VisibleBlockEntity;
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
import net.minecraft.world.phys.shapes.VoxelShape;

public class VisibleBlockEntityRenderer implements BlockEntityRenderer<VisibleBlockEntity> {
  protected final BlockEntityRendererProvider.Context context;

  public VisibleBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    this.context = context;
  }

  @Override
  public void render(VisibleBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
    if (Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes()) {
      pPoseStack.pushPose();
      BlockPos position = pBlockEntity.getBlockPos();
      VoxelShape pShape = pBlockEntity.getBlockState().getShape(pBlockEntity.getLevel(), position);
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
            .setColor(1f, 0.5f, 0.25f, 1f).setNormal(pose, f, f1, f2);
        pConsumer.addVertex(pose.pose(), (float) (pMaxX), (float) (pMaxY), (float) (pMaxZ))
            .setColor(1f, 0.5f, 0.25f, 1f).setNormal(pose, f, f1, f2);
      });
      pPoseStack.popPose();
    }
  }

  @Override
  public int getViewDistance() {
    return 64 * 64;
  }

  @Override
  public boolean shouldRenderOffScreen(VisibleBlockEntity pBlockEntity) {
    return true;
  }
}
