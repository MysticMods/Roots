package mysticmods.roots.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mysticmods.roots.blockentity.FungalTransmuterBlockEntity;
import mysticmods.roots.client.RenderTickHandler;
import mysticmods.roots.client.RenderUtil;
import mysticmods.roots.recipe.transmutation.TransmutationRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public class FungalTransmuterBlockEntityRenderer extends BoundedBlockEntityRenderer<FungalTransmuterBlockEntity> {

  public FungalTransmuterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  protected Component getTextToRender(FungalTransmuterBlockEntity blockEntity) {
/*    if (RootsClientAPI.isGramaryMode(GramaryItem.GramaryMode.BLOCK_ENTITY_INFO)) {
      if (blockEntity.getLifetime() != -1) {
        int lifetime = blockEntity.getLifetime();
        int seconds = lifetime / 20;
        int minutes = seconds / 60;
        return Component.literal(String.format("%02d:%02d", minutes, seconds % 60));
      }
    }*/
    return Component.empty();
  }

  @Override
  public void render(FungalTransmuterBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
    super.render(pBlockEntity, pPartialTick, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay);

    List<ItemStack> items = pBlockEntity.getNonEmptyItems();

    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    for (int i = 0; i < items.size(); i++) {
      ItemStack item = items.get(i);
      pPoseStack.pushPose();
      float shifted = (float) ((RenderTickHandler.getClientTicks() + pPartialTick) * 0.4 + i * (360.0 / items.size()));
      pPoseStack.translate(0.5, 1.1 + 0.05 * ((double) Mth.sin((float) Math.toRadians((double) shifted * 4))), 0.5);
      pPoseStack.mulPose(Axis.YP.rotationDegrees(shifted));
      pPoseStack.translate(-0.5, 0, 0);
      pPoseStack.mulPose(Axis.YP.rotationDegrees(shifted));
      pPoseStack.scale(0.4f, 0.4f, 0.4f);
      /*Minecraft.getInstance().getItemRenderer()
          .renderStatic(item, ItemDisplayContext.GROUND, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, null, 0);*/
      BakedModel baked = itemRenderer.getModel(item, Minecraft.getInstance().level, null, 0);
      RenderUtil.renderItemDissolve(Minecraft.getInstance().getItemRenderer(), item, ItemDisplayContext.FIXED, false, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, baked, Mth.lerp(pPartialTick, pBlockEntity.oDissolveProgress, pBlockEntity.dissolveProgress));
      pPoseStack.popPose();
    }

    ItemStack inSlot;
    RecipeHolder<TransmutationRecipe> recipe = pBlockEntity.getCachedRecipe();
    if (recipe == null && pBlockEntity.isCrafting()) {
      recipe = pBlockEntity.getLastRecipe();
    }
    if (recipe == null) {
      inSlot = ItemStack.EMPTY;
    } else {
      inSlot = recipe.value().getResultItem(Minecraft.getInstance().getConnection().registryAccess());
    }

    if (!inSlot.isEmpty()) {
      int loc = pBlockEntity.getBlockPos().hashCode();
      pPoseStack.pushPose();
      pPoseStack.translate(0.5, 1.3 + Mth.cos((loc + RenderTickHandler.getClientTicks() + pPartialTick) / 10.0f + (float) Math.PI * 2f) * 0.05f, 0.5);
      pPoseStack.scale(1.2f, 1.2f, 1.2f);
      pPoseStack.mulPose(Axis.YP.rotationDegrees((loc + RenderTickHandler.getClientTicks() + pPartialTick) * 0.5f));
      Minecraft.getInstance().getItemRenderer()
          .renderStatic(inSlot, ItemDisplayContext.GROUND, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, null, 0);
      pPoseStack.popPose();
    }
  }
}
