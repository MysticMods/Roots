package mysticmods.roots.api.recipe;

import mysticmods.roots.api.recipe.crafting.IEntityCrafting;
import mysticmods.roots.api.test.entity.EntityTest;

public interface IEntityRecipe<W extends IEntityCrafting> extends IBoundlessRecipe<W> {
  void setEntityTest (EntityTest test);
  EntityTest getEntityTest ();
}
