package epicsquid.roots.integration.crafttweaker.recipes;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.recipe.ChrysopoeiaRecipe;
import epicsquid.roots.util.IngredientWithStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class CTChryospoeiaRecipe extends ChrysopoeiaRecipe {
	private IIngredient input;
	
	// TODO: Update with support for thingy
	public CTChryospoeiaRecipe(IIngredient input, ItemStack outputs) {
		super(IngredientWithStack.EMPTY, outputs);
		this.input = input;
	}
	
	public CTChryospoeiaRecipe(IngredientWithStack inputs, ItemStack outputs) {
		super(inputs, outputs);
	}
	
	@Override
	public IngredientWithStack getIngredient() {
		return new IngredientWithStack(CraftTweakerMC.getIngredient(input), input.getAmount());
	}
	
	@Override
	public boolean matches(ItemStack stack) {
		return input.matches(CraftTweakerMC.getIItemStack(stack));
	}
	
	@Override
	public ItemStack process(EntityPlayer player, ItemStack stack, int overload, int by) {
		if (!matches(stack)) {
			return stack;
		}
		
		if ((stack.getCount() - input.getAmount()) <= 0) {
			return CTTransformer.transformIngredient(input, stack, null);
		}
		
		ItemStack result = stack.copy();
		result.shrink(input.getAmount());
		ItemStack container = CTTransformer.transformIngredient(input, stack, null);
		if (!container.isEmpty()) {
			if (!player.addItemStackToInventory(container)) {
				ItemUtil.spawnItem(player.world, player.getPosition(), container);
			}
		}
		
		return result;
	}
}
