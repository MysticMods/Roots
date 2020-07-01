package epicsquid.roots.modifiers.instance.base;

import com.google.common.collect.Iterators;
import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.modifiers.CostType;
import epicsquid.roots.modifiers.IModifierCore;
import epicsquid.roots.modifiers.IModifierList;
import epicsquid.roots.modifiers.Modifier;
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
  protected final List<T> internal;
  protected final Map<IModifierCore, T> coreToInstance;
  protected final SpellBase spell;

  // TODO
  public BaseModifierInstanceList(SpellBase spell, Function<Modifier, T> empty) {
    internal = new ArrayList<>();
    coreToInstance = new HashMap<>();
    for (Modifier m : spell.getModifierList()) {
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
  public T get(Modifier modifier) {
    for (T mi : internal) {
      if (mi.getModifier().equals(modifier)) {
        return mi;
      }
    }
    return null;
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
}
