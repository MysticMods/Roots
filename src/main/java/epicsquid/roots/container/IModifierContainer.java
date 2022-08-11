package epicsquid.roots.container;

public interface IModifierContainer {
	void setModifierStatus(int modifier, boolean status);
	
	boolean getModifierStatus(int modifier);
	
	default boolean isShiftDown() {
		return getModifierStatus(0);
	}
	
	default boolean isControlDown() {
		return getModifierStatus(1);
	}
	
	default boolean isAltDown() {
		return getModifierStatus(2);
	}
}
