package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockTEBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;

public class BlockImbuer extends BlockTEBase {
	
	public BlockImbuer(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
		super(mat, type, hardness, name, teClass);
	}
	
	@Override
	public boolean isFullCube(@Nonnull IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(@Nonnull IBlockState state) {
		return false;
	}
	
	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
		return new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 0.125, 0.6875);
	}
	
	@Override
	@Nonnull
	@SuppressWarnings("deprecation")
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.BOWL;
	}
}
