package mysticmods.roots.impl;

import mysticmods.roots.api.recipe.IRecipeManagerAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;

public class ServerRecipeAccessor implements IRecipeManagerAccessor {
  @Nullable
  @Override
  public RecipeManager getManager() {
    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    if (server == null) {
      return null;
    }

    return server.getRecipeManager();
  }
}
