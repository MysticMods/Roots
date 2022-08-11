package epicsquid.roots.properties;

public class NoDefaultProperty<T> extends Property<T> {
	public NoDefaultProperty(String name, Class<?> clazz) {
		super(name, clazz);
	}
	
	@Override
	public boolean hasDefaultValue() {
		return false;
	}
}
