package teamroots.roots.recipe;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import teamroots.roots.RegistryManager;
import teamroots.roots.item.IHerb;
import teamroots.roots.item.ItemHammer;
import teamroots.roots.item.ItemPestle;
import teamroots.roots.item.ItemPouch;

public class RecipeHammerSmash extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	ItemStack victim = ItemStack.EMPTY;
	ItemStack result = ItemStack.EMPTY;
	public RecipeHammerSmash(ItemStack stack, ItemStack result){
		this.victim = stack;
		this.result = result;
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		int hammerCount = 0;
		int itemCount = 0;
		for (int i = 0; i < inv.getSizeInventory(); i ++){
			if (inv.getStackInSlot(i) != ItemStack.EMPTY){
				if (inv.getStackInSlot(i).getItem() instanceof ItemHammer){
					hammerCount ++;
				}
				else if (inv.getStackInSlot(i).getItem() == victim.getItem() && inv.getStackInSlot(i).getMetadata() == victim.getMetadata()){
					itemCount ++;
				}
				else {
					return false;
				}
			}
		}
		return hammerCount == 1 && itemCount == 1;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return result.copy();
	}

	@Override
	public ItemStack getRecipeOutput() {
		return result;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> remaining = NonNullList.create();
		for (int i = 0; i < inv.getSizeInventory(); i ++){
			if (inv.getStackInSlot(i) != ItemStack.EMPTY){
				ItemStack stack = inv.getStackInSlot(i).copy();
				if (stack.getItem() instanceof ItemHammer){
					stack.setItemDamage(stack.getItemDamage()+1);
					if (stack.getItemDamage() <= stack.getMaxDamage()){
						remaining.add(inv.getStackInSlot(i).copy());	
					}
				}
				else if (stack.getItem() == victim.getItem() && stack.getMetadata() == victim.getMetadata()){
					stack.shrink(1);
					if (stack.getCount() > 0){
						remaining.add(stack.copy());
					}
				}
				else {
					remaining.add(ItemStack.EMPTY);
				}
			}
			else {
				remaining.add(ItemStack.EMPTY);
			}
		}
		inv.clear();
		return remaining;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width*height > 2;
	}
}
