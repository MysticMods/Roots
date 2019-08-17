package epicsquid.roots.recipe;

import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class BarkRecipe {
  private final ResourceLocation name;
  private final Block block;
  private final ItemStack item;
  private final BlockPlanks.EnumType type;

  public BarkRecipe(ResourceLocation name, Block block, ItemStack item) {
    this.block = block;
    this.item = item;
    this.type = null;
    this.name = name;
  }

  public BarkRecipe(ResourceLocation name, BlockPlanks.EnumType type, ItemStack item) {
    this.item = item;
    this.type = type;
    this.block = null;
    this.name = name;
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
    ItemStack copy = this.item.copy();
    copy.setCount(count);
    return copy;
  }

  public Block getBlock() {
    return block;
  }

  public ItemStack getItem() {
    return item;
  }

  public BlockPlanks.EnumType getType() {
    return type;
  }

  public ResourceLocation getName() {
    return name;
  }
}
