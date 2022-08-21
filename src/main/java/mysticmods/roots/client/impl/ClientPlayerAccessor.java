package mysticmods.roots.client.impl;

import mysticmods.roots.api.IPlayerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class ClientPlayerAccessor implements IPlayerAccessor {
  @Nullable
  @Override
  public Player getPlayer() {
    Minecraft mc = Minecraft.getInstance();
    if (mc == null) {
      return null;
    }

    return mc.player;
  }
}
