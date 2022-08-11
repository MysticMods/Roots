package epicsquid.roots.container.slots;

import epicsquid.roots.spell.info.LibrarySpellInfo;

import javax.annotation.Nullable;

public interface ILibrarySlot {
	
	@Nullable
	LibrarySpellInfo getInfo();
}
