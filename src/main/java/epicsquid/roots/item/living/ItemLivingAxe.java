package epicsquid.roots.item.living;

import epicsquid.mysticallib.item.ItemAxeBase;
import epicsquid.roots.item.ILivingRepair;
import epicsquid.roots.recipe.ingredient.RootsIngredients;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;

public class ItemLivingAxe extends ItemAxeBase implements ILivingRepair {
	public ItemLivingAxe(ToolMaterial material, String name) {
		super(material, name, 3, 192, () -> Ingredient.EMPTY);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		update(stack, worldIn, entityIn, itemSlot, isSelected, 40);
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return toRepair.getItem() == this && RootsIngredients.BARK.test(repair);
	}
}
