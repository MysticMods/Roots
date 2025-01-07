package mysticmods.roots.api.recipe;

import mysticmods.roots.api.recipe.crafting.IEntityCrafting;
import mysticmods.roots.api.test.entity.EntityTest;
import net.minecraft.core.HolderLookup;

public interface IEntityRecipe<W extends IEntityCrafting> extends IRootsRecipe<W> {
  void setEntityTest(EntityTest test);

  EntityTest getEntityTest();

  void modifyEntity (W container, HolderLookup.Provider provider);
}
