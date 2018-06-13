package teamroots.roots.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import teamroots.roots.RegistryManager;
import teamroots.roots.item.IHerb;
import teamroots.roots.item.ItemPestle;
import teamroots.roots.item.ItemPouch;

public class RecipePowderMash extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		int pestleCount = 0;
		int herbCount = 0;
		int pouchCount = 0;
		String plantName = "";
		String pouchPlant = "";
		for (int i = 0; i < inv.getSizeInventory(); i ++){
			if (inv.getStackInSlot(i) != ItemStack.EMPTY){
				if (inv.getStackInSlot(i).getItem() instanceof IHerb){
					herbCount ++;
					plantName = inv.getStackInSlot(i).getItem().getUnlocalizedName();
				}
				else if (inv.getStackInSlot(i).getItem() instanceof ItemPouch){
					pouchCount ++;
					if (inv.getStackInSlot(i).hasTagCompound()){
						if (inv.getStackInSlot(i).getTagCompound().hasKey("plant")){
							pouchPlant = inv.getStackInSlot(i).getTagCompound().getString("plant");
						}
					}
				}
				else if (inv.getStackInSlot(i).getItem() instanceof ItemPestle){
					pestleCount ++;
				}
				else {
					return false;
				}
			}
		}
		return pestleCount == 1 && herbCount == 1 && pouchCount == 1 && (pouchPlant.compareTo(plantName) == 0 || pouchPlant.compareTo("") == 0);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		String plantName = "";
		int plantStack = 0;
		for (int i = 0; i < inv.getSizeInventory(); i ++){
			if (inv.getStackInSlot(i) != ItemStack.EMPTY){
				if (inv.getStackInSlot(i).getItem() instanceof IHerb){
					plantName = inv.getStackInSlot(i).getItem().getUnlocalizedName();
					plantStack = inv.getStackInSlot(i).getCount();
				}
			}
		}
		for (int i = 0; i < inv.getSizeInventory(); i ++){
			if (inv.getStackInSlot(i) != ItemStack.EMPTY){
				if (inv.getStackInSlot(i).getItem() instanceof ItemPouch){
					ItemStack result = inv.getStackInSlot(i).copy();
					if (!result.hasTagCompound()){
						ItemPouch.createData(result, plantName, plantStack);
					}
					else {
						ItemPouch.setQuantity(result, plantName, ItemPouch.getQuantity(result, plantName)+plantStack);
					}
					return result;
				}
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(RegistryManager.pouch,1);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> remaining = NonNullList.create();
		for (int i = 0; i < inv.getSizeInventory(); i ++){
			if (inv.getStackInSlot(i) != ItemStack.EMPTY){
				if (inv.getStackInSlot(i).getItem() instanceof ItemPestle){
					remaining.add(inv.getStackInSlot(i).copy());
				}
				else {
					remaining.add(ItemStack.EMPTY);
				}
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
