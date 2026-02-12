package mysticmods.roots.inventory.fake;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public abstract class FakeContainer extends AbstractContainerMenu {
  protected FakeContainer(@Nullable MenuType<?> menuType, int containerId) {
    super(menuType, containerId);
  }

  @Override
  public ItemStack quickMoveStack(Player player, int index) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean stillValid(Player player) {
    return stillValid(getAccess(), player, getBlock());
  }

  protected abstract ContainerLevelAccess getAccess();

  protected abstract Block getBlock();

  public abstract boolean hasRecipe();
}
