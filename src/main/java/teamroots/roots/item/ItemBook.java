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

public class ItemBook extends ItemBase {
	public int guiId = 0;
	public ItemBook(String name, boolean addToTab, int gui){
		super(name, addToTab);
		this.guiId = gui;
		this.setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){;
		player.openGui(Roots.instance, guiId + (hand == EnumHand.MAIN_HAND ? 0 : 1), world, (int)player.posX, (int)player.posY, (int)player.posZ);
		return new ActionResult<ItemStack>(EnumActionResult.FAIL,player.getHeldItem(hand));
	}
}
