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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.roots.RegistryManager;
import teamroots.roots.Roots;
import teamroots.roots.entity.EntityBlinkProjectile;
import teamroots.roots.entity.EntityFairy;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageFairyTameFX;
import teamroots.roots.spell.SpellBase;
import teamroots.roots.spell.SpellRegistry;

public class ItemRiftWand extends ItemBase {
	public ItemRiftWand(String name, boolean addToTab){
		super(name, addToTab);
		this.setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
		ItemStack stack = player.getHeldItem(hand);
		List<EntityBlinkProjectile> projectiles = world.getEntitiesWithinAABB(EntityBlinkProjectile.class, new AxisAlignedBB(player.posX-64.0,player.posY-64.0,player.posZ-64.0,player.posX+64.0,player.posY+64.0,player.posZ+64.0));
		int ind = -1;
		double minDistance = -1;
		if (projectiles.size() > 0){
			for (int i = 0; i < projectiles.size(); i ++){
				double dist = projectiles.get(i).getDistanceSqToEntity(player);
				if (dist < minDistance || minDistance == -1){
					ind = i;
					minDistance = dist;
				}	
			}
			if (ind != -1){
				player.posX = projectiles.get(ind).posX;
				player.posY = projectiles.get(ind).posY-player.height/2.0f;
				player.posZ = projectiles.get(ind).posZ;
				player.velocityChanged = true;
				player.swingArm(hand);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS,stack);
			}
			return new ActionResult<ItemStack>(EnumActionResult.FAIL,stack);
		}
		else {
			if (player.getCooldownTracker().getCooldown(this, 0) == 0 && !world.isRemote){
				player.getCooldownTracker().setCooldown(this, 20);
				EntityBlinkProjectile proj = new EntityBlinkProjectile(world);
				float offX = 0.5f*(float)Math.sin(Math.toRadians(-90.0f-player.rotationYaw));
				float offZ = 0.5f*(float)Math.cos(Math.toRadians(-90.0f-player.rotationYaw));
				proj.initCustom(player.posX+offX, 
						player.posY+player.getEyeHeight(), 
						player.posZ+offZ,  
						player.getLookVec().x*4.0, player.getLookVec().y*4.0, player.getLookVec().z*4.0, 8.0f, player.getUniqueID());
				world.spawnEntity(proj);
			}
			player.swingArm(hand);
			return new ActionResult<ItemStack>(EnumActionResult.FAIL,stack);
		}
	}
}
