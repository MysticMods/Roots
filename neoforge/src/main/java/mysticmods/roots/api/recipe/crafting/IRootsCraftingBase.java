package mysticmods.roots.api.recipe.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public interface IRootsCraftingBase extends Container {
  @Nullable
  default Level getLevel() {
    Player player = getPlayer();
    if (player == null) {
      return null;
    }

    return player.level;
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
