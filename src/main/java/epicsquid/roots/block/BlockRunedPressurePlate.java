package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockPressurePlateBase;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;

public class BlockRunedPressurePlate extends BlockPressurePlateBase {
  public BlockRunedPressurePlate(@Nonnull Block base, @Nonnull PressurePlateType plateType, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(base, plateType, type, hardness, name);
    setResistance(5000f);
  }

  @Override
  public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
    if (BlockRunedObsidian.checkEntityBlocked(state, world, pos, entity)) {
      return false;
    }

    return super.canEntityDestroy(state, world, pos, entity);
  }
}
