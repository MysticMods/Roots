package mysticmods.roots.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mysticmods.roots.blockentity.MortarBlockEntity;
import mysticmods.roots.client.RenderTickHandler;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class MortarBlockEntityRenderer implements BlockEntityRenderer<MortarBlockEntity> {
  public MortarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
  }

  @Override
  public void render(MortarBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
    int slot = 0;
    RandomSource random = pBlockEntity.getLevel().getRandom();
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
  }
}
