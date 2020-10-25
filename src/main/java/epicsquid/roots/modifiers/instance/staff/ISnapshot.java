package epicsquid.roots.modifiers.instance.staff;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.modifiers.BaseModifiers;
import epicsquid.roots.modifiers.IModifier;
import epicsquid.roots.spell.ISpellMulitipliers;
import io.netty.buffer.ByteBuf;

public interface ISnapshot extends ISpellMulitipliers {
  int[] toArray();

  boolean has(IModifier modifier);

  default boolean hasRand(IModifier modifier, int rand) {
    return has(modifier) && Util.rand.nextInt(rand) == 0;
  }

  default Buff getAmplify() {
    if (has(BaseModifiers.GREATER_EMPOWER)) {
      return ISpellMulitipliers.Buff.GREATER_BONUS;
    }
    if (has(BaseModifiers.EMPOWER)) {
      return ISpellMulitipliers.Buff.BONUS;
    }
    return ISpellMulitipliers.Buff.NONE;
  }

  default Buff getSpeedy() {
    if (has(BaseModifiers.GREATER_SPEEDY)) {
      return ISpellMulitipliers.Buff.GREATER_BONUS;
    }
    if (has(BaseModifiers.EMPOWER)) {
      return ISpellMulitipliers.Buff.BONUS;
    }
    return ISpellMulitipliers.Buff.NONE;
  }

  default void toBytes(ByteBuf buf) {
  }
}
