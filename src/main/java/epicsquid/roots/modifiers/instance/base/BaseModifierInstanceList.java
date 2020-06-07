package epicsquid.roots.modifiers.instance.base;

import com.google.common.collect.Iterators;
import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.modifiers.IModifierList;
import epicsquid.roots.modifiers.ModifierType;
import epicsquid.roots.modifiers.modifier.IModifierCore;
import epicsquid.roots.modifiers.modifier.Modifier;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstance;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.info.AbstractSpellInfo;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class BaseModifierInstanceList<T extends BaseModifierInstance> implements IModifierList<T, NBTTagCompound> {
  private final Map<ModifierType, List<T>> internal;
  private final Map<IModifierCore, T> coreToInstance;
  private final SpellBase spell;

  // TODO
  public BaseModifierInstanceList(SpellBase spell, Function<Modifier, T> empty) {
    internal = new HashMap<>();
    coreToInstance = new HashMap<>();
    for (ModifierType type : ModifierType.values()) {
      internal.put(type, new ArrayList<>());
    }
    for (Modifier m : spell.getModifierList()) {
      add(empty.apply(m));
    }
    this.spell = spell;
  }

  @Override
  public void clear() {
    for (ModifierType type : ModifierType.values()) {
      internal.get(type).clear();
    }
    coreToInstance.clear();
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
    if (o instanceof StaffModifierInstance) {
      return internal.get(((StaffModifierInstance) o).getType()).contains(o);
    } else if (o instanceof IModifierCore) {
      return coreToInstance.containsKey(o);
    }

    return false;
  }

  @Override
  @Nullable
  public T getByCore(IModifierCore core) {
    return coreToInstance.get(core);
  }

  @Override
  @Nullable
  public T get(Modifier modifier) {
    for (Map.Entry<ModifierType, List<T>> instances : internal.entrySet()) {
      for (T mi : instances.getValue()) {
        if (mi.getModifier().equals(modifier)) {
          return mi;
        }
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator<T> iterator() {
    List<BaseModifierIterator> iterators = new ArrayList<>();
    if (!internal.get(ModifierType.NO_COST).isEmpty()) {
      iterators.add(new BaseModifierIterator<>(internal.get(ModifierType.NO_COST).iterator()));
    }
    if (!internal.get(ModifierType.ADDITIONAL_COST).isEmpty()) {
      iterators.add(new BaseModifierIterator<>(internal.get(ModifierType.ADDITIONAL_COST).iterator()));
    }
    if (!internal.get(ModifierType.ALL_COST_MULTIPLIER).isEmpty()) {
      iterators.add(new BaseModifierIterator<>(internal.get(ModifierType.ALL_COST_MULTIPLIER).iterator()));
    }
    // TODO?
    return Iterators.concat(iterators.toArray(new BaseModifierIterator[0]));
  }

  @Override
  public boolean add(T modifierInstance) {
    IModifierCore core = modifierInstance.getCore();
    coreToInstance.put(core, modifierInstance);
    internal.get(modifierInstance.getType()).removeIf(o -> o.getCore().equals(core));
    return internal.get(modifierInstance.getType()).add(modifierInstance);
  }

  // TODO?
  @SuppressWarnings("unchecked")
  @Override
  public boolean remove(Object o) {
    if (!(o instanceof StaffModifierInstance)) {
      return false;
    }

    return internal.get(((T) o).getType()).remove(o);
  }

  public Object2DoubleOpenHashMap<Herb> apply (Object2DoubleOpenHashMap<Herb> costs) {
    for (T m : this) {
      costs = m.apply(costs);
    }
    return costs;
  }

  @Override
  public NBTTagCompound serializeNBT() {
    NBTTagCompound result = new NBTTagCompound();
    NBTTagList list = new NBTTagList();
    for (T m : this) {
      list.appendTag(m.serializeNBT());
    }
    result.setString("s", spell.getRegistryName().toString());
    result.setTag("l", list);
    return result;
  }

  public void deserializeNBT(NBTTagCompound tag, Function<NBTTagCompound, T> creator) {
    NBTTagList nbt = tag.getTagList("l", Constants.NBT.TAG_COMPOUND);

    for (int i = 0; i < nbt.tagCount(); i++) {
      NBTTagCompound thisTag = nbt.getCompoundTagAt(i);
      this.add(creator.apply(thisTag));
    }

    SpellBase other = AbstractSpellInfo.getSpellFromTag(tag);
    if (!other.getRegistryName().equals(spell.getRegistryName())) {
      Roots.logger.error("Tried to deserialize mismatched ModifierInstanceLists for spell " + spell.getRegistryName() + ", trying to load for " + other.getRegistryName());
    }
  }

  private static class BaseModifierIterator<T extends BaseModifierInstance> implements Iterator<T> {
    private final Iterator<T> internalIterator;

    private BaseModifierIterator(Iterator<T> internalIterator) {
      this.internalIterator = internalIterator;
    }

    @Override
    public boolean hasNext() {
      return this.internalIterator.hasNext();
    }

    @Override
    public T next() {
      return this.internalIterator.next();
    }

    @Override
    public void remove() {
      this.internalIterator.remove();
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
      this.internalIterator.forEachRemaining(action);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BaseModifierInstanceList that = (BaseModifierInstanceList) o;
    if (internal.size() != that.internal.size()) {
      return false;
    }
    for (ModifierType mod : internal.keySet()) {
      if (!internal.get(mod).equals(that.internal.get(mod))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(internal);
  }
}
