package epicsquid.roots.world.data;

import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.item.PouchType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

@SuppressWarnings({"WeakerAccess", "NullableProblems"})
public class PouchHandlerData extends WorldSavedData {
  private static final String identifier = "Inventory-";

  private UUID uuid;
  private MarkDirtyHandler inventoryHandler;
  private HerbHandler herbHandler;
  private PouchType type = null;
  private boolean defer = false;

  public static String name(UUID uuid) {
    return identifier + uuid.toString();
  }

  public PouchType getType() {
    return type;
  }

  public PouchHandlerData(String identifier) {
    super(identifier);
    this.uuid = UUID.fromString(identifier.replace(PouchHandlerData.identifier, ""));
    defer = true;
  }

  public PouchHandlerData(UUID uuid, PouchType type) {
    super(name(uuid));
    this.uuid = uuid;
    this.type = type;
    createHandler();
  }

  public void upgrade(PouchType newType) {
    if (type.ordinal() >= newType.ordinal()) {
      return;
    }

    this.type = newType;
    this.inventoryHandler.setSize(type.inventorySlots());
    this.herbHandler.setSize(type.herbSlots());
    markDirty();
  }

  private void createHandler() {
    if (type == null) {
      throw new IllegalStateException("Attempted to instantiate PouchHandlerData before type was set.");
    }
    this.inventoryHandler = new MarkDirtyHandler(type.inventorySlots());
    this.herbHandler = new HerbHandler(type.herbSlots());
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
  public void readFromNBT(CompoundNBT nbt) {
    this.type = PouchType.fromOrdinal(nbt.getInteger("pouch"));
    if (defer) {
      createHandler();
    }
    inventoryHandler.deserializeNBT(nbt.getCompoundTag("inventory"));
    herbHandler.deserializeNBT(nbt.getCompoundTag("herbs"));
  }

  @Override
  public CompoundNBT writeToNBT(CompoundNBT compound) {
    compound.setInteger("pouch", type.ordinal());
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

    @Override
    public void setSize(int size) {
      List<ItemStack> original = stacks;
      super.setSize(size);
      for (int i = 0; i < Math.min(original.size(), size); i++) {
        stacks.set(i, original.get(i));
      }
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
