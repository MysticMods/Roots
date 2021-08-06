package epicsquid.roots.modifiers.instance.base;

import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.info.AbstractSpellInfo;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class BaseModifierInstanceList<T extends BaseModifierInstance> implements IModifierList<T, NBTTagCompound> {
  protected final List<T> internal;
  protected final Map<IModifierCore, T> coreToInstance;
  protected final SpellBase spell;

  public BaseModifierInstanceList(SpellBase spell, Function<Modifier, T> empty) {
    internal = new ArrayList<>();
    coreToInstance = new HashMap<>();
    for (Modifier m : spell.getModifiers()) {
      add(empty.apply(m));
    }
    this.spell = spell;
  }

  @Override
  public void clear() {
    internal.clear();
    coreToInstance.clear();
  }

  @Override
  public int size() {
    return internal.size();
  }

  @Override
  public boolean isEmpty() {
    return internal.isEmpty();
  }

  @Override
  public Stream<T> stream() {
    return internal.stream();
  }

  @Override
  public boolean contains(Object o) {
    if (o instanceof BaseModifierInstance) {
      return internal.contains(o);
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
  public T get(IModifier modifier) {
    if (modifier == null) {
      return null;
    }
    for (T mi : internal) {
      if (mi.getModifier().equals(modifier)) {
        return mi;
      }
    }
    return null;
  }

  @Override
  public Collection<T> getModifiers() {
    return internal;
  }

  public SpellBase getSpell() {
    return spell;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator<T> iterator() {
    return internal.iterator();
  }

  @Override
  public boolean add(T modifierInstance) {
    IModifierCore core = modifierInstance.getCore();
    coreToInstance.put(core, modifierInstance);
    internal.removeIf(o -> o.getCore().equals(core));
    return internal.add(modifierInstance);
  }

  // TODO?
  @SuppressWarnings("unchecked")
  @Override
  public boolean remove(Object o) {
    return internal.remove(o);
  }

  @Override
  public boolean removeIf(Predicate<? super T> predicate) {
    return internal.removeIf(predicate);
  }

  public Object2DoubleOpenHashMap<Herb> apply(Object2DoubleOpenHashMap<Herb> costs) {
    for (CostType type : CostType.values()) {
      for (T m : this) {
        costs = m.apply(costs, type);
      }
    }
    return costs;
  }

  @Override
  public NBTTagCompound serializeNBT() {
    NBTTagCompound result = new NBTTagCompound();
    NBTTagList list = new NBTTagList();
    for (T m : this) {
      if (m.getModifier() != null) {
        list.appendTag(m.serializeNBT());
      }
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
}
