package mysticmods.roots.client.impl;

import mysticmods.roots.api.recipe.IRecipeManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.item.crafting.RecipeManager;

import javax.annotation.Nullable;

public class ClientRecipeAccessor implements IRecipeManagerAccessor {
  @Nullable
  @Override
  public RecipeManager getManager() {
    ClientPlayNetHandler connection = Minecraft.getInstance().getConnection();
    if (connection == null) {
      return null;
    }

    return connection.getRecipeManager();
  }
}
