package epicsquid.roots.integration.jei.interact;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;

public class BlockRightClickRecipe {
  private ItemStack input;
  private List<ItemStack> blocks;
  private List<ItemStack> outputs;

  public BlockRightClickRecipe(ItemStack input, List<ItemStack> blocks, List<ItemStack> outputs) {
    this.input = input;
    this.blocks = blocks;
    this.outputs = outputs;
  }

  public ItemStack getInput() {
    return input;
  }

  public List<ItemStack> getBlocks() {
    return blocks;
  }

  public List<ItemStack> getOutputs() {
    return outputs;
  }
}
