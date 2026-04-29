package mysticmods.roots.inventory.fake;

import mysticmods.roots.blockentity.FungalTransmuterBlockEntity;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModContainers;
import mysticmods.roots.inventory.SlotSingleItem;
import mysticmods.roots.recipe.transmutation.TransmutationInventory;
import mysticmods.roots.util.PlayerGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class TransmuterContainer extends FakeContainer {
  private final ContainerLevelAccess access;

  public TransmuterContainer(int containerId, Inventory inventory, @Nullable RegistryFriendlyByteBuf buffer) {
    this(containerId, inventory, new TransmutationInventory(), PlayerGetter.getLevelAccess(buffer));
  }

  public TransmuterContainer(int containerId, Inventory playerInventory, TransmutationInventory inventory, ContainerLevelAccess access) {
    super(ModContainers.TRANSMUTER.get(), containerId);
    this.access = access;

    // 25 by default
    for (int i = 0; i < inventory.getSlots(); i++) {
      this.addSlot(new SlotSingleItem(inventory, 1, i, 20 * i, 0) {
        @Override
        public boolean isActive() {
          return false;
        }
      });
    }

    for (int i1 = 0; i1 < 3; ++i1) {
      for (int k1 = 0; k1 < 9; ++k1) {
        this.addSlot(new Slot(playerInventory, k1 + i1 * 9, 8 + k1 * 18, 96 + i1 * 18) {
          @Override
          public boolean isActive() {
            return false;
          }
        });
      }
    }

    for (int j1 = 0; j1 < 9; ++j1) {
      this.addSlot(new Slot(playerInventory, j1, 8 + j1 * 18, 154) {
        @Override
        public boolean isActive() {
          return false;
        }
      });
    }
  }


  @Override
  protected ContainerLevelAccess getAccess() {
    return access;
  }

  @Override
  protected Block getBlock() {
    return ModBlocks.FUNGAL_TRANSMUTER.get();
  }

  @Override
  public boolean hasRecipe() {
    if (access.evaluate(Level::getBlockEntity).orElse(null) instanceof FungalTransmuterBlockEntity crafter) {
      return crafter.getCachedRecipe() != null;
    }

    return false;
  }
}
