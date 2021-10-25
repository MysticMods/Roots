package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockWallBase;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;

public class BlockRunedWall extends BlockWallBase {
  public BlockRunedWall(@Nonnull Block base, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(base, type, hardness, name);
    setResistance(5000f);
  }

  @Override
  public boolean canEntityDestroy(BlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
    if (BlockRunedObsidian.checkEntityBlocked(state, world, pos, entity)) {
      return false;
    }

    return super.canEntityDestroy(state, world, pos, entity);
  }
}
