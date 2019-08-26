package epicsquid.roots.recipe;

import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BarkRecipe {
  private final ResourceLocation name;
  private final ItemStack blockStack;
  private final ItemStack item;
  private final BlockPlanks.EnumType type;

  public BarkRecipe(ResourceLocation name, ItemStack result, ItemStack blockStack) {
    this.blockStack = blockStack;
    this.item = result;
    this.type = null;
    this.name = name;
  }

  public BarkRecipe(ResourceLocation name, ItemStack result, BlockPlanks.EnumType type) {
    this.item = result;
    this.type = type;
    this.name = name;
    this.blockStack = null;
  }

  public ItemStack getBlockStack() {
    if (this.blockStack == null) {
      if (this.type == BlockPlanks.EnumType.ACACIA || this.type == BlockPlanks.EnumType.DARK_OAK) {
        return new ItemStack(Blocks.LOG2, 1, this.type.getMetadata() - 4);
      } else {
        return new ItemStack(Blocks.LOG, 1, Objects.requireNonNull(this.type).getMetadata());
      }
    } else {
      return this.blockStack;
    }
  }

  public ItemStack getBarkStack(int count) {
    ItemStack copy = this.item.copy();
    copy.setCount(count);
    return copy;
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
