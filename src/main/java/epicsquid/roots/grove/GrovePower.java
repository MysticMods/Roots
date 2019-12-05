package epicsquid.roots.grove;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class GrovePower {
  private final GroveType grove;
  private final GrovePowerType type;
  private ItemStack item;

  public GrovePower(GroveType grove, GrovePowerType type, ItemStack item) {
    this.grove = grove;
    this.type = type;
    this.item = item;
    if (!(this.item.getItem() instanceof BlockItem)) {
      throw new InvalidGroveBlockException("GrovePower item must be an itemblock: " + this.item.toString());
    }
  }

  public static class InvalidGroveBlockException extends NullPointerException {
    public InvalidGroveBlockException(String s) {
      super(s);
    }
  }
}
