package mysticmods.roots.api.recipe;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import noobanidus.libs.noobutil.inventory.IInvWrapper;

import javax.annotation.Nullable;

public abstract class RootsCrafting<H extends IItemHandler> extends IInvWrapper<H> implements IRootsCrafting <H>{
  protected PlayerEntity player;

  public RootsCrafting(H handler, @Nullable PlayerEntity player) {
    super(handler);
  }

  @Nullable
  @Override
  public PlayerEntity getPlayer() {
    return player;
  }

  @Override
  public H getHandler() {
    return super.getHandler();
  }
}
