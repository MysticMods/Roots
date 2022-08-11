package epicsquid.roots.modifiers;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface IModifierList<T extends IModifier, V extends NBTBase> extends Iterable<T>, INBTSerializable<V> {
	void clear();
	
	int size();
	
	Stream<T> stream();
	
	boolean isEmpty();
	
	boolean contains(Object o);
	
	@Nullable
	T getByCore(IModifierCore core);
	
	@Nullable
	T get(IModifier modifier);
	
	Collection<T> getModifiers();
	
	boolean add(T modifierInstance);
	
	boolean remove(Object o);
	
	default boolean removeIf(Predicate<? super T> predicate) {
		throw new UnsupportedOperationException("this method isn't implemented for this class");
	}
}
