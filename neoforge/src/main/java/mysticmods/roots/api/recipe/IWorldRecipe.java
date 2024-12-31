package mysticmods.roots.api.recipe;

import mysticmods.roots.api.recipe.crafting.IWorldCrafting;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.state.BlockState;

public interface IWorldRecipe<W extends IWorldCrafting> extends IRootsRecipe<W> {
  void setOutputState (BlockState state);
  BlockState getOutputState ();

  WorldCondition getCondition ();
  void setCondition(WorldCondition condition);

  BlockState modifyState (W container, BlockState state, HolderLookup.Provider provider);
}
