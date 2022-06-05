package mysticmods.roots.api.ritual;

import mysticmods.roots.recipe.pyre.PyreRecipe;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IRitual extends IForgeRegistryEntry<IRitual> {
  // TODO: getting/setting recipe, properties
  PyreRecipe getRecipe();
}
