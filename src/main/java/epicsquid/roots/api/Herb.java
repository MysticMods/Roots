package epicsquid.roots.api;

import javax.annotation.Nonnull;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Registery class for herbs. Acts as a wrapper for an item that is a herb.
 *
 * See RegisterHerbEvent and HerbRegistery for how to add a herb
 */
public class Herb extends IForgeRegistryEntry.Impl<Herb> {

  /**
   * Herb Items
   */
  private Item item;

  public Herb(@Nonnull Item item, ResourceLocation resourceLocation) {
    super();
    this.item = item;
    setRegistryName(resourceLocation);
  }

  @Nonnull
  public Item getItem() {
    return item;
  }

  public void setItem(@Nonnull Item item) {
    this.item = item;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Herb && ((Herb) obj).getItem().getUnlocalizedName().equals(getItem().getUnlocalizedName());
  }
}
