package mysticmods.roots.api.recipe;

import net.minecraft.item.crafting.RecipeManager;

import javax.annotation.Nullable;

// TODO: Move to NoobUtil
public interface IRecipeManagerAccessor {
  @Nullable
  RecipeManager getManager();
}
