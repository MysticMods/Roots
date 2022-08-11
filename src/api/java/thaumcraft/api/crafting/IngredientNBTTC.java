package thaumcraft.api.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import thaumcraft.api.ThaumcraftApiHelper;

import javax.annotation.Nullable;

public class IngredientNBTTC extends Ingredient {
	private final ItemStack stack;
	
	public IngredientNBTTC(ItemStack stack) {
		super(stack);
		this.stack = stack;
	}
	
	@Override
	public boolean apply(@Nullable ItemStack input) {
		if (input == null)
			return false;
		return this.stack.getItem() == input.getItem() &&
				this.stack.getItemDamage() == input.getItemDamage() &&
				ThaumcraftApiHelper.areItemStackTagsEqualRelaxed(this.stack, input);
	}
	
	@Override
	public boolean isSimple() {
		return false;
	}
}
