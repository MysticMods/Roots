package mysticmods.roots.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mysticmods.roots.blockentity.MortarBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class MortarBlockEntityRenderer implements BlockEntityRenderer<MortarBlockEntity> {
  private final Random random = new Random();

  public MortarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
  }

  @Override
  public void render(MortarBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
    int slot = 0;
    for (ItemStack item : pBlockEntity.getNonEmptyItems()) {
      slot++;
      pPoseStack.pushPose();
      int uses = Math.max(0, pBlockEntity.getUses());
      random.setSeed(((long) item.getItem().hashCode() * slot) ^ uses);
      pPoseStack.translate(0.475 + random.nextFloat() / 20.0, 0.15 + random.nextFloat() / 20.0, 0.475 + random.nextFloat() / 20);
      pPoseStack.scale(0.8f, 0.8f, 0.8f);
      pPoseStack.mulPose(Axis.YP.rotationDegrees(random.nextInt(360)));
      Minecraft.getInstance().getItemRenderer()
          .renderStatic(item, ItemDisplayContext.GROUND, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, null, 0);
      pPoseStack.popPose();
    }
  }
}
