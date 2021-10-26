package epicsquid.roots.block;

import epicsquid.mysticallib.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;

public class BlockRunedObsidian extends Block {
  public BlockRunedObsidian(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(mat, type, hardness, name);
    setResistance(5000f);
  }

  public static boolean checkEntityBlocked(BlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
    if (entity instanceof EnderDragonEntity || (entity instanceof WitherEntity) || (entity instanceof WitherSkullEntity)) {
      return true;
    }

    return false;
  }

  @Override
  public boolean canEntityDestroy(BlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
    if (BlockRunedObsidian.checkEntityBlocked(state, world, pos, entity)) {
      return false;
    }

    return super.canEntityDestroy(state, world, pos, entity);
  }
}
