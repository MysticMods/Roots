package teamroots.roots.item;

import java.util.List;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
import teamroots.roots.util.FeyMagicManager;

public class ItemSpritelyBrew extends ItemBase{
	public ItemSpritelyBrew(String name, boolean addToTab){
		super(name, true);
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack){
		return EnumAction.DRINK;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack){
		return 40;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){;
		ItemStack stack = player.getHeldItem(hand);
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS,stack);
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity){
		if (entity instanceof EntityPlayer){
			FeyMagicManager.setFeyMagic((EntityPlayer)entity, FeyMagicManager.MAX_VALUE);
			stack.shrink(1);
			if (!world.isRemote){
				world.spawnEntity(new EntityItem(world,entity.posX,entity.posY+entity.height/2.0f,entity.posZ,new ItemStack(Items.GLASS_BOTTLE,1)));
			}
		}
		return stack;
	}
	
	@Override
	public void initModel(){
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName().toString()));
	}
}
