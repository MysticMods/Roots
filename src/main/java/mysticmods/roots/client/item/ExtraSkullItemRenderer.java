package mysticmods.roots.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.client.blockentity.ExtraSkullBlockRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;

public class ExtraSkullItemRenderer extends BlockEntityWithoutLevelRenderer {
  private static ExtraSkullItemRenderer INSTANCE;

  public ExtraSkullItemRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
    super(blockEntityRenderDispatcher, entityModelSet);
  }

  public static ExtraSkullItemRenderer getInstance() {
    if (INSTANCE == null) {
      var mc = Minecraft.getInstance();
      INSTANCE = new ExtraSkullItemRenderer(mc.getBlockEntityRenderDispatcher(), mc.getEntityModels());
    }

    return INSTANCE;
  }

  @Override
  public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
    if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractSkullBlock skullBlock) {
      SkullBlock.Type type = skullBlock.getType();
      ExtraSkullBlockRenderer.renderSkull(null, 180.0f, 0.0f, poseStack, buffer, packedLight, type, ExtraSkullBlockRenderer.getModel(type), ExtraSkullBlockRenderer.getExtraModel(type), SkullBlockRenderer.getRenderType(type, null));
    }
  }
}
