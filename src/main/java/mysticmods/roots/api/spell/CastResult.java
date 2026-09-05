package mysticmods.roots.api.spell;

import mysticmods.roots.api.herb.Costing;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public record CastResult(ResultType type, int operations, int cooldown) {
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

  public static CastResult nothing() {
    return new CastResult(ResultType.NOOP, 0, 0);
  }

  public static CastResult success(int cooldown) {
    return new CastResult(ResultType.SUCCESS, 0, cooldown);
  }

  public static CastResult tickFromCosting (int cooldown, Costing costing) {
    return fromCosting(cooldown, costing, CastResult::tick);
  }

  public static CastResult fromCosting (int cooldown, Costing costing) {
    return fromCosting(cooldown, costing, CastResult::nothing);
  }

  public static CastResult fromCosting (int cooldown, Costing costing, Supplier<CastResult> defaultResult) {
    if (costing.hasNoCharge()) {
      return defaultResult.get();
    } else {
      return CastResult.success(costing.operations(), cooldown);
    }
  }

  public static CastResult success(int operations, int cooldown) {
    return new CastResult(ResultType.SUCCESS, operations, cooldown);
  }

  public static CastResult fail() {
    return new CastResult(ResultType.FAIL, 0, 0);
  }

  public static CastResult tick() {
    return new CastResult(ResultType.TICK, 0, 0);
  }

  public CastResult modify(int newCooldown) {
    return new CastResult(type, operations, newCooldown);
  }

  public boolean success() {
    return this.type() == ResultType.SUCCESS/* || this.type() == ResultType.TICK*/;
  }
}
