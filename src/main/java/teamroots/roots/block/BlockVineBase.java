package teamroots.roots.block;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.roots.util.Misc;

public class BlockVineBase extends BlockPlantBase{
	public static final PropertyDirection facing = PropertyDirection.create("facing");
	public BlockVineBase(String name, boolean addToTab) {
		super(name, addToTab);
	}
	
	@Override
	public BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, facing);
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(facing).getIndex();
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return true;
    }

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
		return worldIn.getBlockState(pos.offset(Misc.getOppositeFace(side))).isFullCube() && worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
    }
	
	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state){
        if (state.getBlock() == this) {
            IBlockState soil = worldIn.getBlockState(pos.offset(Misc.getOppositeFace(state.getValue(facing))));
            return soil.isFullCube();
        }
        return false;
    }
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(facing,EnumFacing.getFront(meta));
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing face, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		return getDefaultState().withProperty(facing, face);
	}
	
}
