package teamroots.roots.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemShears;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import teamroots.roots.RegistryManager;
import teamroots.roots.Roots;

public class ItemShearsBase extends ItemShears implements IModeledItem {
	public ItemShearsBase(String name, boolean addToTab, int durability){
		super();
		setUnlocalizedName(name);
		setRegistryName(Roots.MODID+":"+name);
		if (addToTab){
			setCreativeTab(Roots.tab);
		}
		this.setMaxDamage(durability);
	}

	@Override
	public void initModel(){
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName().toString()));
	}

}
