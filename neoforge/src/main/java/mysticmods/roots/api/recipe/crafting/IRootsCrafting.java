package mysticmods.roots.api.recipe.crafting;


import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public interface IRootsCrafting<H extends IItemHandler> extends RecipeInput {
  ItemStackHandler NULL_HANDLER = new ItemStackHandler(1);

  H getHandler();

  @Nullable
  default Level getLevel() {
    Player player = getPlayer();
    if (player == null) {
      return null;
    }

    return player.level();
  }

  @Nullable
  Player getPlayer();

  @Nullable
  default Inventory getPlayerInventory() {
    Player player = getPlayer();
    if (player == null) {
      return null;
    }

    return player.getInventory();
  }
}
