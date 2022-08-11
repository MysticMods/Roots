package epicsquid.roots.modifiers;

import epicsquid.roots.api.Herb;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class Cost implements IModifierCost {
	private final CostType cost;
	private final double value;
	private final IModifierCore herb;
	
	public Cost(CostType cost, double value, IModifierCore herb) {
		this.cost = cost;
		this.value = value;
		this.herb = herb;
		if (cost == CostType.ADDITIONAL_COST && herb == null) {
			throw new IllegalArgumentException("Modifier cannot be additional cost without a herb specified.");
		}
		if (cost != CostType.NO_COST && value == 0) {
			throw new IllegalArgumentException("Modifier cannot be a cost modifier or an additional cost with a value single zero.");
		}
	}
	
	public Cost(CostType cost, double value) {
		this(cost, value, null);
	}
	
	public Cost(CostType cost) {
		this(cost, 0, null);
	}
	
	@Override
	public CostType getCost() {
		return cost;
	}
	
	@Override
	public double getValue() {
		return value;
	}
	
	@Override
	@Nullable
	public Herb getHerb() {
		if (herb != null) {
			return herb.getHerb();
		} else {
			return null;
		}
	}
	
	public static Map<CostType, IModifierCost> single(CostType cost, IModifierCore herb, double value) {
		Map<CostType, IModifierCost> map = new HashMap<>();
		map.put(cost, new Cost(cost, value, herb));
		return map;
	}
	
	public static Map<CostType, IModifierCost> single(CostType cost, double value) {
		return single(cost, null, value);
	}
	
	public static Map<CostType, IModifierCost> single(CostType cost) {
		return single(cost, null, 0);
	}
	
	public static IModifierCost cost(CostType cost, IModifierCore herb, double value) {
		return new Cost(cost, value, herb);
	}
	
	public static IModifierCost cost(CostType cost, double value) {
		return cost(cost, null, value);
	}
	
	public static IModifierCost cost(CostType cost) {
		return cost(cost, null, 0);
	}
	
	public static Map<CostType, IModifierCost> of(IModifierCost... costs) {
		Map<CostType, IModifierCost> map = new HashMap<>();
		for (IModifierCost cost : costs) {
			map.put(cost.getCost(), cost);
		}
		return map;
	}
	
	public static Map<CostType, IModifierCost> noCost() {
		return single(CostType.NO_COST);
	}
}
