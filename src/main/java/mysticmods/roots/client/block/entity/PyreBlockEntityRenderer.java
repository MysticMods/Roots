package mysticmods.roots.client.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mysticmods.roots.blockentity.PyreBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
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
