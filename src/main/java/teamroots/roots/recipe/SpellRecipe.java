package teamroots.roots.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import teamroots.roots.util.ListUtil;

public class SpellRecipe {
	public List<ItemStack> ingredients = new ArrayList<ItemStack>();
	public String result = "null";
	
	public SpellRecipe(String result){
		this.result = result;
	}
	
	public SpellRecipe addIngredient(ItemStack stack){
		this.ingredients.add(stack);
		return this;
	}
	
	public boolean matches(List<ItemStack> ingredients){
		return ListUtil.stackListsMatch(ingredients, this.ingredients);
	}
}
