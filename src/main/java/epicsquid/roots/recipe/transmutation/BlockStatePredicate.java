package epicsquid.roots.recipe.transmutation;

import net.minecraft.block.state.IBlockState;

@FunctionalInterface
public interface BlockStatePredicate extends MatchingStates {
    BlockStatePredicate TRUE = (o) -> true;

    boolean test(IBlockState state);
}
