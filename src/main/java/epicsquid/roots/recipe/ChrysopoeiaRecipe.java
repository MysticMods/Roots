package epicsquid.roots.recipe;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.util.IngredientWithStack;
import epicsquid.roots.util.types.RegistryItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

public class ChrysopoeiaRecipe extends RegistryItem {
  private final IngredientWithStack inputs;
  private final IngredientWithStack reversedInputs;
  private final ItemStack outputs;
  private final ItemStack inverted;
  private final boolean canReverse;
  private final float overload;
  private final float byproductChance;
  private final ItemStack byproduct;
  private final ItemStack byproductInverted;

  public ChrysopoeiaRecipe(IngredientWithStack inputs, ItemStack inverted, ItemStack outputs, float overload, ItemStack byproduct, ItemStack byproductInverted, float byproductChance, boolean canReverse) {
    this.inputs = inputs;
    this.reversedInputs = new IngredientWithStack(outputs);
    this.outputs = outputs;
    this.canReverse = canReverse;
    this.overload = overload;
    this.byproduct = byproduct;
    this.byproductInverted = byproductInverted;
    this.byproductChance = byproductChance;
    this.inverted = inverted;
  }

  public IngredientWithStack getIngredient() {
    return inputs;
  }

  public boolean matches(ItemStack stack, boolean inverted) {
    if (!inverted) {
      return inputs.getIngredient().test(stack) && stack.getCount() >= inputs.getCount();
    } else {
      return reversedInputs.getIngredient().test(stack) && stack.getCount() >= reversedInputs.getCount();
    }
  }

  public ItemStack process(EntityPlayer player, ItemStack stack, int overload, int by, boolean inverted) {
    if (!matches(stack, inverted)) {
      return stack;
    }

    int count = inverted ? reversedInputs.getCount() : inputs.getCount();

    if ((stack.getCount() - count) <= 0) {
      return ForgeHooks.getContainerItem(stack);
    }

    ItemStack result = stack.copy();
    result.shrink(count);
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

  public ItemStack getCraftingResult (int overloadAmount, boolean inverted) {
    ItemStack result;
    if (inverted && canReverse) {
      if (this.inverted.isEmpty()) {
        return ItemStack.EMPTY;
      } else {
        result = this.inverted.copy();
      }
    } else if (inverted) {
      return ItemStack.EMPTY;
    } else {
      result = outputs.copy();
    }

    if (result.isEmpty()) {
      return ItemStack.EMPTY;
    }

    if (overloadAmount > 0) {
      int multiplier = Util.floatChance(byproductChance * overloadAmount);
      result.setCount(byproduct.getCount() * multiplier);
    }
    return result;
  }

  public ItemStack getByproduct (int byproductAmount, boolean inverted) {
    ItemStack result;
    if (inverted && canReverse) {
      if (byproductInverted.isEmpty()) {
        return ItemStack.EMPTY;
      } else {
        result = byproductInverted.copy();
      }
    } else if (inverted) {
      return ItemStack.EMPTY;
    } else {
      result = byproduct.copy();
    }

    if (result.isEmpty()) {
      return ItemStack.EMPTY;
    }

    if (byproductAmount > 0) {
      int multiplier = Util.floatChance(byproductChance * byproductAmount);
      result.setCount(byproduct.getCount() * multiplier);
    }
    return result;
  }
}
