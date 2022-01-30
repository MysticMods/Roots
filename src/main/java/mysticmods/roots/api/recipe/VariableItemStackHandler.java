package mysticmods.roots.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class VariableItemStackHandler extends ItemStackHandler implements IVariableHandler {
  public VariableItemStackHandler() {
  }

  public VariableItemStackHandler(int size) {
    super(size);
  }

  public VariableItemStackHandler(NonNullList<ItemStack> stacks) {
    super(stacks);
  }

  @Override
  public NonNullList<ItemStack> getItemStacks() {
    return stacks;
  }
}
