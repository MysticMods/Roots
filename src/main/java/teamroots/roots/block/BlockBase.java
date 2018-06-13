package teamroots.roots.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import teamroots.roots.Roots;

public class BlockBase extends Block implements IModeledBlock, IBlock {
	public Item itemBlock = null;
	public boolean isOpaqueCube = true, isFullCube = true;
	public BlockRenderLayer layer = BlockRenderLayer.SOLID;
	public AxisAlignedBB bounds = new AxisAlignedBB(0,0,0,1,1,1);
	public BlockBase(Material material, SoundType type, String name, boolean addToTab){
		super(material);
		this.setSoundType(type);
		setUnlocalizedName(name);
		setRegistryName(Roots.MODID+":"+name);
		if (addToTab){
			setCreativeTab(Roots.tab);
		}
		itemBlock = (new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }
	
	public BlockBase setIsOpaqueCube(boolean b){
		isOpaqueCube = b;
		return this;
	}
	
	public BlockBase setBoundingBox(AxisAlignedBB box){
		this.bounds = box;
		return this;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return this.bounds;
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer){
		return layer == this.layer;
	}
	
	public BlockBase setRenderLayer(BlockRenderLayer layer){
		this.layer = layer;
		return this;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
		return isOpaqueCube;
	}
	
	public BlockBase setIsFullCube(boolean b){
		isFullCube = b;
		return this;
	}
	
	@Override
	public boolean isFullCube(IBlockState state){
		return isFullCube;
	}
	
	public BlockBase setHarvestProperties(String toolType, int level){
		super.setHarvestLevel(toolType, level);
		return this;
	}
	
	@Override
	public void initModel(){
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName().toString(),"inventory"));
	}

	@Override
	public Item getItemBlock() {
		return itemBlock;
	}
}
