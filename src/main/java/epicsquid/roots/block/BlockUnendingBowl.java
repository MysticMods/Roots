package epicsquid.roots.block;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.block.BlockTEBase;
import epicsquid.roots.block.itemblock.ItemBlockUnendingBowl;
import epicsquid.roots.integration.botania.PetalApothecaryFiller;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;


@SuppressWarnings("deprecation")
public class BlockUnendingBowl extends BlockTEBase {
	
	public BlockUnendingBowl(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
		super(mat, type, hardness, name, teClass);
		this.setItemBlock(new ItemBlockUnendingBowl(this)).setRegistryName(LibRegistry.getActiveModid(), name);
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
		return new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.3125, 0.875);
	}
	
	@Nonnull
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	@Override
	@Nonnull
	@SuppressWarnings("deprecation")
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.BOWL;
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		if (PetalApothecaryFiller.hasBotania()) {
			PetalApothecaryFiller.getAdjacentApothecary(worldIn, pos);
		}
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
		if (PetalApothecaryFiller.hasBotania()) {
			PetalApothecaryFiller.getAdjacentApothecary(worldIn, pos);
		}
	}
	
	@Override
	public int tickRate(World worldIn) {
		return 40;
	}
}
