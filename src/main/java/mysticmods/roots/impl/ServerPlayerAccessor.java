package mysticmods.roots.impl;

import mysticmods.roots.api.IPlayerAccessor;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class ServerPlayerAccessor implements IPlayerAccessor {
  @Nullable
  @Override
  public Player getPlayer() {
    return null;
  }
}
