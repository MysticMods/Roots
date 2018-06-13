package teamroots.roots.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.roots.tileentity.TileEntityImbuer;
import teamroots.roots.tileentity.TileEntityMortar;

public class BlockStructureMarker extends BlockBase {
	public static final PropertyInteger marker_value = PropertyInteger.create("marker_value", 0, 15);
	public BlockStructureMarker() {
		super(Material.STRUCTURE_VOID, SoundType.ANVIL, "structure_marker", false);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list){
		if (tab == this.getCreativeTabToDisplayOn()){
			list.clear();
		}
	}
	
	@Override
	public BlockStateContainer createBlockState(){
		return new BlockStateContainer(this,marker_value);
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(marker_value);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(marker_value,meta);
	}
	
}
