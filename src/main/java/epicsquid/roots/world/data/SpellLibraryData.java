package epicsquid.roots.world.data;

import epicsquid.roots.library.StaffSpellInfo;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.*;

@SuppressWarnings("NullableProblems")
public class SpellLibraryData extends WorldSavedData {
  private static final String identifier = "SpellData-";
  private Map<SpellBase, StaffSpellInfo> spells = new HashMap<>();

  private UUID uuid;

  public static String name(UUID uuid) {
    return identifier + uuid.toString();
  }

  public SpellLibraryData(String name) {
    super(name);
    this.uuid = UUID.fromString(identifier.replace(identifier, ""));
    generateMap();
  }

  public SpellLibraryData(UUID uuid) {
    super (name(uuid));
    this.uuid = uuid;
    generateMap();
  }

  private void generateMap () {
    spells.clear();
    for (SpellBase spell : SpellRegistry.spellRegistry.values()) {
      spells.put(spell, StaffSpellInfo.EMPTY);
    }
  }

  public StaffSpellInfo getData (SpellBase spell) {
    return spells.get(spell);
  }

  @SuppressWarnings("NullableProblems")
  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    NBTTagList list = nbt.getTagList("spells", Constants.NBT.TAG_COMPOUND);
    generateMap();
    for (int i = 0; i < list.tagCount(); i++) {
      StaffSpellInfo instance = StaffSpellInfo.fromNBT(list.getCompoundTagAt(i));
      spells.put(instance.getSpell(), instance);
    }
    uuid = nbt.getUniqueId("uuid");
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    NBTTagList list = new NBTTagList();
    for (StaffSpellInfo instance : spells.values()) {
      if (instance == StaffSpellInfo.EMPTY) {
        continue;
      }
      list.appendTag(instance.serializeNBT());
    }
    compound.setTag("spells", list);
    compound.setUniqueId("uuid", uuid);
    return compound;
  }
}
