package epicsquid.roots.modifiers.instance.staff;

import epicsquid.roots.Roots;
import epicsquid.roots.modifiers.IModifier;
import epicsquid.roots.modifiers.IModifierCore;
import epicsquid.roots.modifiers.ModifierCores;
import epicsquid.roots.modifiers.ModifierRegistry;
import epicsquid.roots.modifiers.instance.base.BaseModifierInstanceList;
import epicsquid.roots.modifiers.instance.library.LibraryModifierInstance;
import epicsquid.roots.modifiers.instance.library.LibraryModifierInstanceList;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.info.AbstractSpellInfo;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StaffModifierInstanceList extends BaseModifierInstanceList<StaffModifierInstance> {
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

  public void toBytes (ByteBuf buf) {
    List<IModifierCore> cores = new ArrayList<>();
    for (StaffModifierInstance modifier : this) {
      if (modifier.isEnabled() && modifier.isApplied() && !modifier.isConflicting(this)) {
        cores.add(modifier.getCore());
      }
    }
    buf.writeInt(cores.size());
    for (IModifierCore core : cores) {
      buf.writeShort(core.getKey());
    }
  }

  public static Set<IModifierCore> fromBytes (ByteBuf buf) {
    Set<IModifierCore> modifiers = new HashSet<>();
    int size = buf.readInt();
    for (int i = 0; i < size; i++) {
      int key = buf.readShort();
      IModifierCore core = ModifierCores.getByOrdinal(key);
      if (core == null) {
        throw new NullPointerException("Invalid modifier when StaffModifierInstanceList, ModifierCore ordinal of " + key);
      }
      modifiers.add(core);
    }
    return modifiers;
  }

  public boolean has (IModifier modifier) {
    StaffModifierInstance instance = get(modifier);
    if (instance == null) {
      return false;
    }

    return instance.isApplied() && instance.isEnabled();
  }
}
