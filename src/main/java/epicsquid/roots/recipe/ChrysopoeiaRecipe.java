package epicsquid.roots.recipe;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.util.IngredientWithStack;
import epicsquid.roots.util.types.RegistryItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

public class ChrysopoeiaRecipe extends RegistryItem {
	private final IngredientWithStack inputs;
	private final ItemStack outputs;
	private final float overload;
	private final float byproductChance;
	private final ItemStack byproduct;
	
	public ChrysopoeiaRecipe(IngredientWithStack inputs, ItemStack outputs) {
		this(inputs, outputs, ItemStack.EMPTY, 0, 0);
	}
	
	public ChrysopoeiaRecipe(IngredientWithStack inputs, ItemStack outputs, ItemStack byproduct, float overload, float byproductChance) {
		this.inputs = inputs;
		this.outputs = outputs;
		this.overload = overload;
		this.byproduct = byproduct;
		this.byproductChance = byproductChance;
	}
	
	public IngredientWithStack getIngredient() {
		return inputs;
	}
	
	public boolean matches(ItemStack stack) {
		return inputs.getIngredient().test(stack) && stack.getCount() >= inputs.getCount();
	}
	
	public ItemStack process(EntityPlayer player, ItemStack stack, int overload, int by) {
		if (!matches(stack)) {
			return stack;
		}
		
		int count = inputs.getCount();
		
		if ((stack.getCount() - count) <= 0) {
			return ForgeHooks.getContainerItem(stack);
		}
		
		ItemStack result = stack.copy();
		result.shrink(count);
		ItemStack container = ForgeHooks.getContainerItem(stack);
		if (!container.isEmpty()) {
			if (!player.addItemStackToInventory(container)) {
				ItemUtil.spawnItem(player.world, player.getPosition(), container);
			}
		}
		
		return result;
	}
	
	public ItemStack getOutput() {
		return outputs;
	}
	
	public ItemStack getCraftingResult(int overloadAmount) {

/*    if (overloadAmount > 0) {
      int multiplier = Util.floatChance(byproductChance * overloadAmount);
      result.setCount(byproduct.getCount() * multiplier);
    }*/
		return outputs.copy();
	}

/*  public ItemStack getByproduct(int byproductAmount) {
    ItemStack result = byproduct.copy();

    if (byproductAmount > 0) {
      int multiplier = Util.floatChance(byproductChance * byproductAmount);
      result.setCount(byproduct.getCount() * multiplier);
    }
    return result;
  }*/
}
