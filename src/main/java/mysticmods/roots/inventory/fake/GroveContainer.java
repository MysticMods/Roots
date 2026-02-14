package mysticmods.roots.inventory.fake;

import mysticmods.roots.blockentity.GroveCrafterBlockEntity;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModContainers;
import mysticmods.roots.recipe.grove.GroveCrafting;
import mysticmods.roots.util.PlayerGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GroveContainer extends FakeContainer {
  private final ContainerLevelAccess access;
  private final GroveCrafting crafting;

  private final List<Slot> recipeSlots = new ArrayList<>();
  private final List<Slot> inventorySlots = new ArrayList<>();

  public GroveContainer(int containerId, Inventory inventory, @Nullable RegistryFriendlyByteBuf buffer) {
    this(containerId, inventory, null, PlayerGetter.getLevelAccess(buffer));
  }

  public GroveContainer(int containerId, Inventory inventory, @Nullable GroveCrafting crafting, ContainerLevelAccess access) {
    super(ModContainers.GROVE.get(), containerId);
    this.access = access;
    if (crafting == null) {
      if (access.evaluate(Level::getBlockEntity).orElse(null) instanceof GroveCrafterBlockEntity crafter) {
        this.crafting = crafter.getCrafting(inventory.player, true);
      } else {
        throw new IllegalStateException("GroveContainer created without crafting and block entity is not a GroveCrafterBlockEntity");
      }
    } else {
      this.crafting = crafting;
    }

    assert crafting != null;

    for (int i1 = 0; i1 < 3; ++i1) {
      for (int k1 = 0; k1 < 9; ++k1) {
        inventorySlots.add(this.addSlot(new Slot(inventory, k1 + i1 * 9, 8 + k1 * 18, 96 + i1 * 18) {
          @Override
          public boolean isActive() {
            return false;
          }
        }));
      }
    }

    for (int j1 = 0; j1 < 9; ++j1) {
      inventorySlots.add(this.addSlot(new Slot(inventory, j1, 8 + j1 * 18, 154) {
        @Override
        public boolean isActive() {
          return false;
        }
      }));
    }

    var handler = this.crafting.getHandler();

    for (int i = 0; i < handler.getSlots(); i++) {
      recipeSlots.add(this.addSlot(new SlotItemHandler(handler, i, 20 * i, 0) {
        @Override
        public boolean isActive() {
          return false;
        }
      }));
    }
  }


  @Override
  protected ContainerLevelAccess getAccess() {
    return access;
  }

  @Override
  protected Block getBlock() {
    return ModBlocks.GROVE_CRAFTER.get();
  }

  @Override
  public boolean hasRecipe() {
    if (access.evaluate(Level::getBlockEntity).orElse(null) instanceof GroveCrafterBlockEntity crafter) {
      return crafter.getRecipe() != null;
    }

    return false;
  }

  public List<Slot> recipeSlots() {
    return recipeSlots;
  }

  public List<Slot> inventorySlots() {
    return inventorySlots;
  }
}
