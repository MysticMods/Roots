package mysticmods.roots.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.blockentity.EnchantedTurfBlockEntity;
import mysticmods.roots.client.RootsRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.client.model.data.ModelData;

public class EnchantedTurfBlockEntityRenderer implements BlockEntityRenderer<EnchantedTurfBlockEntity> {

  private static final RandomSource RANDOM = RandomSource.create();

  private final BlockRenderDispatcher blockRenderDispatcher;

  public EnchantedTurfBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    this.blockRenderDispatcher = context.getBlockRenderDispatcher();
  }

  @Override
  public void render(EnchantedTurfBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                     MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    if (blockEntity.getLevel() == null) return;

    poseStack.pushPose();
    PoseStack.Pose pose = poseStack.last();
    VertexConsumer consumer = new SheetedDecalTextureGenerator(bufferSource.getBuffer(RootsRenderTypes.GLINT), pose, 0.0078125F);
    blockRenderDispatcher.renderBatched(
        blockEntity.getBlockState(),
        blockEntity.getBlockPos(),
        blockEntity.getLevel(),
        poseStack,
        consumer,
        true,
        RANDOM,
        ModelData.EMPTY,
        null); // <- Dear Bysco, is this safe?
    poseStack.popPose();
  }
}
