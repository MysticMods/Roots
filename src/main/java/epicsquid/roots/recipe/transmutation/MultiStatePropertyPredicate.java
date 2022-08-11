package epicsquid.roots.recipe.transmutation;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MultiStatePropertyPredicate extends MultiStatePredicate {
	protected List<IProperty<?>> props;
	
	public MultiStatePropertyPredicate(IProperty<?> prop, IBlockState... states) {
		super(states);
		this.props = Collections.singletonList(prop);
	}
	
	public MultiStatePropertyPredicate(List<IProperty<?>> props, IBlockState... states) {
		super(states);
		this.props = props;
	}
	
	@Override
	public boolean test(IBlockState incState) {
		Collection<IProperty<?>> incoming = incState.getPropertyKeys();
		for (IBlockState state : this.states) {
			Collection<IProperty<?>> current = state.getPropertyKeys();
			if (super.test(incState) && props.stream().allMatch(prop -> incoming.contains(prop) && current.contains(prop) && state.getValue(prop).equals(incState.getValue(prop)))) {
				return true;
			}
		}
		
		return false;
	}
}
