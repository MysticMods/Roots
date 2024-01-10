package epicsquid.roots.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.*;
import java.util.function.Predicate;

public class StateUtil {
	public static StateMatcher ANY = new StateMatcher("any");
	public static StateMatcher AIR = new StateMatcher("air");
	
	public static class StateMatcher implements Predicate<IBlockState> {
		private final String initial;
		private boolean invalid = false;
		private IBlockState state = null;
		
		private Predicate<IBlockState> tester = null;
		private final Predicate<IBlockState> air = (s) -> s.getBlock() == Blocks.AIR;
		private final Predicate<IBlockState> any = (s) -> true;
		
		private Set<IProperty<?>> pairList = null;
		
		public IBlockState getState() {
			if (state == null) {
				state = calculateState();
			}
			return state;
		}
		
		public StateMatcher(String initial) {
			this.initial = initial.trim().toLowerCase(Locale.ROOT);
		}
		
		@Override
		public boolean test(IBlockState state) {
			if (invalid) {
				return false;
			}
			if (this.tester == null) {
				this.tester = calculateTester();
			}
			if (this.tester == null) {
				invalid = true;
				return false;
			}
			return this.tester.test(state);
		}
		
		private <T extends Comparable<T>> IBlockState findProperty(IBlockState baseState, IProperty<T> property, String keyName, String newValue) {
			if (keyName.equals(property.getName())) {
				for (T value : property.getAllowedValues()) {
					if (property.getName(value).equals(newValue)) {
						pairList.add(property);
						return baseState.withProperty(property, value);
					}
				}
			}
			return null;
		}
		
		private Predicate<IBlockState> calculateTester() {
			if (initial.isEmpty()) {
				return null;
			}
			
			if (initial.equals("air")) {
				return air;
			} else if (initial.equals("any")) {
				return any;
			}
			
			this.state = calculateState();
			if (this.state == null) {
				return null;
			}
			
			return new Matcher(this.pairList, this.state);
		}
		
		private IBlockState calculateState() {
			pairList = new HashSet<>();
			
			String[] split = initial.split("\\[");
			Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(split[0]));
			if (block != null) {
				IBlockState startingState = block.getDefaultState();
				if (split.length > 1) {
					String[] split2 = split[1].replace("]", "").split(",");
					for (String part : split2) {
						String[] pair = part.split("=");
						if (pair.length != 2) {
							continue;
						}
						for (IProperty<?> property : startingState.getPropertyKeys()) {
							IBlockState potential = findProperty(startingState, property, pair[0], pair[1]);
							if (potential != null) {
								startingState = potential;
							}
						}
					}
				}
				return startingState;
			} else {
				return null;
			}
		}
		
		private static class Matcher implements Predicate<IBlockState> {
			private final Set<IProperty<?>> props;
			private final IBlockState state;
			
			public Matcher(Set<IProperty<?>> props, IBlockState state) {
				this.props = props;
				this.state = state;
			}
			
			@Override
			public boolean test(IBlockState state) {
				if (this.state.getBlock() != state.getBlock()) {
					return false;
				}
				
				Set<IProperty<?>> incomingStates = state.getProperties().keySet();
				for (IProperty<?> mine : this.props) {
					if (!incomingStates.contains(mine)) {
						return false;
					}
					
					if (!this.state.getValue(mine).equals(state.getValue(mine))) {
						return false;
					}
				}
				
				return true;
			}
		}
	}
	
	public static Map<Block, List<IProperty<?>>> skipProperties = new HashMap<>();
	
	public static void ignoreState(Block state, IProperty<?> property) {
		List<IProperty<?>> thisProperty = skipProperties.computeIfAbsent(state, (block) -> new ArrayList<>());
		thisProperty.add(property);
	}
	
	/**
	 * @param state1 Reference state
	 * @param state2 State we're comparing to
	 * @return Returns true if the states are of the same block, and state2 contains
	 * all of the same property keys as state2, and that the values of these
	 * properties match.
	 */
	public static boolean compareStates(IBlockState state1, IBlockState state2) {
		// This should cover most options
		if (state1.getBlock() != state2.getBlock()) return false;
		
		ImmutableMap<IProperty<?>, Comparable<?>> state1_props = state1.getProperties();
		ImmutableMap<IProperty<?>, Comparable<?>> state2_props = state2.getProperties();
		
		if (state1_props.size() < state2_props.size()) return false;
		
		List<IProperty<?>> skips = skipProperties.computeIfAbsent(state1.getBlock(), (block) -> new ArrayList<>());
		
		for (Map.Entry<IProperty<?>, Comparable<?>> entry : state1_props.entrySet()) {
			if (skips.contains(entry.getKey())) continue;
			
			if (!state2_props.containsKey(entry.getKey())) return false;
			
			Comparable<?> comp2 = state2_props.get(entry.getKey());
			
			if (entry.getKey().getValueClass().cast(entry.getValue()) != entry.getKey().getValueClass().cast(comp2))
				return false;
		}
		
		return true;
	}
	
	/**
	 * @return true if all of the state's values contained in stateWithValues are present and are equal in the stateBeingChecked, false otherwise.
	 */
	public static boolean stateContainsValues(IBlockState stateBeingChecked, IBlockState stateWithValues) {
		// If they aren't the same block then they aren't the same state
		if (stateBeingChecked.getBlock() != stateWithValues.getBlock()) return false;
		
		Collection<IProperty<?>> keys = stateBeingChecked.getPropertyKeys();
		
		// If stateWithValues has no keys then this is a noop
		for (IProperty<?> prop : stateWithValues.getPropertyKeys()) {
			if (!keys.contains(prop)) {
				return false;
			}
			
			if (stateBeingChecked.getValue(prop) != stateWithValues.getValue(prop)) {
				return false;
			}
		}
		
		return true;
	}
}
