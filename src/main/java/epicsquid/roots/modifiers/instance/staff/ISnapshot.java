package epicsquid.roots.modifiers.instance.staff;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.modifiers.IModifier;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundNBT;

public interface ISnapshot {
  int[] toArray();

  boolean has(IModifier modifier);

  default boolean hasRand(IModifier modifier, int rand) {
    return has(modifier) && Util.rand.nextInt(rand) == 0;
  }

  default void toBytes(ByteBuf buf) {
  }

  default void toCompound(CompoundNBT tag) {
  }
}
