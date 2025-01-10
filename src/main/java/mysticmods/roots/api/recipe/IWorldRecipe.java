package mysticmods.roots.api.recipe;

import mysticmods.roots.api.recipe.crafting.IWorldCrafting;
import mysticmods.roots.api.world.PartialBlockState;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface IWorldRecipe<W extends IWorldCrafting> extends IRootsRecipe<W> {
  void setOutputState (PartialBlockState state);
  PartialBlockState getOutputState ();

  WorldCondition getCondition ();
  void setCondition(WorldCondition condition);

  @Nullable
  BlockState modifyState (W container, BlockState state, HolderLookup.Provider provider);
}
