package mysticmods.roots.api.spell;

import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;

public record SpellCastResult(ResultType type, int operations, int cooldown) {
  public enum ResultType {
    NOOP,
    SUCCESS,
    FAIL,
    TICK;
  }

  public InteractionResultHolder<ItemStack> result(ItemStack stack) {
    if (type == ResultType.SUCCESS || type == ResultType.TICK) {
      return InteractionResultHolder.success(stack);
    } else if (type == ResultType.NOOP) {
      return InteractionResultHolder.consume(stack);
    } else {
      return InteractionResultHolder.fail(stack);
    }
  }

  public static SpellCastResult nothing() {
    return new SpellCastResult(ResultType.NOOP, 0, 0);
  }

  public static SpellCastResult success(int cooldown) {
    return new SpellCastResult(ResultType.SUCCESS, 0, cooldown);
  }

  public static SpellCastResult success(int operations, int cooldown) {
    return new SpellCastResult(ResultType.SUCCESS, operations, cooldown);
  }

  public static SpellCastResult fail() {
    return new SpellCastResult(ResultType.FAIL, 0, 0);
  }

  public static SpellCastResult tick() {
    return new SpellCastResult(ResultType.TICK, 0, 0);
  }

  public SpellCastResult modify(int newCooldown) {
    return new SpellCastResult(type, operations, newCooldown);
  }

  public boolean success() {
    return this.type() == ResultType.SUCCESS/* || this.type() == ResultType.TICK*/;
  }
}
