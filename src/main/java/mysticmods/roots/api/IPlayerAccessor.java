package mysticmods.roots.api;

import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public interface IPlayerAccessor {
  @Nullable
  Player getPlayer ();
}
