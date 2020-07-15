package epicsquid.roots.recipe;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.util.IngredientWithStack;
import epicsquid.roots.util.types.RegistryItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

public class ChrysopoeiaRecipe extends RegistryItem {
  private IngredientWithStack inputs;
  private ItemStack outputs;

  public ChrysopoeiaRecipe(IngredientWithStack inputs, ItemStack outputs) {
    this.inputs = inputs;
    this.outputs = outputs;
  }

  public IngredientWithStack getIngredient() {
    return inputs;
  }

  public boolean matches(ItemStack stack) {
    return inputs.getIngredient().test(stack) && stack.getCount() >= inputs.getCount();
  }

  public ItemStack process(EntityPlayer player, ItemStack stack) {
    if (!matches(stack)) {
      return stack;
    }

    if ((stack.getCount() - inputs.getCount()) <= 0) {
      return ForgeHooks.getContainerItem(stack);
    }

    ItemStack result = stack.copy();
    result.shrink(inputs.getCount());
    ItemStack container = ForgeHooks.getContainerItem(stack);
    if (!container.isEmpty()) {
      if (!player.addItemStackToInventory(container)) {
        ItemUtil.spawnItem(player.world, player.getPosition(), container);
      }
    }

    return result;
  }

  public ItemStack getOutput() {
    return outputs;
  }
}
