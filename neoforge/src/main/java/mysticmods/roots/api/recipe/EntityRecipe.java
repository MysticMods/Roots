package mysticmods.roots.api.recipe;

import mysticmods.roots.api.recipe.crafting.IEntityCrafting;
import mysticmods.roots.api.test.entity.EntityTest;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;

public abstract class EntityRecipe<W extends IEntityCrafting> extends RootsRecipe<ItemStackHandler, W> implements IEntityRecipe<W> {
  protected EntityTest test;

  public EntityRecipe(ResourceLocation recipeId) {
  }

  @Override
  public void setEntityTest(EntityTest test) {
    this.test = test;
  }

  @Override
  public EntityTest getEntityTest() {
    return test;
  }

  @Override
  public boolean matches(W pContainer, Level pLevel) {
    return test.test(pContainer.getEntity());
  }

  // Wot's this
  public void modifyEntity(W pContainer) {
  }
}
