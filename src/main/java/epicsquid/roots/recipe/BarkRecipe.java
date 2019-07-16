package epicsquid.roots.recipe;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BarkRecipe {
  private final Block block;
  private final Item item;
  private final BlockPlanks.EnumType type;

  public BarkRecipe(Block block, Item item) {
    this.block = block;
    this.item = item;
    this.type = null;
  }

  public BarkRecipe(BlockPlanks.EnumType type, Item item) {
    this.item = item;
    this.type = type;
    this.block = null;
  }

  public ItemStack getBlockStack () {
    if (this.block == null) {
      if (this.type == BlockPlanks.EnumType.ACACIA || this.type == BlockPlanks.EnumType.DARK_OAK) {
        return new ItemStack(Blocks.LOG2, 1, this.type.getMetadata() - 4);
      } else {
        return new ItemStack(Blocks.LOG, 1, this.type.getMetadata());
      }
    } else {
      return new ItemStack(this.block);
    }
  }

  public ItemStack getBarkStack (int count) {
    return new ItemStack(this.item, count);
  }

  public Block getBlock() {
    return block;
  }

  public Item getItem() {
    return item;
  }

  public BlockPlanks.EnumType getType() {
    return type;
  }
}
