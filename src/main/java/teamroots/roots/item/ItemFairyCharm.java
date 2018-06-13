package teamroots.roots.item;

import java.util.List;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
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
import teamroots.roots.entity.EntityFairy;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageFairyTameFX;
import teamroots.roots.spell.SpellBase;
import teamroots.roots.spell.SpellRegistry;

public class ItemFairyCharm extends ItemBase {
	public ItemFairyCharm(String name, boolean addToTab){
		super(name, addToTab);
		this.setMaxStackSize(1);
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand){
		if (target instanceof EntityFairy){
			EntityFairy f = (EntityFairy)target;
			f.getDataManager().set(f.tame, true);
			f.owner = player.getUniqueID();
			player.setHeldItem(hand, ItemStack.EMPTY);
			PacketHandler.INSTANCE.sendToAll(new MessageFairyTameFX(f.posX,f.posY+f.height/2.0f,f.posZ));
			return true;
		}
		return false;
	}
}
