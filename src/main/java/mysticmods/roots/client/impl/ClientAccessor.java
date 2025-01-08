package mysticmods.roots.client.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.Nullable;

public class ClientAccessor {
  private static RecipeManager manager = null;

  public static boolean isShiftKeyDown() {
    return Screen.hasShiftDown();
  }

  @Nullable
  public static RecipeManager getManager() {
    if (manager != null) {
      return manager;
    }

/*    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    if (server != null) {
      manager = server.getRecipeManager();
      return manager;
    }*/

    ClientPacketListener connection = Minecraft.getInstance().getConnection();
    if (connection == null) {
      return null;
    }

    manager = connection.getRecipeManager();
    return manager;
  }

  @Nullable
  public static Player getPlayer() {
    Minecraft mc = Minecraft.getInstance();
    if (mc == null) {
      return null;
    }

    return mc.player;
  }
}
