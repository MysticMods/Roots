package mysticmods.roots.impl;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.server.ServerLifecycleHooks;


import javax.annotation.Nullable;

public class ServerAccessor {
  private static RecipeManager manager = null;

  @Nullable
  public static RecipeManager getManager() {
    if (manager != null) {
      return manager;
    }
    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    if (server == null) {
      return null;
    }

    manager = server.getRecipeManager();
    return manager;
  }
}
