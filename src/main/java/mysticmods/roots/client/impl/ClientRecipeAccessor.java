package mysticmods.roots.client.impl;

import mysticmods.roots.api.recipe.IRecipeManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.Nullable;

public class ClientRecipeAccessor implements IRecipeManagerAccessor {
  @Nullable
  @Override
  public RecipeManager getManager() {
    ClientPacketListener connection = Minecraft.getInstance().getConnection();
    if (connection == null) {
      return null;
    }

    return connection.getRecipeManager();
  }
}
