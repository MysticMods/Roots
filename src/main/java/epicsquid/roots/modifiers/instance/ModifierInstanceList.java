package epicsquid.roots.modifiers.instance;

import com.google.common.collect.Iterators;
import epicsquid.roots.modifiers.IModifierList;
import epicsquid.roots.modifiers.ModifierType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.*;
import java.util.function.Consumer;

public class ModifierInstanceList implements IModifierList<ModifierInstance, NBTTagList> {
  private final Map<ModifierType, List<ModifierInstance>> internal;

  public ModifierInstanceList() {
    internal = new HashMap<>();
    for (ModifierType type : ModifierType.values()) {
      internal.put(type, new ArrayList<>());
    }
  }

  @Override
  public void clear() {
    for (ModifierType type : ModifierType.values()) {
      internal.get(type).clear();
    }
  }

  @Override
  public int size() {
    int size = 0;
    for (ModifierType type : ModifierType.values()) {
      size += internal.get(type).size();
    }
    return size;
  }

  @Override
  public boolean isEmpty() {
    for (ModifierType type : ModifierType.values()) {
      if (!internal.get(type).isEmpty()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean contains(Object o) {
    if (!(o instanceof ModifierInstance)) {
      return false;
    }
    return internal.get(((ModifierInstance) o).getType()).contains(o);
  }

  @Override
  public Iterator<ModifierInstance> iterator() {
    List<ModifierIterator> iterators = new ArrayList<>();
    if (!internal.get(ModifierType.NO_COST).isEmpty()) {
      iterators.add(new ModifierIterator(internal.get(ModifierType.NO_COST).iterator()));
    }
    if (!internal.get(ModifierType.ADDITIONAL_COST).isEmpty()) {
      iterators.add(new ModifierIterator(internal.get(ModifierType.ADDITIONAL_COST).iterator()));
    }
    if (!internal.get(ModifierType.ALL_COST_MULTIPLIER).isEmpty()) {
      iterators.add(new ModifierIterator(internal.get(ModifierType.ALL_COST_MULTIPLIER).iterator()));
    }
    return Iterators.concat(iterators.toArray(new ModifierIterator[0]));
  }

  @Override
  public boolean add(ModifierInstance modifierInstance) {
    return internal.get(modifierInstance.getType()).add(modifierInstance);
  }

  @Override
  public boolean remove(Object o) {
    if (!(o instanceof ModifierInstance)) {
      return false;
    }

    return internal.get(((ModifierInstance) o).getType()).remove(o);
  }

  @Override
  public NBTTagList serializeNBT() {
    NBTTagList result = new NBTTagList();
    for (ModifierInstance m : this) {
      result.appendTag(m.serializeNBT());
    }
    return result;
  }

  @Override
  public void deserializeNBT(NBTTagList nbt) {
    this.clear();

    for (int i = 0; i < nbt.tagCount(); i++) {
      NBTTagCompound tag = nbt.getCompoundTagAt(i);
      this.add(ModifierInstance.fromNBT(tag));
    }
  }

  public static ModifierInstanceList fromNBT(NBTTagList tag) {
    ModifierInstanceList result = new ModifierInstanceList();
    result.deserializeNBT(tag);
    return result;
  }

  private static class ModifierIterator implements Iterator<ModifierInstance> {
    private final Iterator<ModifierInstance> internalIterator;

    private ModifierIterator(Iterator<ModifierInstance> internalIterator) {
      this.internalIterator = internalIterator;
    }

    @Override
    public boolean hasNext() {
      return this.internalIterator.hasNext();
    }

    @Override
    public ModifierInstance next() {
      return this.internalIterator.next();
    }

    @Override
    public void remove() {
      this.internalIterator.remove();
    }

    @Override
    public void forEachRemaining(Consumer<? super ModifierInstance> action) {
      this.internalIterator.forEachRemaining(action);
    }
  }
}
