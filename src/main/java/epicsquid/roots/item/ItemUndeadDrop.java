package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.recipe.SpiritDrops;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public class ItemUndeadDrop extends ItemBase {
	private DropType type;
	
	public ItemUndeadDrop(@Nonnull String name, DropType type) {
		super(name);
		this.type = type;
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		switch (type) {
			default:
			case POUCH:
				return EnumRarity.UNCOMMON;
			case RELIQUARY:
				return EnumRarity.RARE;
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		if (!worldIn.isRemote) {
			ItemStack result = type == DropType.POUCH ? SpiritDrops.getRandomPouch() : SpiritDrops.getRandomReliquary();
			if (!playerIn.addItemStackToInventory(result)) {
				ItemUtil.spawnItem(worldIn, playerIn.getPosition(), result);
			}
			
			if (!playerIn.isCreative()) {
				stack.shrink(1);
			}
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		tooltip.add("");
		switch (type) {
			default:
			case POUCH:
				tooltip.add(TextFormatting.YELLOW + I18n.format("roots.spirit_pouch.tooltip"));
				break;
			case RELIQUARY:
				tooltip.add(TextFormatting.YELLOW + I18n.format("roots.reliquary.tooltip"));
				break;
		}
		
	}
	
	public enum DropType {
		RELIQUARY, POUCH
	}
}
