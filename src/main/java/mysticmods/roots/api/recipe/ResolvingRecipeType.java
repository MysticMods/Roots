package mysticmods.roots.api.recipe;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

// TODO: Move recipe caching into NoobUtil
public class ResolvingRecipeType<C extends IInventory, T extends IRecipe<C>> extends noobanidus.libs.noobutil.recipe.ResolvingRecipeType<C, T> {
  private T lastRecipe = null;

  public ResolvingRecipeType(Supplier<IRecipeType<T>> type, Comparator<? super T> comparator) {
    super(type, comparator);
  }

  @Override
  protected List<T> getRecipesList() {
    return RootsAPI.getInstance().getRecipeManager().getAllRecipesFor(type.get());
  }

  @Nullable
  public T findRecipe(C inventory, World level) {
    if (lastRecipe != null && lastRecipe.matches(inventory, level)) {
      return lastRecipe;
    }
    for (T recipe : getRecipes()) {
      if (recipe.matches(inventory, level)) {
        lastRecipe = recipe;
        return recipe;
      }
    }

    return null;
  }
}
