package mysticmods.roots.block;

import mysticmods.roots.block.entity.template.BaseBlockEntity;
import mysticmods.roots.block.entity.template.UseDelegatedBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class UseDelegatedBlock extends Block {
  public UseDelegatedBlock(Properties properties) {
    super(properties);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Nullable
  @Override
  public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

  @Override
  public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray) {
    TileEntity be = level.getBlockEntity(pos);
    if (be instanceof UseDelegatedBlockEntity) {
      return ((UseDelegatedBlockEntity)be).use(state, level, pos, player, hand, ray);
    }

    return super.use(state, level, pos, player, hand, ray);
  }
}
