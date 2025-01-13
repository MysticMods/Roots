package mysticmods.roots.api.recipe.crafting;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nullable;

public abstract class RootsCrafting<H extends IItemHandler> implements IRootsCrafting<H> {
  protected Player player;
  protected H handler;

  public RootsCrafting(H handler, @Nullable Player player) {
    this.handler = handler;
    this.player = player;
  }

  @Nullable
  @Override
  public Player getPlayer() {
    return player;
  }

  @Override
  public H getHandler() {
    return this.handler;
  }
}
