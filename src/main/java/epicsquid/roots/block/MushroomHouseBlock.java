package epicsquid.roots.block;

import net.minecraft.block.Block;
import net.minecraft.util.BlockRenderLayer;

import javax.annotation.Nonnull;

public class MushroomHouseBlock extends Block {
  public MushroomHouseBlock(Properties properties) {
    super(properties);
  }

  // TODO: Light opacity?


/*  @Override
  public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull Direction face, float hitX, float hitY, float hitZ) {
    if (player.getHeldItem(hand).getItem() instanceof DruidKnifeItem) {
      if (super.onBlockActivated(world, pos, state, player, hand, face, hitX, hitY, hitZ)) return true;
    }

    if (!world.isRemote) {
      //player.openGui(Roots.instance, GuiHandler.CRAFTER_ID, world, pos.getX(), pos.getY(), pos.getZ());
    }

    return true;
  }*/

  @Nonnull
  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }

  // TODO: Voxel shape
}
