package epicsquid.roots.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
public class BlockReinforcedPyre extends BlockPyre {
  public BlockReinforcedPyre(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
    super(mat, type, hardness, name, teClass);
    setResistance(5000f);
  }

  @Override
  public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
    if (entity instanceof net.minecraft.entity.boss.EntityDragon || (entity instanceof net.minecraft.entity.boss.EntityWither) || (entity instanceof net.minecraft.entity.projectile.EntityWitherSkull)) {
      return false;
    }

    return super.canEntityDestroy(state, world, pos, entity);
  }
}
