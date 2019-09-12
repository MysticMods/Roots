package epicsquid.roots.grove;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

public class GrovePower {
  private final GroveType grove;
  private final GrovePowerType type;
  private ItemStack item;

  public GrovePower (GroveType grove, GrovePowerType type, ItemStack item) {
    this.grove = grove;
    this.type = type;
    this.item = item;
    if (!(this.item.getItem() instanceof ItemBlock)) {
      throw new InvalidGroveBlockException("GrovePower item must be an itemblock: " + this.item.toString());
    }
  }

  public List<IBlockState> getStates () {
    if (statesFinal == null) {
      ItemBlock item = (ItemBlock) this.item.getItem();
      Block block = item.getBlock();
      IBlockState state = block.getStateFromMeta(this.item.getMetadata());
    }
  }

  public static class InvalidGroveBlockException extends NullPointerException {
    public InvalidGroveBlockException(String s) {
      super(s);
    }
  }
}
