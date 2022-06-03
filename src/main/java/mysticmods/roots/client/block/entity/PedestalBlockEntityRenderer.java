package mysticmods.roots.client.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import mysticmods.roots.block.PedestalBlock;
import mysticmods.roots.block.entity.PedestalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import noobanidus.libs.particleslib.client.events.RenderTickHandler;

public class PedestalBlockEntityRenderer implements BlockEntityRenderer<PedestalBlockEntity> {
  public PedestalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
  }

  // TODO: Render more when there's more
  @Override
  public void render(PedestalBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
    ItemStack inSlot = pBlockEntity.getHeldItem();
    if (Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes()) {
      pPoseStack.pushPose();
      VoxelShape pShape = pBlockEntity.getBlockState().getCollisionShape(pBlockEntity.getLevel(), pBlockEntity.getBlockPos());
      VertexConsumer pConsumer = pBufferSource.getBuffer(RenderType.lines());
      PoseStack.Pose pose = pPoseStack.last();

      double pX = 0;
      double pY = 0;
      double pZ = 0;
      float pRed, pGreen, pBlue;
      float pAlpha = 1f;

      BlockState state = pBlockEntity.getBlockState();

      if (state.hasProperty(PedestalBlock.VALID) && state.getValue(PedestalBlock.VALID)) {
        pRed = 0f;
        pGreen = 1f;
        pBlue = 0f;
      } else if (inSlot.isEmpty()) {
        pRed = 1f;
        pBlue = 1f;
        pGreen = 0f;
      } else {
        pRed = 1f;
        pGreen = 0f;
        pBlue = 0f;
      }

      pShape.forAllEdges((pMinX, pMinY, pMinZ, pMaxX, pMaxY, pMaxZ) -> {
        float f = (float) (pMaxX - pMinX);
        float f1 = (float) (pMaxY - pMinY);
        float f2 = (float) (pMaxZ - pMinZ);
        float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
        f /= f3;
        f1 /= f3;
        f2 /= f3;
        pConsumer.vertex(pose.pose(), (float) (pMinX + pX), (float) (pMinY + pY), (float) (pMinZ + pZ)).color(pRed, pGreen, pBlue, pAlpha).normal(pose.normal(), f, f1, f2).endVertex();
        pConsumer.vertex(pose.pose(), (float) (pMaxX + pX), (float) (pMaxY + pY), (float) (pMaxZ + pZ)).color(pRed, pGreen, pBlue, pAlpha).normal(pose.normal(), f, f1, f2).endVertex();
      });
      pPoseStack.popPose();
    }

    if (!inSlot.isEmpty()) {
      int loc = (int) pBlockEntity.getBlockPos().asLong();
      pPoseStack.pushPose();
      pPoseStack.translate(0.5, pBlockEntity.offset() + Mth.cos((loc + RenderTickHandler.getClientTicks() + pPartialTick) / 10.0f + (float) Math.PI * 2f) * 0.05f, 0.5);
      pPoseStack.scale(0.5f, 0.5f, 0.5f);
      pPoseStack.mulPose(Vector3f.YP.rotationDegrees((loc + RenderTickHandler.getClientTicks() + pPartialTick) * 0.5f));
      Minecraft.getInstance().getItemRenderer().renderStatic(inSlot, ItemTransforms.TransformType.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, 0);
      pPoseStack.popPose();
    }
  }
}
