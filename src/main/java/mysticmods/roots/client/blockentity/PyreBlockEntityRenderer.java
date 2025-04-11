package mysticmods.roots.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.client.RenderTickHandler;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class PyreBlockEntityRenderer extends BoundedBlockEntityRenderer<PyreBlockEntity> {
  public PyreBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  public void render(PyreBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
    super.render(pBlockEntity, pPartialTick, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay);

    if (Minecraft.getInstance().getEntityRenderDispatcher()
        .shouldRenderHitBoxes() && pBlockEntity.getCurrentRitual() != null) {
      AABB bounds = pBlockEntity.getCurrentRitual().getAABB();
      pPoseStack.pushPose();
      VoxelShape pShape = Shapes.create(bounds);
      VertexConsumer pConsumer = pBufferSource.getBuffer(RenderType.lines());
      PoseStack.Pose pose = pPoseStack.last();
      ColorHelper.Color color = ColorHelper.color(pBlockEntity.getBlockPos());

      pShape.forAllEdges((pMinX, pMinY, pMinZ, pMaxX, pMaxY, pMaxZ) -> {
        float f = (float) (pMaxX - pMinX);
        float f1 = (float) (pMaxY - pMinY);
        float f2 = (float) (pMaxZ - pMinZ);
        float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
        f /= f3;
        f1 /= f3;
        f2 /= f3;
        pConsumer.addVertex(pose.pose(), (float) (pMinX), (float) (pMinY), (float) (pMinZ))
            .setColor(color.r(), color.g(), color.b(), color.a()).setNormal(pose, f, f1, f2);
        pConsumer.addVertex(pose.pose(), (float) (pMaxX), (float) (pMaxY), (float) (pMaxZ))
            .setColor(color.g(), color.g(), color.b(), color.a()).setNormal(pose, f, f1, f2);
      });
      pPoseStack.popPose();
    }

    List<ItemStack> items = pBlockEntity.getNonEmptyItems();

    for (int i = 0; i < items.size(); i++) {
      ItemStack item = items.get(i);
      pPoseStack.pushPose();
      float shifted = (float) ((RenderTickHandler.getClientTicks() + pPartialTick) * 0.4 + i * (360.0 / items.size()));
      pPoseStack.translate(0.5, 0.5 + 0.05 * ((double) Mth.sin((float) Math.toRadians((double) shifted * 4))), 0.5);
      pPoseStack.mulPose(Axis.YP.rotationDegrees(shifted));
      pPoseStack.translate(-0.5, 0, 0);
      pPoseStack.mulPose(Axis.YP.rotationDegrees(shifted));
      pPoseStack.scale(0.4f, 0.4f, 0.4f);
      Minecraft.getInstance().getItemRenderer()
          .renderStatic(item, ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, null, 0);
      pPoseStack.popPose();
    }

    ItemStack inSlot;
    RecipeHolder<PyreRecipe> recipe = pBlockEntity.getCachedRecipe();
    if (pBlockEntity.getCurrentRitual() == ModRituals.CRAFTING.get()) {
      if (recipe == null) {
        recipe = pBlockEntity.getLastRecipe();
      }
      if (recipe == null) {
        inSlot = ItemStack.EMPTY;
      } else {
        inSlot = recipe.value().getResultItem(Minecraft.getInstance().getConnection().registryAccess());
      }
    } else if (pBlockEntity.getCurrentRitual() == null) {
      if (recipe == null) {
        inSlot = ItemStack.EMPTY;
      } else if (recipe.value().getRitual() == null) {
        inSlot = recipe.value().getResultItem(Minecraft.getInstance().getConnection().registryAccess());
      } else {
        inSlot = recipe.value().getRitual().getIcon();
      }
    } else {
      inSlot = pBlockEntity.getCurrentRitual().getIcon();
    }


    if (!inSlot.isEmpty()) {
      int loc = pBlockEntity.getBlockPos().hashCode();
      pPoseStack.pushPose();
      pPoseStack.translate(0.5, 1 + Mth.cos((loc + RenderTickHandler.getClientTicks() + pPartialTick) / 10.0f + (float) Math.PI * 2f) * 0.05f, 0.5);
      pPoseStack.scale(1.2f, 1.2f, 1.2f);
      pPoseStack.mulPose(Axis.YP.rotationDegrees((loc + RenderTickHandler.getClientTicks() + pPartialTick) * 0.5f));
      Minecraft.getInstance().getItemRenderer()
          .renderStatic(inSlot, ItemDisplayContext.GROUND, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, null, 0);
      pPoseStack.popPose();
    }
  }
}
