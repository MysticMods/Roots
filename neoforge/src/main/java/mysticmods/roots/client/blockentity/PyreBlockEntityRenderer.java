package mysticmods.roots.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import mysticmods.roots.blockentity.PyreBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import noobanidus.libs.particleslib.client.events.RenderTickHandler;

import java.util.List;
import java.util.Random;

public class PyreBlockEntityRenderer extends BoundedBlockEntityRenderer<PyreBlockEntity> {
  private final Random random = new Random();

  public PyreBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  public void render(PyreBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
    super.render(pBlockEntity, pPartialTick, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay);

    if (Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes() && pBlockEntity.getCurrentRitual() != null) {
      AABB bounds = pBlockEntity.getCurrentRitual().getAABB();
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

    List<ItemStack> items = pBlockEntity.getNonEmptyItems();

    for (int i = 0; i < items.size(); i++) {
      ItemStack item = items.get(i);
      pPoseStack.pushPose();
      random.setSeed(item.hashCode());
      float shifted = (float) (RenderTickHandler.getClientTicks() + pPartialTick + i * (360.0 / items.size()));
      pPoseStack.translate(0.5, 0.5 + 0.05 * ((double) Mth.sin((float) Math.toRadians((double) shifted * 4))), 0.5);
      pPoseStack.mulPose(Vector3f.YP.rotationDegrees(shifted));
      pPoseStack.translate(-0.5, 0, 0);
      pPoseStack.mulPose(Vector3f.YP.rotationDegrees(shifted));
      pPoseStack.scale(0.4f, 0.4f, 0.4f);
      Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemTransforms.TransformType.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, 0);
      pPoseStack.popPose();
    }
  }
}
