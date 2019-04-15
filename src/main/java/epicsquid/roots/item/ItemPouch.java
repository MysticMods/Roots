package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.gui.GuiHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.handler.PouchHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class ItemPouch extends ItemBase {

  public ItemPouch(@Nonnull String name) {
    super(name);
    this.setMaxStackSize(1);
  }

  public static boolean hasHerb(@Nonnull ItemStack pouch, Herb herb) {
    return getHerbQuantity(pouch, herb) > 0;
  }

  public static double getHerbQuantity(@Nonnull ItemStack pouch, Herb herb) {
    PouchHandler pouchHandler = PouchHandler.getHandler(pouch);
    if (pouchHandler == null) return 0.0;
    IItemHandler handler = pouchHandler.getHerbs();
    for (int i = 0; i < handler.getSlots(); i++) {
      ItemStack stack = handler.getStackInSlot(i);
      if (!stack.isEmpty() && HerbRegistry.containsHerbItem(stack.getItem()) && HerbRegistry.getHerbByItem(stack.getItem()).equals(herb)) {
        return stack.getCount() + getNbtQuantity(pouch, herb.getName());
      }
    }
    return getNbtQuantity(pouch, herb.getName());
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
    PouchHandler pouchHandler = PouchHandler.getHandler(pouch);
    if (pouchHandler == null) return false;
    IItemHandler handler = pouchHandler.getHerbs();

    for (int i = 0; i < handler.getSlots(); i++) {
        ItemStack stack = handler.getStackInSlot(i);
        if (!stack.isEmpty() && HerbRegistry.containsHerbItem(stack.getItem()) && HerbRegistry.getHerbByItem(stack.getItem()).equals(herb)) {
          if (!handler.extractItem(i, 1, false).isEmpty()) {
            createData(pouch, herb, 1.0);
            return true;
          }
        }
      }
    return false;
  }
}