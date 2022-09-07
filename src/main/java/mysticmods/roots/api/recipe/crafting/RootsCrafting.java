package mysticmods.roots.api.recipe.crafting;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.IItemHandler;
import noobanidus.libs.noobutil.inventory.IInvWrapper;

import javax.annotation.Nullable;

public abstract class RootsCrafting<H extends IItemHandler> extends IInvWrapper<H> implements IRootsCrafting<H> {
  protected Player player;

  public RootsCrafting(H handler, @Nullable Player player) {
    super(handler);
    this.player = player;
  }

  @Nullable
  @Override
  public Player getPlayer() {
    return player;
  }

  @Override
  public H getHandler() {
    return super.getHandler();
  }
}
