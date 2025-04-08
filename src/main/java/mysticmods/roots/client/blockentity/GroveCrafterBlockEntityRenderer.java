package mysticmods.roots.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mysticmods.roots.block.GroveCrafterBlock;
import mysticmods.roots.blockentity.GroveCrafterBlockEntity;
import mysticmods.roots.client.ClientSetup;
import mysticmods.roots.client.RenderTickHandler;
import mysticmods.roots.client.TintWrappedVertexConsumer;
import mysticmods.roots.recipe.grove.GroveRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;


public class GroveCrafterBlockEntityRenderer extends BoundedBlockEntityRenderer<GroveCrafterBlockEntity> {
  public GroveCrafterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  public void render(GroveCrafterBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
    super.render(pBlockEntity, pPartialTick, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay);

    BlockState state = pBlockEntity.getBlockState();
    int loc = pBlockEntity.getBlockPos().hashCode();
    if (!state.getValue(GroveCrafterBlock.ACTIVE)) {
      pPoseStack.pushPose();
      pPoseStack.translate(0.5, 1.15 /*+ Mth.cos((loc + RenderTickHandler.getClientTicks() + pPartialTick) / 10.0f + (float) Math.PI * 2f) * 0.05f*/, 0.5);
      pPoseStack.mulPose(Axis.YP.rotationDegrees((loc + RenderTickHandler.getClientTicks() + pPartialTick) * 0.5f));
      pPoseStack.scale(0.6f, 0.6f, 0.6f);
      pPoseStack.translate(-0.5f, -0.5f, -0.5f);
      Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
          pPoseStack.last(),
          new TintWrappedVertexConsumer(pBufferSource.getBuffer(Sheets.translucentItemSheet()), 1f, 1f, 1f, 0.5f),
          null,
          ClientSetup.NO_GROVE_STONE_MODEL,
          1,
          1,
          1,
          LightTexture.FULL_SKY,
          OverlayTexture.NO_OVERLAY,
          ModelData.EMPTY,
          Sheets.translucentItemSheet());
      pPoseStack.popPose();
    } else {
      RecipeHolder<GroveRecipe> recipe = pBlockEntity.getRecipe();
      if (recipe != null) {
        ItemStack inSlot = recipe.value().getResultItem(Minecraft.getInstance().getConnection().registryAccess());
        pPoseStack.pushPose();
        pPoseStack.translate(0.5, 1.15 + Mth.cos((loc + RenderTickHandler.getClientTicks() + pPartialTick) / 10.0f + (float) Math.PI * 2f) * 0.05f, 0.5);
        pPoseStack.mulPose(Axis.YP.rotationDegrees((loc + RenderTickHandler.getClientTicks() + pPartialTick) * 0.5f));
        pPoseStack.scale(0.8f, 0.8f, 0.8f);
        Minecraft.getInstance().getItemRenderer()
            .renderStatic(inSlot, ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, null, 0);
        pPoseStack.popPose();
      }
    }
  }
}
