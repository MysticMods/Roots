package teamroots.roots.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.common.registry.GameRegistry;
import teamroots.roots.RegistryManager;
import teamroots.roots.Roots;

public class ItemSpiritHerb extends ItemSeeds implements IModeledItem {
	public ItemSpiritHerb(String name, boolean addToTab){
		super(RegistryManager.spirit_herb, Blocks.SOUL_SAND);
		setUnlocalizedName(name);
		setRegistryName(Roots.MODID+":"+name);
		if (addToTab){
			setCreativeTab(Roots.tab);
		}
	}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos){
		return EnumPlantType.Nether;
	}
	
	@Override
	public void initModel(){
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName().toString()));
	}
}
