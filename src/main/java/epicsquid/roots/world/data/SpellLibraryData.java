package epicsquid.roots.world.data;

import epicsquid.roots.modifiers.instance.library.LibraryModifierInstance;
import epicsquid.roots.modifiers.instance.library.LibraryModifierInstanceList;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.spell.info.LibrarySpellInfo;
import epicsquid.roots.spell.info.SpellDustInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

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
		this.uuid = UUID.fromString(name.replace(identifier, ""));
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
		SpellBase spell = info.getSpell();
		if (spell != null) {
			LibrarySpellInfo libinfo = spells.get(spell);
			libinfo.setObtained();
			list = null;
			markDirty();
		}
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
	
	@Nullable
	public LibraryModifierInstanceList getModifiers(LibrarySpellInfo incoming) {
		SpellBase spell = incoming.getSpell();
		if (spell != null) {
			LibrarySpellInfo current = getData(spell);
			return current.getModifiers();
		} else {
			return null;
		}
	}
	
	public void updateSpell(LibrarySpellInfo incoming) {
		SpellBase spell = incoming.getSpell();
		if (spell == null) {
			return;
		}
		LibrarySpellInfo current = getData(spell);
		if (!current.isObtained() && incoming.isObtained()) {
			current.setObtained();
		}
		for (LibraryModifierInstance instance : incoming.getModifiers()) {
			if (instance.isApplied()) {
				LibraryModifierInstance currentInstance = current.getModifiers().get(instance.getModifier());
				if (currentInstance == null) {
					throw new NullPointerException("Trying to update " + spell.getRegistryName() + " for player " + uuid + " but incoming modifier list contained " + instance.getModifier().getRegistryName() + " but our copy does not!");
				}
				if (!currentInstance.isApplied()) {
					currentInstance.setApplied();
				}
			}
		}
		list = null;
		markDirty();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList list = nbt.getTagList("spells", Constants.NBT.TAG_COMPOUND);
		generateMap();
		for (int i = 0; i < list.tagCount(); i++) {
			LibrarySpellInfo instance = LibrarySpellInfo.fromNBT(list.getCompoundTagAt(i));
			SpellBase spell = instance.getSpell();
			if (spell != null) {
				spells.put(spell, instance);
			}
		}
		uuid = nbt.getUniqueId("uuid");
		this.list = null;
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
	
	public List<LibrarySpellInfo> asList() {
		if (list == null) {
			list = spells.values()
			             .stream()
			             .filter(o -> o.getSpell() != null)
			             .filter(LibrarySpellInfo::isObtained)
			             .sorted(Comparator.comparing(a -> a.getSpell() == null ? "" : a.getSpell().getRegistryName().getPath()))
			             .collect(Collectors.toList());
		}
		return list;
	}
	
	@Override
	public Iterator<LibrarySpellInfo> iterator() {
		return asList().iterator();
	}
	
	@Nullable
	public LibrarySpellInfo get(int slot) {
		List<LibrarySpellInfo> info = asList();
		if (slot < info.size()) {
			return info.get(slot);
		}
		return null;
	}
	
	public int size() {
		return asList().size();
	}
}
