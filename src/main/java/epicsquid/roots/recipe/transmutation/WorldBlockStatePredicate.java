package epicsquid.roots.recipe.transmutation;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@FunctionalInterface
public interface WorldBlockStatePredicate extends MatchingStates {
	WorldBlockStatePredicate TRUE = (a, b, c) -> true;
	
	default StatePosition getPosition() {
		return StatePosition.NULL;
	}
	
	boolean test(IBlockState state, World world, BlockPos pos);
}
