package mysticmods.roots.api.recipe;

import mysticmods.roots.api.recipe.crafting.IEntityCrafting;
import mysticmods.roots.api.test.entity.EntityTest;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;

public abstract class EntityRecipe<W extends IEntityCrafting> extends RootsRecipe<ItemStackHandler, W> implements IEntityRecipe<W> {
  protected EntityTest test;

  public EntityRecipe(BaseRecipeData data, EntityTest test) {
    super(data);
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

  @Override
  public ItemStack assemble(W arg, HolderLookup.Provider arg2) {
    modifyEntity(arg, arg2);
    return super.assemble(arg, arg2);
  }

  @Override
  public abstract void modifyEntity(W pContainer, HolderLookup.Provider provider);
}
