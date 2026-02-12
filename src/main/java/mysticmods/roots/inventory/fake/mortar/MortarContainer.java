package mysticmods.roots.inventory.fake.mortar;

import mysticmods.roots.blockentity.MortarBlockEntity;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModContainers;
import mysticmods.roots.recipe.mortar.MortarInventory;
import mysticmods.roots.util.PlayerGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class MortarContainer extends AbstractContainerMenu {
  private final ContainerLevelAccess access;

  public MortarContainer(int containerId, Inventory inventory, @Nullable RegistryFriendlyByteBuf buffer) {
    this(containerId, inventory, new MortarInventory(), PlayerGetter.getLevelAccess(buffer));
  }

  public MortarContainer(int containerId, Inventory inventory, MortarInventory mortarInventory, ContainerLevelAccess access) {
    super(ModContainers.MORTAR.get(), containerId);
    this.access = access;

    for (int i = 0; i < mortarInventory.getSlots(); i++) {
      this.addSlot(new SlotItemHandler(mortarInventory, i, 20 * i, 0) {
        @Override
        public boolean isActive() {
          return false;
        }
      });
    }

    for (int i1 = 0; i1 < 3; ++i1) {
      for (int k1 = 0; k1 < 9; ++k1) {
        this.addSlot(new Slot(inventory, k1 + i1 * 9, 8 + k1 * 18, 96 + i1 * 18) {
          @Override
          public boolean isActive() {
            return false;
          }
        });
      }
    }

    for (int j1 = 0; j1 < 9; ++j1) {
      this.addSlot(new Slot(inventory, j1, 8 + j1 * 18, 154) {
        @Override
        public boolean isActive() {
          return false;
        }
      });
    }
  }

  @Override
  public ItemStack quickMoveStack(Player player, int index) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean stillValid(Player player) {
    return stillValid(this.access, player, ModBlocks.MORTAR.get());
  }

  public boolean hasRecipe () {
    if (access.evaluate(Level::getBlockEntity).orElse(null) instanceof MortarBlockEntity mortar) {
      return mortar.getCachedRecipe() != null;
    }

    return false;
  }
}
