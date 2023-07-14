package mysticmods.roots.api.recipe.type;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.recipe.IRootsRecipeBase;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

// TODO: Move recipe caching into NoobUtil
public class ResolvingRecipeType<C extends Container, T extends Recipe<C> & IRootsRecipeBase> extends noobanidus.libs.noobutil.recipe.ResolvingRecipeType<C, T> {
  private T lastRecipe = null;

  public ResolvingRecipeType(Supplier<RecipeType<T>> type, Comparator<? super T> comparator) {
    super(type, comparator);
  }

  @Override
  protected List<T> getRecipesList() {
    RecipeManager manager = RootsAPI.getInstance().getRecipeManager();
    if (manager == null) {
      // TODO:
      return Collections.emptyList();
    }
    return manager.getAllRecipesFor(type.get());
  }

  @Nullable
  public T findRecipe(C inventory, Level level) {
    if (lastRecipe != null && !lastRecipe.isDynamic() && lastRecipe.matches(inventory, level)) {
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
