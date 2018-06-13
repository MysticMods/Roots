package teamroots.roots.item;

import java.util.List;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.roots.RegistryManager;
import teamroots.roots.Roots;
import teamroots.roots.spell.SpellRegistry;

public class ItemPetalDust extends ItemBase {
	public ItemPetalDust(String name, boolean addToTab){
		super(name, addToTab);
		this.hasSubtypes = true;
		this.setHasSubtypes(true);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems){
		if (tab == this.getCreativeTab()){
			for (int i = 0; i < SpellRegistry.spellRegistry.size(); i ++){
				ItemStack stack = new ItemStack(this,1);
				createData(stack,SpellRegistry.spellRegistry.keySet().toArray(new String[SpellRegistry.spellRegistry.size()])[i]);
				subItems.add(stack);
			}
		}
	}
	
	public static ItemStack createData(ItemStack stack, String spellName){
		if (!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setString("spell", spellName);
		return stack;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced){
		if (!stack.hasTagCompound()){
			tooltip.add(I18n.format("roots.tooltip.petaldust.notag"));
		}
		else {
			SpellRegistry.spellRegistry.get(stack.getTagCompound().getString("spell")).addToolTip(tooltip);
		}
	}
}
