package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.network.MessageClearToasts;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.*;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.patchouli.common.handler.AdvancementSyncHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@SuppressWarnings("deprecation")
public class ItemSalmon extends ItemBase {
	public ItemSalmon(@Nonnull String name) {
		super(name);
		setMaxStackSize(1);
	}
	
	@SuppressWarnings("Duplicates")
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer) {
			NBTTagCompound tag = stack.getTagCompound();
			if (tag == null || (!tag.hasKey("crafter", Constants.NBT.TAG_STRING) || !tag.hasKey("advancements", Constants.NBT.TAG_LIST))) {
				return stack;
			}
			
			if (worldIn.isRemote) {
				return ItemStack.EMPTY;
			}
			
			List<ResourceLocation> advancements = new ArrayList<>();
			
			NBTTagList advancementsList = tag.getTagList("advancements", Constants.NBT.TAG_STRING);
			for (int i = 0; i < advancementsList.tagCount(); i++) {
				String adv = advancementsList.getStringTagAt(i);
				if (adv.equals("pacifist")) {
					continue;
				}
				advancements.add(new ResourceLocation(Roots.MODID, adv));
			}
			
			EntityPlayerMP player = (EntityPlayerMP) entityLiving;
			WorldServer world = (WorldServer) player.world;
			AdvancementManager manager = world.getAdvancementManager();
			PlayerAdvancements playerAdvancements = player.getAdvancements();
			
			if (advancements.size() == 1 && advancements.get(0).getPath().equals("everything")) {
				advancements.clear();
				
				for (Advancement adv : manager.getAdvancements()) {
					ResourceLocation id = adv.getId();
					if (id.getNamespace().equals(Roots.MODID) && !id.getPath().equals("pacifist")) {
						advancements.add(id);
					}
				}
			}
			
			for (ResourceLocation adv : advancements) {
				Advancement adv2 = manager.getAdvancement(adv);
				if (adv2 != null) {
					AdvancementProgress progress = playerAdvancements.getProgress(adv2);
					for (String criterion : progress.getRemaningCriteria()) {
						progress.grantCriterion(criterion);
					}
					adv2.getRewards().apply(player);
				}
			}
			playerAdvancements.save();
			playerAdvancements.flushDirty(player);
			MessageClearToasts message = new MessageClearToasts();
			PacketHandler.INSTANCE.sendTo(message, player);
			AdvancementSyncHandler.syncPlayer(player, false);
			worldIn.playSound(null, player.getPosition(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1f, 0.8f);
			player.sendMessage(new TextComponentTranslation("roots.message.salmon_eaten").setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE).setBold(true)));
		}
		
		return ItemStack.EMPTY;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (isInCreativeTab(tab)) {
			ItemStack inTab = new ItemStack(this);
			NBTTagCompound tag = inTab.getTagCompound();
			if (tag == null) {
				tag = new NBTTagCompound();
				inTab.setTagCompound(tag);
			}
			tag.setString("crafter", "Nature");
			NBTTagList advancements = new NBTTagList();
			advancements.appendTag(new NBTTagString("everything"));
			tag.setTag("advancements", advancements);
			items.add(inTab);
		}
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 32;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.EAT;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		return ActionResult.newResult(EnumActionResult.SUCCESS, itemstack);
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.EPIC;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		tooltip.add("");
		
		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null || !tag.hasKey("crafter", Constants.NBT.TAG_STRING) && !tag.hasKey("advancements", Constants.NBT.TAG_LIST)) {
			tooltip.add(TextFormatting.BOLD + "" + TextFormatting.RED + I18n.format("roots.tooltip.salmon.invalid"));
		} else {
			String crafter = tag.getString("crafter");
			NBTTagList advancements = tag.getTagList("advancements", Constants.NBT.TAG_STRING);
			tooltip.add(I18n.format("roots.tooltip.salmon.crafter", crafter));
			StringJoiner joiner = new StringJoiner(", ", "[", "]");
			for (int i = 0; i < advancements.tagCount(); i++) {
				joiner.add(advancements.getStringTagAt(i));
			}
			tooltip.add(I18n.format("roots.tooltip.salmon.advancements", joiner.toString()));
		}
	}
}
