package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
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
public class ItemGlassEye extends ItemBase {
	public ItemGlassEye(@Nonnull String name) {
		super(name);
		setMaxStackSize(64);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		if (!worldIn.isRemote) {
			if (playerIn.getActivePotionEffect(MobEffects.NIGHT_VISION) != null) {
				return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
			}
			playerIn.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 15 * 20, 0, false, false));
			
			if (!playerIn.isCreative()) {
				stack.shrink(1);
			}
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.UNCOMMON;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		tooltip.add("");
		tooltip.add(TextFormatting.YELLOW + I18n.format("roots.glass_eye.tooltip"));
	}
}
