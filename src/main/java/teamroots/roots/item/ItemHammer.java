package teamroots.roots.item;

import java.util.List;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.roots.RegistryManager;
import teamroots.roots.Roots;
import teamroots.roots.spell.SpellBase;
import teamroots.roots.spell.SpellRegistry;
import teamroots.roots.util.Misc;

public class ItemHammer extends ItemTool implements IModeledItem {
	public ItemHammer(String name, boolean addToTab, ToolMaterial material){
		super(7.0f, -3.4f, material, Sets.newHashSet(new Block[]{Blocks.PLANKS}));
		setUnlocalizedName(name);
		setRegistryName(Roots.MODID+":"+name);
		if (addToTab){
			setCreativeTab(Roots.tab);
		}
		this.setContainerItem(this);
	}
	
	@Override
	public Item getContainerItem(){
		return this;
	}
	
	@Override
	public boolean hasContainerItem(){
		return true;
	}
	
	@Override
	public boolean hasContainerItem(ItemStack stack){
		return true;
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack stack){
		return new ItemStack(this,1,stack.getItemDamage()+1);
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker){
        stack.damageItem(1, attacker);
		target.knockBack(attacker, 0.7f, attacker.posX-target.posX, attacker.posZ-target.posZ);
		return true;
	}
	
	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state){
		return this.efficiencyOnProperMaterial;
	}
	
	@Override
	public void initModel(){
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName().toString()));
	}
}
