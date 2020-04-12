package epicsquid.roots.modifiers;

import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

public interface IModifierList<T extends IModifier> extends Iterable<T>, INBTSerializable<NBTTagList> {
  void clear();

  int size();

  boolean isEmpty();

  boolean contains(Object o);

  boolean add(T modifierInstance);

  boolean remove(Object o);

  @Override
  NBTTagList serializeNBT();

  @Override
  void deserializeNBT(NBTTagList nbt);
}
