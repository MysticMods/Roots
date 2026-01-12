package mysticmods.roots.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import mysticmods.roots.blockentity.WildwoodChestBlockEntity;
import mysticmods.roots.init.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class WildwoodChestItemRenderer extends BlockEntityWithoutLevelRenderer {
  private static WildwoodChestItemRenderer INSTANCE;

  public static WildwoodChestItemRenderer getInstance() {
    if (INSTANCE == null) {
      var mc = Minecraft.getInstance();
      INSTANCE = new WildwoodChestItemRenderer(mc.getBlockEntityRenderDispatcher(), mc.getEntityModels());
    }

    return INSTANCE;
  }

  private static WildwoodChestBlockEntity DEFAULT_WILDWOOD_CHEST;

  private final BlockEntityRenderDispatcher dispatcher;

  public WildwoodChestItemRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
    super(blockEntityRenderDispatcher, entityModelSet);
    this.dispatcher = blockEntityRenderDispatcher;
    if (DEFAULT_WILDWOOD_CHEST == null) {
      DEFAULT_WILDWOOD_CHEST = new WildwoodChestBlockEntity(BlockPos.ZERO, ModBlocks.WILDWOOD_CHEST.get()
          .defaultBlockState());
    }
  }

  public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
    this.dispatcher.renderItem(DEFAULT_WILDWOOD_CHEST, poseStack, buffer, packedLight, packedOverlay);
  }
}
