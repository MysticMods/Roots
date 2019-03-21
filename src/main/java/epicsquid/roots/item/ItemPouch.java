package epicsquid.roots.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.capability.pouch.PouchItemHandler;
import epicsquid.roots.gui.GuiHandler;
import epicsquid.roots.init.HerbRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

public class ItemPouch extends ItemBase {

  private int inventorySlots;
  private int herbSlots;

  public ItemPouch(@Nonnull String name, int inventorySlots, int herbSlots) {
    super(name);
    this.inventorySlots = inventorySlots;
    this.herbSlots = herbSlots;
    this.setMaxStackSize(1);
  }

  public static boolean hasHerb(@Nonnull ItemStack pouch, Herb herb) {
    return getHerbQuantity(pouch, herb) > 0;
  }

  public static double getHerbQuantity(@Nonnull ItemStack pouch, Herb herb) {
    if (!pouch.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
      return 0.0;
    }
    PouchItemHandler handler = (PouchItemHandler) pouch.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    for (int i = handler.getInventorySlots(); i < handler.getInventorySlots() + handler.getHerbSlots(); i++) {
      ItemStack stack = handler.getStackInSlot(i);
      if (!stack.isEmpty() && HerbRegistry.containsHerbItem(stack.getItem()) && HerbRegistry.getHerbByItem(stack.getItem()).equals(herb)) {
        return stack.getCount() + getNbtQuantity(pouch, herb.getName());
      }
    }
    return getNbtQuantity(pouch, herb.getName());
  }

  @Nullable
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
    return new PouchItemHandler(inventorySlots, herbSlots);
  }

  @Override
  @Nonnull
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
    player.openGui(Roots.getInstance(), GuiHandler.POUCH_ID, world, 0, 0, 0);
    return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
  }

  private static ItemStack createData(ItemStack stack, Herb herb, double quantity) {
    if (!stack.hasTagCompound()) {
      stack.setTagCompound(new NBTTagCompound());
    }
    stack.getTagCompound().setDouble(herb.getName(), quantity);
    return stack;
  }

  private static double getNbtQuantity(@Nonnull ItemStack stack, String plantName) {
    if (stack.hasTagCompound()) {
      if (stack.getTagCompound().hasKey(plantName)) {
        return stack.getTagCompound().getDouble(plantName);
      }
    }
    return 0.0;
  }

  public static double useQuantity(@Nonnull ItemStack stack, Herb herb, double quantity) {
    double temp = quantity;
    if (stack.hasTagCompound() && stack.getTagCompound().hasKey(herb.getName())) {
      temp = temp - stack.getTagCompound().getDouble(herb.getName());
      if (temp >= 0) {
        stack.getTagCompound().removeTag(herb.getName());
        if (temp > 0 && addHerbToNbt(stack, herb)) {
          temp = useQuantity(stack, herb, temp);
        }
      } else {
        stack.getTagCompound().setDouble(herb.getName(), stack.getTagCompound().getDouble(herb.getName()) - quantity);
        temp = 0;
      }
    } else {
      if (addHerbToNbt(stack, herb)) {
        temp = useQuantity(stack, herb, quantity);
      }
    }
    return temp;
  }

  private static boolean addHerbToNbt(@Nonnull ItemStack pouch, Herb herb) {
    if (pouch.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
      PouchItemHandler handler = (PouchItemHandler) pouch.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
      for (int i = handler.getInventorySlots(); i < handler.getInventorySlots() + handler.getHerbSlots(); i++) {
        ItemStack stack = handler.getStackInSlot(i);
        if (!stack.isEmpty() && HerbRegistry.containsHerbItem(stack.getItem()) && HerbRegistry.getHerbByItem(stack.getItem()).equals(herb)) {
          createData(pouch, herb, 1.0);
          stack.shrink(1);
          return true;
        }
      }
    }
    return false;
  }
}