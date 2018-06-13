package teamroots.roots.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.roots.util.ListUtil;

public class MortarRecipe {
	public ItemStack result = ItemStack.EMPTY;
	
	public List<ItemStack> ingredients = new ArrayList<ItemStack>();
	
	public float r1, g1, b1, r2, g2, b2;
	
	public MortarRecipe(ItemStack result, ItemStack[] ingredients, float red1, float green1, float blue1, float red2, float green2, float blue2){
		this.result = result;
		for (int i = 0; i < ingredients.length; i ++){
			this.ingredients.add(ingredients[i]);
		}
		this.r1 = red1;
		this.g1 = green1;
		this.b1 = blue1;
		this.r2 = red2;
		this.g2 = green2;
		this.b2 = blue2;
	}
	
	public boolean matches(List<ItemStack> ingredients){
		return ListUtil.stackListsMatch(ingredients, this.ingredients);
	}
	
	public ItemStack getResult(List<ItemStack> ingredients, World world, BlockPos pos){
		return result;
	}
}
