package epicsquid.roots.modifiers;

import epicsquid.roots.spell.SpellBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

public interface IModifierList<T extends IModifier, V extends NBTBase> extends Iterable<T>, INBTSerializable<V> {
  void clear();

  int size();

  boolean isEmpty();

  boolean contains(Object o);

  boolean add(T modifierInstance);

  boolean remove(Object o);
}
