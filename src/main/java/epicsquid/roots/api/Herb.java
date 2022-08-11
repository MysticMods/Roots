package epicsquid.roots.api;

import epicsquid.roots.util.types.RegistryItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class Herb extends RegistryItem {
	private Supplier<Item> item;
	private Supplier<ItemStack> stack = null;
	
	public Herb(@Nonnull Supplier<Item> item) {
		this.item = item;
	}
	
	@Nonnull
	public Item getItem() {
		return item.get();
	}
	
	public void setItem(@Nonnull Item item) {
		this.item = () -> item;
	}
	
	public String getName() {
		return getRegistryName().getPath();
	}
	
	public ItemStack getStack() {
		if (stack == null) {
			final ItemStack ourStack = new ItemStack(this.item.get());
			stack = () -> ourStack;
		}
		return stack.get();
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Herb && ((Herb) obj).getRegistryName().equals(this.getRegistryName());
	}
}
