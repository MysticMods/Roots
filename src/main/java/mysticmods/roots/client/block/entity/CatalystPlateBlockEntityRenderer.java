package mysticmods.roots.client.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mysticmods.roots.block.entity.PedestalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import noobanidus.libs.particleslib.client.events.RenderTickHandler;

public class CatalystPlateBlockEntityRenderer implements BlockEntityRenderer<PedestalBlockEntity> {
  public CatalystPlateBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
  }

  @Override
  public void render(PedestalBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
    ItemStack inSlot = pBlockEntity.getHeldItem();
    if (!inSlot.isEmpty()) {
      int loc = (int) pBlockEntity.getBlockPos().asLong();
      pPoseStack.pushPose();
      pPoseStack.translate(0.5, 1.4 + Mth.cos((loc + RenderTickHandler.getClientTicks() + pPartialTick) / 10.0f + (float)Math.PI * 2f) * 0.05f, 0.5);
      pPoseStack.scale(0.5f, 0.5f, 0.5f);
      pPoseStack.mulPose(Vector3f.YP.rotationDegrees((loc + RenderTickHandler.getClientTicks() + pPartialTick) * 0.5f));
      Minecraft.getInstance().getItemRenderer().renderStatic(inSlot, ItemTransforms.TransformType.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, 0);
      pPoseStack.popPose();

    }
  }
}
