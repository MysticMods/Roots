package teamroots.roots.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import teamroots.roots.RegistryManager;
import teamroots.roots.Roots;

public class ItemPereskiaBulb extends ItemSeeds implements IModeledItem, IHerb {
	public ItemPereskiaBulb(String name, boolean addToTab){
		super(RegistryManager.pereskia, Blocks.DIRT);
		setUnlocalizedName(name);
		setRegistryName(Roots.MODID+":"+name);
		if (addToTab){
			setCreativeTab(Roots.tab);
		}
	}
	
	@Override
	public void initModel(){
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName().toString()));
	}
}
