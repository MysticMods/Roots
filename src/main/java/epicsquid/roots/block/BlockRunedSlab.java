package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockSlabBase;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockRunedSlab extends BlockSlabBase {
  public BlockRunedSlab(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull BlockState parent, boolean isDouble, @Nullable Block slab) {
    super(mat, type, hardness, name, parent, isDouble, slab);
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
