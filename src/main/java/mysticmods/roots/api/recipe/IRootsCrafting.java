package mysticmods.roots.api.recipe;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import noobanidus.libs.noobutil.inventory.IIInvWrapper;

import javax.annotation.Nullable;

public interface IRootsCrafting<H extends IItemHandler> extends IInventory, IIInvWrapper<H> {
  @Nullable
  default World getLevel() {
    PlayerEntity player = getPlayer();
    if (player == null) {
      return null;
    }

    return player.level;
  }

  @Nullable
  PlayerEntity getPlayer();

  @Nullable
  default PlayerInventory getPlayerInventory() {
    PlayerEntity player = getPlayer();
    if (player == null) {
      return null;
    }

    return player.inventory;
  }

  @Override
  H getHandler();
}
