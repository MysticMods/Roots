package epicsquid.roots.item.runed;

import epicsquid.mysticallib.item.ItemAxeBase;
import epicsquid.roots.item.ILivingRepair;
import epicsquid.roots.recipe.ingredient.RootsIngredients;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;

public class ItemRunedAxe extends ItemAxeBase implements ILivingRepair {
	public ItemRunedAxe(ToolMaterial material, String name) {
		super(material, name, 3, 1992, () -> Ingredient.EMPTY);
		this.attackDamage = 9.5f;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		update(stack, worldIn, entityIn, itemSlot, isSelected, 90);
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return toRepair.getItem() == this && RootsIngredients.RUNED_OBSIDIAN.test(repair);
	}
}
