package mysticmods.roots.client.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mysticmods.roots.blockentity.PedestalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import noobanidus.libs.particleslib.client.events.RenderTickHandler;

import java.util.Random;

public class PedestalBlockEntityRenderer implements BlockEntityRenderer<PedestalBlockEntity> {
  private static final Random random = new Random();

  public PedestalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
  }

  // TODO: Render more when there's more
  @Override
  public void render(PedestalBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
    ItemStack inSlot = pBlockEntity.getHeldItem();
/*    if (Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes()) {
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
*//*      } else if (inSlot.isEmpty()) {
        pRed = 1f;
        pBlue = 1f;
        pGreen = 0f;*//*
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
    }*/

    // original single item rendering
/*    if (!inSlot.isEmpty()) {
      int loc = pBlockEntity.getBlockPos().hashCode();
      pPoseStack.pushPose();
      pPoseStack.translate(0.5, pBlockEntity.offset() + Mth.cos((loc + RenderTickHandler.getClientTicks() + pPartialTick) / 10.0f + (float) Math.PI * 2f) * 0.05f, 0.5);
      pPoseStack.scale(0.5f, 0.5f, 0.5f);
      pPoseStack.mulPose(Vector3f.YP.rotationDegrees((loc + RenderTickHandler.getClientTicks() + pPartialTick) * 0.5f));
      Minecraft.getInstance().getItemRenderer().renderStatic(inSlot, ItemTransforms.TransformType.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, 0);
      pPoseStack.popPose();
    }*/

    if (!inSlot.isEmpty()) {
      pPoseStack.pushPose();
      int loc = pBlockEntity.getBlockPos().hashCode();
      random.setSeed(loc);
      BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(inSlot, pBlockEntity.getLevel(), null, inSlot.hashCode());
      boolean flag = bakedmodel.isGui3d();
      int j = this.getRenderAmount(inSlot);
      pPoseStack.translate(0.5, pBlockEntity.offset() + Mth.cos((loc + RenderTickHandler.getClientTicks() + pPartialTick) / 10.0f + (float) Math.PI * 2f) * 0.05f, 0.5);
      pPoseStack.mulPose(Vector3f.YP.rotationDegrees((loc + RenderTickHandler.getClientTicks() + pPartialTick) * 0.5f));
      if (!flag) {
        float f7 = -0.0F * (float) (j - 1) * 0.5F;
        float f8 = -0.0F * (float) (j - 1) * 0.5F;
        float f9 = -0.09375F * (float) (j - 1) * 0.5F;
        pPoseStack.translate(f7, f8, f9);
      }

      for (int k = 0; k < j; ++k) {
        pPoseStack.pushPose();
        if (k > 0) {
          if (flag) {
            float f11 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
            float f13 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
            float f10 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
            pPoseStack.translate(f11, f13, f10);
          } else {
            float f12 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
            float f14 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
            pPoseStack.translate(f12, f14, 0.0D);
          }
        }

        Minecraft.getInstance().getItemRenderer().render(inSlot, ItemTransforms.TransformType.GROUND, false, pPoseStack, pBufferSource, pPackedLight, OverlayTexture.NO_OVERLAY, bakedmodel);
        pPoseStack.popPose();
        if (!flag) {
          pPoseStack.translate(0.0, 0.0, 0.09375F);
        }
      }

      pPoseStack.popPose();
    }
  }

  protected int getRenderAmount(ItemStack pStack) {
    int i = 1;
    if (pStack.getCount() > 48) {
      i = 5;
    } else if (pStack.getCount() > 32) {
      i = 4;
    } else if (pStack.getCount() > 16) {
      i = 3;
    } else if (pStack.getCount() > 1) {
      i = 2;
    }

    return i;
  }
}
