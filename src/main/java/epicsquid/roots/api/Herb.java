package epicsquid.roots.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * Registery class for herbs. Acts as a wrapper for an item that is a herb.
 * <p>
 * See RegisterHerbEvent and HerbRegistery for how to add a herb
 */
public class Herb extends IForgeRegistryEntry.Impl<Herb> {

  /**
   * Herb Items
   */
  private Supplier<Item> item;
  private Supplier<ItemStack> stack = null;

  public Herb(@Nonnull Supplier<Item> item, ResourceLocation resourceLocation) {
    super();
    this.item = item;
    setRegistryName(resourceLocation);
  }

  @Nonnull
  public String getName() {
    return getRegistryName().getPath();
  }

  @Nonnull
  public Item getItem() {
    return item.get();
  }

  public void setItem(@Nonnull Item item) {
    this.item = () -> item;
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
    return obj instanceof Herb && ((Herb) obj).getName().equals(this.getName());
  }
}
