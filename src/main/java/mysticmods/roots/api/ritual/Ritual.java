package mysticmods.roots.api.ritual;

import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class Ritual extends ForgeRegistryEntry<IRitual> implements IRitual {
  @Override
  @Nullable
  public PyreRecipe getRecipe() {
    return ResolvedRecipes.PYRE.getRecipe(getRegistryName());
  }
}
