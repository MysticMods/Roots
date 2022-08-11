package epicsquid.roots.integration.crafttweaker.tweaks.predicates;

import crafttweaker.annotations.ZenRegister;
import epicsquid.roots.Roots;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods." + Roots.MODID + ".predicates.BlockStateAbove")
@ZenDocAppend({"docs/include/transmutation.blockstateabove.example.md"})
@ZenClass("mods." + Roots.MODID + ".predicates.BlockStateAbove")
@ZenRegister
public class BlockStateAbove implements Predicates.IWorldPredicate {
	private Predicates.IPredicate predicate;
	
	public BlockStateAbove(Predicates.IPredicate predicate) {
		this.predicate = predicate;
	}
	
	@ZenMethod
	@ZenDocMethod(
			order = 1,
			args = {
					@ZenDocArg(arg = "predicate", info = "a defined predicate that describes (potentially) multiple blockstates")
			},
			description = "Creates an IWorldCondition which tests the state above a block using the provided IPredicate."
	)
	public static BlockStateAbove create(Predicates.IPredicate predicate) {
		return new BlockStateAbove(predicate);
	}
	
	@Override
	public epicsquid.roots.recipe.transmutation.BlockStateAbove get() {
		return new epicsquid.roots.recipe.transmutation.BlockStateAbove(this.predicate.get());
	}
}
