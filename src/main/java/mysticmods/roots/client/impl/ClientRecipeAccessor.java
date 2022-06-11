package mysticmods.roots.client.impl;

import mysticmods.roots.api.recipe.IRecipeManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;

public class ClientRecipeAccessor implements IRecipeManagerAccessor {
  private RecipeManager manager = null;

  @Nullable
  @Override
  public RecipeManager getManager() {
    if (manager != null) {
      return manager;
    }

    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    if (server != null) {
      manager = server.getRecipeManager();
      return manager;
    }

    ClientPacketListener connection = Minecraft.getInstance().getConnection();
    if (connection == null) {
      return null;
    }

    manager = connection.getRecipeManager();
    return manager;
  }
}
