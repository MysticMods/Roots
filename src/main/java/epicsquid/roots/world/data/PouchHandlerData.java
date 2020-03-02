package epicsquid.roots.world.data;

import epicsquid.roots.handler.PouchHandler;
import epicsquid.roots.init.HerbRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.UUID;

@SuppressWarnings({"WeakerAccess", "NullableProblems"})
public class PouchHandlerData extends WorldSavedData {
  private static final String identifier = "Inventory-";

  private UUID uuid;
  private MarkDirtyHandler inventoryHandler;
  private HerbHandler herbHandler;

  public static String name(UUID uuid) {
    return identifier + uuid.toString();
  }

  public PouchHandlerData(String identifier) {
    super(identifier);
    this.uuid = UUID.fromString(identifier.replace(PouchHandlerData.identifier, ""));
    createHandler();
  }

  public PouchHandlerData(UUID uuid) {
    super(name(uuid));
    this.uuid = uuid;
    createHandler();
  }

  private void createHandler() {
    this.inventoryHandler = new MarkDirtyHandler(PouchHandler.APOTHECARY_POUCH_INVENTORY_SLOTS);
    this.herbHandler = new HerbHandler(PouchHandler.APOTHECARY_POUCH_HERB_SLOTS);
  }

  public int refill(ItemStack herbStack) {
    return herbHandler.refill(herbStack);
  }

  public UUID getUuid() {
    return uuid;
  }

  public MarkDirtyHandler getInventoryHandler() {
    return inventoryHandler;
  }

  public HerbHandler getHerbHandler() {
    return herbHandler;
  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    inventoryHandler.deserializeNBT(nbt.getCompoundTag("inventory"));
    herbHandler.deserializeNBT(nbt.getCompoundTag("herbs"));
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setTag("inventory", inventoryHandler.serializeNBT());
    compound.setTag("herbs", herbHandler.serializeNBT());
    return compound;
  }

  public class MarkDirtyHandler extends ItemStackHandler {
    public MarkDirtyHandler(int size) {
      super(size);
    }

    @Override
    protected void onContentsChanged(int slot) {
      super.onContentsChanged(slot);
      markDirty();
    }
  }

  public class HerbHandler extends MarkDirtyHandler {
    public HerbHandler(int size) {
      super(size);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
      return HerbRegistry.isHerb(stack.getItem());
    }

    public int refill(ItemStack herbStack) {
      if (!containsHerb(herbStack.getItem())) {
        return herbStack.getCount();
      }

      Item herb = herbStack.getItem();
      int count = herbStack.getCount();

      for (int i = 0; i < stacks.size(); i++) {
        ItemStack stack = stacks.get(i);
        if (stack.getItem() == herb) {
          if (stack.getCount() < stack.getMaxStackSize()) {
            int consumed = Math.min(count, stack.getMaxStackSize() - stack.getCount());
            if (consumed > 0) {
              stack.grow(consumed);
              count = Math.max(0, count - consumed);
              stacks.set(i, stack);
            }
          }
        }
        if (count == 0) {
          return 0;
        }
      }

      return count;
    }

    public boolean containsHerb(Item item) {
      for (ItemStack stack : stacks) {
        if (stack.getItem() == item) {
          return true;
        }
      }

      return false;
    }
  }
}
