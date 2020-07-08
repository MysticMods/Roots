package epicsquid.roots.modifiers;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.Collection;

public interface IModifierList<T extends IModifier, V extends NBTBase> extends Iterable<T>, INBTSerializable<V> {
  void clear();

  int size();

  boolean isEmpty();

  boolean contains(Object o);

  @Nullable
  T getByCore (IModifierCore core);

  @Nullable
  T get (Modifier modifier);

  Collection<T> getModifiers ();

  boolean add(T modifierInstance);

  boolean remove(Object o);
}
