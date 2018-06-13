package teamroots.roots.book;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import teamroots.roots.Constants;

public class Book {
	public ArrayList<Page> pages = new ArrayList<Page>();
	public String name = "";
	public int selectedPage = 0;
	public Book(String name){
		this.name = name;
	}
	
	public Book addPage(Page page){
		this.pages.add(page);
		return this;
	}
	
	public static ItemStack createData(ItemStack blankStack, float knowledge){
		if (!blankStack.hasTagCompound()){
			blankStack.setTagCompound(new NBTTagCompound());
		}
		ItemStack stack = blankStack.copy();
		stack.getTagCompound().setFloat(Constants.KNOWLEDGE, 0);
		return stack;
	}
	
	public static float getKnowledge(ItemStack stack){
		if (stack.hasTagCompound()){
			if (stack.getTagCompound().hasKey(Constants.KNOWLEDGE)){
				return stack.getTagCompound().getFloat(Constants.KNOWLEDGE);
			}
		}
		return 0.0f;
	}
}
