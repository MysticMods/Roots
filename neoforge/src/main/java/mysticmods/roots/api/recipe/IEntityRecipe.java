package mysticmods.roots.api.recipe;

import mysticmods.roots.api.recipe.crafting.IEntityCrafting;
import mysticmods.roots.api.test.entity.EntityTest;

public interface IEntityRecipe<W extends IEntityCrafting> extends IRootsRecipe<W> {
  void setEntityTest(EntityTest test);

  EntityTest getEntityTest();

}
