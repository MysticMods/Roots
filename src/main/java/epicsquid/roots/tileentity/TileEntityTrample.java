package epicsquid.roots.tileentity;

import epicsquid.mysticallib.tile.TileBase;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityTrample extends TileBase implements ITickable {
  public TileEntityTrample() {
    super();
  }

  @Override
  public void update() {
    if (world.rand.nextInt(5) != 0) return;
    for (BlockPos.MutableBlockPos fpos : BlockPos.getAllInBoxMutable(pos.add(-4, -2, -4), pos.add(4, 3, 4))) {
      IBlockState fstate = world.getBlockState(fpos);
      if (fstate.getBlock() == Blocks.FARMLAND) {
        int value = fstate.getValue(BlockFarmland.MOISTURE);
        if (value < 7) {
          world.setBlockState(fpos, fstate.withProperty(BlockFarmland.MOISTURE, value + 1));
          if (world.rand.nextInt(4) != 0) {
            break;
          }
        }
      }
    }
  }
}
