package mysticmods.roots.api.recipe;

import mysticmods.roots.api.recipe.crafting.IWorldCrafting;
import mysticmods.roots.api.test.world.PartialBlockState;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IWorldRecipe<W extends IWorldCrafting> extends IRootsRecipe<W> {
  PartialBlockState getOutputState ();
  WorldCondition getCondition ();
  List<String> getSkipProperties ();

  @Nullable
  BlockState modifyState (W container, BlockState state, HolderLookup.Provider provider);
}
