package mysticmods.roots.api.spell;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;

public record SpellCastResult (ResultType type, int operations, int cooldown) {
  public enum ResultType {
    NOOP,
    SUCCESS,
    FAIL;
  }

  public InteractionResultHolder<ItemStack> result (ItemStack stack) {
    if (type == ResultType.SUCCESS) {
      return InteractionResultHolder.success(stack);
    } else {
      return InteractionResultHolder.fail(stack);
    }
  }
}
