package teamroots.roots.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class LecternScribingRecipe {
	public ItemStack book = ItemStack.EMPTY;
	public ItemStack core = ItemStack.EMPTY;
	public List<ItemStack> ingredients = new ArrayList<ItemStack>();
	public ItemStack result = ItemStack.EMPTY;
	public LecternScribingRecipe(ItemStack book, ItemStack core, ItemStack result){
		this.book = book;
		this.core = core;
		this.result = result;
	}
	
	public boolean matches(ItemStack book, ItemStack core){
		if (book != ItemStack.EMPTY && core != ItemStack.EMPTY){
			return book.getItem() == this.book.getItem() && book.getItemDamage() == this.book.getItemDamage() && core.getItem() == this.core.getItem() && core.getItemDamage() == this.core.getItemDamage();
		}
		return false;
	}
	
	public LecternScribingRecipe addIngredient(ItemStack stack){
		this.ingredients.add(stack);
		return this;
	}
}
