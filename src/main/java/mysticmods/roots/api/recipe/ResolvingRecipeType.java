package mysticmods.roots.api.recipe;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

public class ResolvingRecipeType<C extends IInventory, T extends IRecipe<C>> extends noobanidus.libs.noobutil.recipe.ResolvingRecipeType<C, T> {

  public ResolvingRecipeType(Supplier<IRecipeType<T>> type, Comparator<? super T> comparator) {
    super(type, comparator);
  }

  @Override
  protected List<T> getRecipesList() {
    return RootsAPI.getInstance().getRecipeManager().getAllRecipesFor(type.get());
  }
}
