package epicsquid.roots.modifiers.instance.staff;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.modifiers.IModifier;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.IntArraySet;

public class ModifierSnapshot {
  private static IntArraySet EMPTY = new IntArraySet();

  private IntArraySet modifiers;

  public ModifierSnapshot() {
    this(EMPTY);
  }

  public ModifierSnapshot(IntArraySet modifiers) {
    this.modifiers = modifiers;
  }

  public ModifierSnapshot(int[] modifiers) {
    this.modifiers = new IntArraySet(modifiers);
  }

  public boolean has (IModifier modifier) {
    return this.modifiers.contains(modifier.getCore().getKey());
  }

  public boolean hasRand (IModifier modifier, int rand) {
    return has(modifier) && Util.rand.nextInt(rand) == 0;
  }

  public void toBytes (ByteBuf buf) {
    buf.writeShort(modifiers.size());
    for (int i : modifiers) {
      buf.writeInt(i);
    }
  }
}
