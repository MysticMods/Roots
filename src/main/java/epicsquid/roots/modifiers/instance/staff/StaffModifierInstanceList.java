package epicsquid.roots.modifiers.instance.staff;

import epicsquid.roots.modifiers.IModifier;
import epicsquid.roots.modifiers.IModifierCore;
import epicsquid.roots.modifiers.ModifierCores;
import epicsquid.roots.modifiers.instance.base.BaseModifierInstanceList;
import epicsquid.roots.modifiers.instance.library.LibraryModifierInstance;
import epicsquid.roots.modifiers.instance.library.LibraryModifierInstanceList;
import epicsquid.roots.spell.ISpellMulitipliers;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.info.AbstractSpellInfo;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StaffModifierInstanceList extends BaseModifierInstanceList<StaffModifierInstance> implements ISpellMulitipliers, ISnapshot {
  public StaffModifierInstanceList(SpellBase spell) {
    super(spell, StaffModifierInstance::new);
  }

  @Override
  public void deserializeNBT(NBTTagCompound tag) {
    super.deserializeNBT(tag, StaffModifierInstance::fromNBT);
  }

  public static StaffModifierInstanceList fromNBT(NBTTagCompound tag) {
    StaffModifierInstanceList result = new StaffModifierInstanceList(AbstractSpellInfo.getSpellFromTag(tag));
    result.deserializeNBT(tag);
    return result;
  }

  public static StaffModifierInstanceList fromLibrary(LibraryModifierInstanceList incoming) {
    StaffModifierInstanceList result = new StaffModifierInstanceList(incoming.getSpell());
    for (LibraryModifierInstance modifier : incoming) {
      result.add(modifier.toStaff());
    }
    return result;
  }

  public LibraryModifierInstanceList toLibrary() {
    LibraryModifierInstanceList result = new LibraryModifierInstanceList(spell);
    for (StaffModifierInstance modifier : this) {
      result.add(modifier);
    }
    return result;
  }

  private List<IModifierCore> getCores () {
    List<IModifierCore> cores = new ArrayList<>();
    for (StaffModifierInstance modifier : this) {
      if (modifier.isEnabled() && modifier.isApplied() && !modifier.isConflicting(this)) {
        cores.add(modifier.getCore());
      }
    }
    return cores;
  }

  @Override
  public void toBytes(ByteBuf buf) {
    List<IModifierCore> cores = getCores();
    buf.writeInt(cores.size());
    for (IModifierCore core : cores) {
      buf.writeInt(core.getKey());
    }
  }

  @Override
  public void toCompound (NBTTagCompound tag) {
    tag.setIntArray("modifiers", getCores().stream().mapToInt(IModifierCore::getKey).toArray());
  }

  @Override
  public int[] toArray() {
    return getCores().stream().mapToInt(IModifierCore::getKey).toArray();
  }

  public static ModifierSnapshot fromSnapshot(NBTTagCompound tag, SpellBase spell) {
    if (tag.hasKey(spell.getCachedName(), Constants.NBT.TAG_INT_ARRAY)) {
      return new ModifierSnapshot(tag.getIntArray(spell.getCachedName()));
    } else {
      return new ModifierSnapshot();
    }
  }

  public static ModifierSnapshot fromBytes(ByteBuf buf) {
    IntArraySet modifiers = new IntArraySet();
    int size = buf.readInt();
    for (int i = 0; i < size; i++) {
      modifiers.add(buf.readInt());
    }
    return new ModifierSnapshot(modifiers);
  }

  @Override
  public boolean has(IModifier modifier) {
    StaffModifierInstance instance = get(modifier);
    if (instance == null) {
      return false;
    }

    return instance.isApplied() && instance.isEnabled();
  }
}
