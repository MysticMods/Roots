package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.GuiHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.client.Keybinds;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.handler.PouchHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemPouch extends ItemBase implements IItemPouch {
  protected PouchType type = PouchType.COMPONENT;

  public ItemPouch(@Nonnull String name) {
    super(name);
    this.setMaxStackSize(1);
  }

  public ItemPouch(@Nonnull String name, PouchType type) {
    this(name);
    this.type = type;
  }

  public PouchType getType() {
    return type;
  }

  public static PouchType getPouchType(ItemStack pouch) {
    if (!(pouch.getItem() instanceof ItemPouch)) {
      return null;
    }
    return ((ItemPouch) pouch.getItem()).getType();
  }

  public static double getHerbQuantity(PlayerEntity player, ItemStack pouch, Herb herb) {
    if (player.world.isRemote) {
      return 0;
    }

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
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
    ItemStack stack = player.getHeldItem(hand);
    if (!world.isRemote) {
      player.openGui(Roots.getInstance(), GuiHandler.POUCH_ID, world, 0, 0, 0);
    }
    return new ActionResult<>(ActionResultType.SUCCESS, stack);
  }

  private static ItemStack createData(ItemStack stack, Herb herb, double quantity) {
    if (!stack.hasTagCompound()) {
      stack.setTag(new CompoundNBT());
    }
    stack.getTagCompound().setDouble(herb.getName(), quantity);
    return stack;
  }

  private static double getNbtQuantity(@Nonnull ItemStack stack, String plantName) {
    if (stack.hasTagCompound()) {
      if (stack.getTagCompound().contains(plantName)) {
        return stack.getTagCompound().getDouble(plantName);
      }
    }
    return 0.0;
  }

  public static double useQuantity(PlayerEntity player, ItemStack stack, Herb herb, double quantity) {
    if (player.world.isRemote) {
      return 0;
    }
    double temp = quantity;
    if (stack.hasTagCompound() && stack.getTagCompound().contains(herb.getName())) {
      temp = temp - stack.getTagCompound().getDouble(herb.getName());
      if (temp >= 0) {
        stack.getTagCompound().remove(herb.getName());
        if (temp > 0 && addHerbToNbt(player, stack, herb)) {
          temp = useQuantity(player, stack, herb, temp);
        }
      } else {
        stack.getTagCompound().setDouble(herb.getName(), stack.getTagCompound().getDouble(herb.getName()) - quantity);
        temp = 0;
      }
    } else {
      if (addHerbToNbt(player, stack, herb)) {
        temp = useQuantity(player, stack, herb, quantity);
      }
    }
    return temp;
  }

  private static boolean addHerbToNbt(PlayerEntity player, ItemStack pouch, Herb herb) {
    if (player.world.isRemote) {
      return false;
    }

    PouchHandler pouchHandler = PouchHandler.getHandler(pouch);
    if (pouchHandler == null) {
      return false;
    }
    IItemHandler handler = pouchHandler.getHerbs();
    for (int i = 0; i < handler.getSlots(); i++) {
      ItemStack stack = handler.getStackInSlot(i);
      if (!stack.isEmpty() && HerbRegistry.isHerb(stack.getItem()) && Objects.equals(HerbRegistry.getHerbByItem(stack.getItem()), herb)) {
        if (!handler.extractItem(i, 1, false).isEmpty()) {
          createData(pouch, herb, 1.0);
          return true;
        }
      }
    }
    return false;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    if (GeneralConfig.AutoEquipPouches && Loader.isModLoaded("baubles")) {
      tooltip.add(TextFormatting.GREEN + I18n.format("roots.tooltip.pouch", Keybinds.POUCH_KEYBIND.getDisplayName()));
    } else {
      tooltip.add(TextFormatting.GREEN + I18n.format("roots.tooltip.pouch2", Keybinds.POUCH_KEYBIND.getDisplayName()));
    }
    if (GeneralConfig.AutoRefillPouches) {
      tooltip.add("");
      tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("roots.tooltip.refill"));
    }

    CompoundNBT tag = ItemUtil.getOrCreateTag(stack);
    DyeColor color = this == ModItems.fey_pouch ? DyeColor.BLUE : DyeColor.BROWN;
    if (tag.contains("color", Constants.NBT.TAG_INT)) {
      color = DyeColor.byMetadata(tag.getInt("color"));
    }
    tooltip.add("");
    tooltip.add(I18n.format("roots.tooltip.color", I18n.format(color.getTranslationKey())));

    super.addInformation(stack, worldIn, tooltip, flagIn);
  }

  @Nullable
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
/*    if (Loader.isModLoaded("baubles")) {
      return BaublesHook.getInstance();
    }*/
    return null;
  }
}
