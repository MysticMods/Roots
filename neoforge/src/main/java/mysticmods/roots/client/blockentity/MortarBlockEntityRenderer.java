package mysticmods.roots.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mysticmods.roots.blockentity.MortarBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class MortarBlockEntityRenderer implements BlockEntityRenderer<MortarBlockEntity> {
  private final Random random = new Random();

  public MortarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
  }

  @Override
  public void render(MortarBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
    for (ItemStack item : pBlockEntity.getNonEmptyItems()) {
      pPoseStack.pushPose();
      random.setSeed(item.hashCode() ^ pBlockEntity.getUses());
      pPoseStack.translate(0.475 + random.nextFloat() / 20.0, 0.25 + random.nextFloat() / 20.0, 0.475 + random.nextFloat() / 20);
      pPoseStack.scale(0.4f, 0.4f, 0.4f);
      pPoseStack.mulPose(Vector3f.YP.rotationDegrees(random.nextInt(360)));
      Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemTransforms.TransformType.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, 0);
      pPoseStack.popPose();
    }
  }
}
