package mysticmods.roots.api.access;

import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.Nullable;

// TODO: Move to NoobUtil
public interface IRecipeManagerAccessor {
  @Nullable
  RecipeManager getManager();
}
