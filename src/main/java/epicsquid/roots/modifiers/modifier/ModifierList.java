package epicsquid.roots.modifiers.modifier;

import epicsquid.roots.modifiers.IModifierList;
import epicsquid.roots.modifiers.ModifierRegistry;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.Iterator;

public class ModifierList implements IModifierList<Modifier, NBTTagCompound> {
  private Object2BooleanOpenHashMap<Modifier> map = new Object2BooleanOpenHashMap<>();
  private SpellBase spell;

  public ModifierList(SpellBase spell) {
    map.defaultReturnValue(false);
    this.spell = spell;
    clear();
  }

  @Override
  public void clear() {
    map.clear();
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
  public Iterator<Modifier> iterator() {
    return map.keySet().iterator();
  }

  @Override
  public NBTTagCompound serializeNBT() {
    NBTTagCompound result = new NBTTagCompound();
    result.setString("s", spell.getRegistryName().toString());
    for (Object2BooleanMap.Entry<Modifier> entry : map.object2BooleanEntrySet()) {
      if (!entry.getBooleanValue()) {
        continue;
      }
      result.setBoolean(entry.getKey().getRegistryName().toString(), true);
    }
    return result;
  }

  @Override
  public void deserializeNBT(NBTTagCompound nbt) {
    this.spell = SpellRegistry.getSpell(nbt.getString("s"));
    clear();
    nbt.removeTag("s");
    for (String modifierName : nbt.getKeySet()) {
      Modifier m = ModifierRegistry.get(new ResourceLocation(modifierName));
      map.put(m, true);
    }
  }
}
