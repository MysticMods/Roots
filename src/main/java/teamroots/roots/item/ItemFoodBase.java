package teamroots.roots.item;

import java.util.List;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
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

public class ItemFoodBase extends ItemFood implements IModeledItem {
	public int food = 0;
	public float saturation = 0;
	public ItemFoodBase(String name, boolean addToTab, int food, float saturation){
		super(food, saturation, false);
		this.food = food;
		this.saturation = saturation;
		setUnlocalizedName(name);
		setRegistryName(Roots.MODID+":"+name);
		if (addToTab){
			setCreativeTab(Roots.tab);
		}
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack){
		return EnumAction.EAT;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack){
		return 40;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){;
		ItemStack stack = player.getHeldItem(hand);
		if (player.getFoodStats().getFoodLevel() < 20){
			player.setActiveHand(hand);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS,stack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL,player.getHeldItem(hand));
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity){
		if (entity instanceof EntityPlayer){
			((EntityPlayer)entity).getFoodStats().addStats(this.food, this.saturation);
			stack.shrink(1);
		}
		return stack;
	}
	
	@Override
	public void initModel(){
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName().toString()));
	}
}
