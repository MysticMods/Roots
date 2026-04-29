package mysticmods.roots.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mysticmods.roots.blockentity.MortarBlockEntity;
import mysticmods.roots.client.RenderTickHandler;
import mysticmods.roots.client.gui.layer.HudOverlay;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MortarBlockEntityRenderer extends BoundedBlockEntityRenderer<MortarBlockEntity> {
  public static final RandomSource RANDOM = RandomSource.create();

  public MortarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  public void render(MortarBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
    if (ConfigManager.HIGHLIGHT_LAST_BLOCK.getAsBoolean() && pBlockEntity.getBlockPos()
        .equals(HudOverlay.getStoredBlockPos())) {
      pPoseStack.pushPose();
      VoxelShape pShape = pBlockEntity.getBlockState()
          .getCollisionShape(pBlockEntity.getLevel(), pBlockEntity.getBlockPos());
      VertexConsumer pConsumer = pBufferSource.getBuffer(RenderType.lines());
      PoseStack.Pose pose = pPoseStack.last();

      double pX = 0;
      double pY = 0;
      double pZ = 0;
      float pRed, pGreen, pBlue;
      float pAlpha = 1f;

      pRed = 0.4f;
      pGreen = 0.1f;
      pBlue = 0.4f;

      pShape.forAllEdges((pMinX, pMinY, pMinZ, pMaxX, pMaxY, pMaxZ) -> {
        float f = (float) (pMaxX - pMinX);
        float f1 = (float) (pMaxY - pMinY);
        float f2 = (float) (pMaxZ - pMinZ);
        float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
        f /= f3;
        f1 /= f3;
        f2 /= f3;
        pConsumer.addVertex(pose.pose(), (float) (pMinX + pX), (float) (pMinY + pY), (float) (pMinZ + pZ))
            .setColor(pRed, pGreen, pBlue, pAlpha).setNormal(pose, f, f1, f2);
        pConsumer.addVertex(pose.pose(), (float) (pMaxX + pX), (float) (pMaxY + pY), (float) (pMaxZ + pZ))
            .setColor(pRed, pGreen, pBlue, pAlpha).setNormal(pose, f, f1, f2);
      });
      pPoseStack.popPose();
    }
    int slot = 0;
    RandomSource random = RANDOM;
    for (ItemStack item : pBlockEntity.getNonEmptyItems()) {
      slot++;
      pPoseStack.pushPose();
      int uses = Math.max(0, pBlockEntity.getUses());
      random.setSeed(((item.getItem().hashCode() & 0xFFFFFFFFL) * 31 + slot) * 31 + uses);
      pPoseStack.translate(0.475 + random.nextFloat() / 20.0, 0.15 + random.nextFloat() / 20.0, 0.475 + random.nextFloat() / 20);
      pPoseStack.scale(0.8f, 0.8f, 0.8f);
      pPoseStack.mulPose(Axis.YP.rotationDegrees(random.nextInt(360)));
      Minecraft.getInstance().getItemRenderer()
          .renderStatic(item, ItemDisplayContext.GROUND, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, null, 0);
      pPoseStack.popPose();
    }

    int loc = pBlockEntity.getBlockPos().hashCode();
    RecipeHolder<MortarRecipe> recipe = pBlockEntity.getCachedRecipe();
    if (recipe != null) {
      ItemStack inSlot = recipe.value().getResultItem(Minecraft.getInstance().getConnection().registryAccess());
      if (inSlot.isEmpty() && !recipe.value().getUnlocks().isEmpty()) {
        inSlot = recipe.value().getUnlocks().getFirst().getIcon();
      }
      pPoseStack.pushPose();
      pPoseStack.translate(0.5, 1.15 + Mth.cos((loc + RenderTickHandler.getClientTicks() + pPartialTick) / 10.0f + (float) Math.PI * 2f) * 0.05f, 0.5);
      pPoseStack.mulPose(Axis.YP.rotationDegrees((loc + RenderTickHandler.getClientTicks() + pPartialTick) * 0.5f));
      pPoseStack.scale(0.4f, 0.4f, 0.4f);
      Minecraft.getInstance().getItemRenderer()
          .renderStatic(inSlot, ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, null, 0);
      pPoseStack.popPose();
    }
    super.render(pBlockEntity, pPartialTick, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay);
  }
}
