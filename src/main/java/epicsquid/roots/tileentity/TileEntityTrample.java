package epicsquid.roots.tileentity;

import epicsquid.mysticallib.tile.TileBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.FarmlandWaterManager;
import net.minecraftforge.common.ticket.AABBTicket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityTrample extends TileBase {
  public static AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(-4, -1, -4, 5, 1, 5);

  private AABBTicket ticket = null;

  public TileEntityTrample() {
  }

  @Override
  public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable PlayerEntity player) {
    super.breakBlock(world, pos, state, player);

    onChunkUnload();
  }

  @Override
  public void onLoad() {
    super.onLoad();

    if (world != null && !world.isRemote) {
      if (ticket != null) {
        ticket.validate();
      } else {
        ticket = FarmlandWaterManager.addAABBTicket(world, BOUNDING_BOX.offset(getPos()));
      }
    }
  }

  @Override
  public void onChunkUnload() {
    super.onChunkUnload();

    if (world != null && !world.isRemote) {
      if (ticket != null) {
        ticket.invalidate();
      }
    }
  }
}
