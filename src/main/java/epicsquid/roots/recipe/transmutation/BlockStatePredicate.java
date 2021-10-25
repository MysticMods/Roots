package epicsquid.roots.recipe.transmutation;

import net.minecraft.block.BlockState;

@FunctionalInterface
public interface BlockStatePredicate extends MatchingStates {
  BlockStatePredicate TRUE = (o) -> true;

  boolean test(BlockState state);
}
