package epicsquid.roots.modifiers;

import epicsquid.roots.modifiers.modifier.IModifierCore;
import epicsquid.roots.modifiers.modifier.Modifier;
import epicsquid.roots.spell.SpellBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public interface IModifierList<T extends IModifier, V extends NBTBase> extends Iterable<T>, INBTSerializable<V> {
  void clear();

  int size();

  boolean isEmpty();

  boolean contains(Object o);

  @Nullable
  T getByCore (IModifierCore core);

  @Nullable
  T get (Modifier modifier);

  boolean add(T modifierInstance);

  boolean remove(Object o);
}
