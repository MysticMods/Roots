package epicsquid.roots.item.runed;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.item.ILivingRepair;
import epicsquid.roots.item.ItemDruidKnife;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRunedKnife extends ItemDruidKnife implements ILivingRepair {
	public ItemRunedKnife(String name, ToolMaterial material) {
		super(name, material);
		this.attackDamage = 4.5f;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		update(stack, worldIn, entityIn, itemSlot, isSelected, 90);
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (super.hitEntity(stack, target, attacker)) {
			if (Util.rand.nextBoolean()) {
				PotionEffect bleed = target.getActivePotionEffect(ModPotions.bleeding);
				if (bleed == null) {
					target.addPotionEffect(new PotionEffect(ModPotions.bleeding, 30));
				} else {
					target.addPotionEffect(new PotionEffect(ModPotions.bleeding, 30 + bleed.getDuration(), Math.max(2, bleed.getAmplifier() + 1)));
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		tooltip.add("");
		tooltip.add(TextFormatting.BOLD + "" + TextFormatting.DARK_RED + I18n.format("roots.tooltip.runed_dagger"));
	}
}
