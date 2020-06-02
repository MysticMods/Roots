package epicsquid.roots.world.data;

import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.spell.info.LibrarySpellInfo;
import epicsquid.roots.spell.info.SpellDustInfo;
import epicsquid.roots.spell.info.storage.DustSpellStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("NullableProblems")
public class SpellLibraryData extends WorldSavedData implements Iterable<LibrarySpellInfo> {
  private static final String identifier = "SpellData-";
  private Map<SpellBase, LibrarySpellInfo> spells = new HashMap<>();
  private List<LibrarySpellInfo> list = null;

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
    super(name(uuid));
    this.uuid = uuid;
    generateMap();
  }

  public SpellLibraryData(EntityPlayer player) {
    super(identifier + player.getCachedUniqueIdString());
    this.uuid = player.getUniqueID();
    generateMap();
  }

  private void generateMap() {
    spells.clear();
    for (SpellBase spell : SpellRegistry.spellRegistry.values()) {
      spells.put(spell, new LibrarySpellInfo(spell));
    }
  }

  public void addSpell(SpellDustInfo info) {
    LibrarySpellInfo libinfo = spells.get(info.getSpell());
    libinfo.setObtained();
    list = null;
    markDirty();
  }

  public void addSpell(SpellBase spell) {
    LibrarySpellInfo info = spells.get(spell);
    info.setObtained();
    list = null;
    markDirty();
  }

  public LibrarySpellInfo getData(SpellBase spell) {
    return spells.get(spell);
  }

  @SuppressWarnings("NullableProblems")
  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    NBTTagList list = nbt.getTagList("spells", Constants.NBT.TAG_COMPOUND);
    generateMap();
    for (int i = 0; i < list.tagCount(); i++) {
      LibrarySpellInfo instance = LibrarySpellInfo.fromNBT(list.getCompoundTagAt(i));
      spells.put(instance.getSpell(), instance);
    }
    uuid = nbt.getUniqueId("uuid");
    list = null;
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    NBTTagList list = new NBTTagList();
    for (LibrarySpellInfo instance : spells.values()) {
      if (instance == null) {
        continue;
      }
      list.appendTag(instance.serializeNBT());
    }
    compound.setTag("spells", list);
    compound.setUniqueId("uuid", uuid);
    return compound;
  }

  public List<LibrarySpellInfo> asList () {
    if (list == null) {
      list = spells.values().stream().filter(LibrarySpellInfo::isObtained).collect(Collectors.toList());
    }
    list.sort(Comparator.comparing(a -> a.getSpell().getRegistryName().getPath()));
    return list;
  }

  @Override
  public Iterator<LibrarySpellInfo> iterator() {
    return asList().iterator();
  }

  @Nullable
  public LibrarySpellInfo get (int slot) {
    List<LibrarySpellInfo> info = asList();
    if (slot < info.size()) {
      return info.get(slot);
    }
    return null;
  }

  public int size () {
    return asList().size();
  }
}
