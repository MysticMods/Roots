package epicsquid.roots.modifiers.modifier;

import epicsquid.roots.modifiers.IModifierList;
import epicsquid.roots.spell.SpellBase;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.nbt.NBTTagList;

import java.util.Iterator;

public class ModifierList implements IModifierList<Modifier> {
  private Object2BooleanOpenHashMap<Modifier> map = new Object2BooleanOpenHashMap<>();
  private SpellBase spell;

  public ModifierList(SpellBase spell) {
    this.spell = spell;
    map.defaultReturnValue(false);
    clear();
  }

  @Override
  public void clear() {
    for (Modifier m : spell.getModifiers()) {
      map.put(m, false);
    }
  }

  @Override
  public int size() {
    return (int) map.values().stream().filter(o -> o).count();
  }

  @Override
  public boolean isEmpty() {
    return map.values().stream().anyMatch(o -> o);
  }

  @Override
  public boolean contains(Object o) {
    return map.getBoolean(o);
  }

  @Override
  public boolean add(Modifier modifier) {
    return map.put(modifier, true);
  }

  @Override
  public boolean remove(Object o) {
    return map.put((Modifier) o, false);
  }

  @Override
  public NBTTagList serializeNBT() {
    return null;
  }

  @Override
  public void deserializeNBT(NBTTagList nbt) {

  }

  @Override
  public Iterator<Modifier> iterator() {
    return null;
  }
}
