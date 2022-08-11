package epicsquid.roots.item;

import javax.annotation.Nullable;

public enum PouchType {
	HERB, COMPONENT, APOTHECARY, FEY, CREATIVE;
	
	public static final int COMPONENT_POUCH_HERB_SLOTS = 6;
	public static final int COMPONENT_POUCH_INVENTORY_SLOTS = 12;
	public static final int APOTHECARY_POUCH_HERB_SLOTS = 9;
	public static final int APOTHECARY_POUCH_INVENTORY_SLOTS = 18;
	public static final int HERB_POUCH_SLOTS = 9;
	public static final int FEY_POUCH_INVENTORY_SLOTS = 6;
	public static final int FEY_POUCH_HERB_SLOTS = 12;
	
	public int herbSlots() {
		switch (this) {
			case COMPONENT:
				return COMPONENT_POUCH_HERB_SLOTS;
			case APOTHECARY:
				return APOTHECARY_POUCH_HERB_SLOTS;
			case HERB:
				return HERB_POUCH_SLOTS;
			case FEY:
				return FEY_POUCH_HERB_SLOTS;
			default:
				return 0;
		}
	}
	
	public int inventorySlots() {
		switch (this) {
			case COMPONENT:
				return COMPONENT_POUCH_INVENTORY_SLOTS;
			case APOTHECARY:
				return APOTHECARY_POUCH_INVENTORY_SLOTS;
			case FEY:
				return FEY_POUCH_INVENTORY_SLOTS;
			default:
				return 0;
		}
	}
	
	@Nullable
	public static PouchType fromOrdinal(int ordinal) {
		int i = 0;
		for (PouchType type : values()) {
			if (i == ordinal) {
				return type;
			}
			i++;
		}
		return null;
	}
}
