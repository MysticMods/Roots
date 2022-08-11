package epicsquid.roots.modifiers;

import epicsquid.roots.api.Herb;

import java.util.Locale;

public interface IModifierCost {
	CostType getCost();
	
	double getValue();
	
	Herb getHerb();
	
	default String asPropertyName() {
		Herb herb = getHerb();
		if (herb == null) {
			return "no_herb_" + getCost().toString().toLowerCase(Locale.ROOT);
		} else {
			return herb.getName() + "_" + getCost().toString().toLowerCase(Locale.ROOT);
		}
	}
}
