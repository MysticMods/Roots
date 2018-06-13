package teamroots.roots.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import teamroots.roots.util.ListUtil;

public class RitualRecipe {
	public List<ItemStack> ingredients = new ArrayList<ItemStack>();
	public String result = "null";
	
	public RitualRecipe(String result){
		this.result = result;
	}
	
	public RitualRecipe addIngredient(ItemStack stack){
		this.ingredients.add(stack);
		return this;
	}
	
	public boolean matches(List<ItemStack> ingredients){
		return ListUtil.stackListsMatch(ingredients, this.ingredients);
	}
}
