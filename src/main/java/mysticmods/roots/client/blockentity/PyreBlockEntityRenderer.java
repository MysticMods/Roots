package mysticmods.roots.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.client.RootsClientAPI;
import mysticmods.roots.api.recipe.ComplexEntityType;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.client.RenderTickHandler;
import mysticmods.roots.client.RenderUtil;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.item.GramaryItem;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SpawnerRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public class PyreBlockEntityRenderer extends BoundedBlockEntityRenderer<PyreBlockEntity> {
  private final EntityRenderDispatcher entityRenderer;

  public PyreBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    super(context);
    this.entityRenderer = context.getEntityRenderer();
  }

  @Override
  protected Component getTextToRender(PyreBlockEntity blockEntity) {
    if (RootsClientAPI.isGramaryMode(GramaryItem.GramaryMode.BLOCK_ENTITY_INFO)) {
      if (blockEntity.getLifetime() != -1) {
        int lifetime = blockEntity.getLifetime();
        int seconds = lifetime / 20;
        int minutes = seconds / 60;
        return Component.literal(String.format("%02d:%02d", minutes, seconds % 60));
      }
    }
    return Component.empty();
  }

  @Override
  public void render(PyreBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
    super.render(pBlockEntity, pPartialTick, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay);

    BlockPos boundPos = pBlockEntity.getBoundPosition();
    if (boundPos != null && !boundPos.equals(BlockPos.ZERO)) {
      if (Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes()) {
        BlockPos origin = pBlockEntity.getBlockPos();
        BlockPos offset = boundPos.subtract(origin);
        RenderUtil.renderAABB(pPoseStack, pBufferSource, origin, offset, null, null);
      }
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

      if (pBlockEntity.getCurrentRitual().is(RootsTags.Rituals.SUMMON_CREATURES)) {
        ComplexEntityType entity = pBlockEntity.getData(ModAttachments.CACHED_PYRE_ENTITY);
        if (!entity.isEmpty()) {
          Entity cached = entity.cachedEntity(pBlockEntity.getLevel());
          if (cached != null) {
            pPoseStack.pushPose();
            pPoseStack.translate(0, 1.9, 0);
            SpawnerRenderer.renderEntityInSpawner(pPartialTick, pPoseStack, pBufferSource, pPackedLight, cached, entityRenderer, 0, 0);
            pPoseStack.popPose();
          }
        }
      }
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
