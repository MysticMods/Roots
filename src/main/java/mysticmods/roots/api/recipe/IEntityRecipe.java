package mysticmods.roots.api.recipe;

import mysticmods.roots.api.recipe.crafting.IEntityCrafting;
import mysticmods.roots.api.recipe.crafting.IRootsCrafting;
import mysticmods.roots.api.test.entity.EntityTest;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;

public interface IEntityRecipe<W extends IEntityCrafting> extends IRootsRecipe<W> {
  EntityTest getEntityTest();

  @Override
  default boolean matchesIngredients(RecipeInput arg, Level arg2) {
    return false;
  }

  void modifyEntity(W container, HolderLookup.Provider provider);
}
