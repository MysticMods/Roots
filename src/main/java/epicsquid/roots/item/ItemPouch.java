package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.gui.GuiHandler;
import epicsquid.roots.gui.Keybinds;
import epicsquid.roots.handler.PouchHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.integration.baubles.pouch.PouchEquipHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemPouch extends ItemBase {

  public ItemPouch(@Nonnull String name) {
    super(name);
    this.setMaxStackSize(1);
  }

  public boolean isApothecary() {
    return false;
  }

  public static boolean hasHerb(@Nonnull ItemStack pouch, Herb herb) {
    return getHerbQuantity(pouch, herb) > 0;
  }

  public static double getHerbQuantity(@Nonnull ItemStack pouch, Herb herb) {
    PouchHandler pouchHandler = PouchHandler.getHandler(pouch);
    if (pouchHandler == null) return 0.0;
    double count = getNbtQuantity(pouch, herb.getName());
    IItemHandler handler = pouchHandler.getHerbs();
    for (int i = 0; i < handler.getSlots(); i++) {
      ItemStack stack = handler.getStackInSlot(i);
      if (!stack.isEmpty() && herb.equals(HerbRegistry.getHerbByItem(stack.getItem()))) {
        count += stack.getCount();
      }
    }
    return count;
  }

  @Override
  @Nonnull
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
    ItemStack stack = player.getHeldItem(hand);
    boolean isBaublesLoaded = Loader.isModLoaded("baubles");
    boolean open_gui = false;
    if (player.isSneaking()) {
      open_gui = true;
    }
    if (isBaublesLoaded) {
      if (!world.isRemote) {
        if (!PouchEquipHandler.tryEquipPouch(player, stack)) {
          open_gui = true;
        }
      }
    }
    if (!world.isRemote && open_gui) {
      player.openGui(Roots.getInstance(), GuiHandler.POUCH_ID, world, 0, 0, 0);
    }
    return new ActionResult<>(EnumActionResult.SUCCESS, stack);
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

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    if (Loader.isModLoaded("baubles")) {
      tooltip.add(TextFormatting.GREEN + I18n.format("roots.tooltip.pouch", Keybinds.POUCH_KEYBIND.getDisplayName()));
    } else {
      tooltip.add(TextFormatting.GREEN + I18n.format("roots.tooltip.pouch2", Keybinds.POUCH_KEYBIND.getDisplayName()));
    }
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }
}
