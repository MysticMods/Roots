package mysticmods.roots.impl;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.capability.Unlock;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeManager;

public class RootsAPIImpl extends RootsAPI {
  @Override
  public void unlock(ServerPlayer player, Unlock<?> unlock) {

  }

  @Override
  public boolean canUnlock(ServerPlayer player, Unlock<?> unlock) {
    return false;
  }

  @Override
  public Player getPlayer() {
    return Accessor.getPlayer();
  }

  @Override
  public boolean isShiftKeyDown() {
    return Accessor.isShiftKeyDown();
  }

  @Override
  public RecipeManager getRecipeManager() {
    return Accessor.getManager();
  }
}
