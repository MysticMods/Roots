package epicsquid.roots.item;

import net.minecraft.item.Item;

public class PouchItem extends Item implements IPouch {
  public PouchItem(Properties properties) {
    super(properties);
  }

  public boolean isApothecary() {
    return false;
  }

  public boolean isCreative() {
    return false;
  }

/*  public static boolean hasHerb(@Nonnull ItemStack pouch, Herb herb) {
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
  }*/

/*  @Override
  @Nonnull
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
    ItemStack stack = player.getHeldItem(hand);
    boolean isBaublesLoaded = Loader.isModLoaded("baubles");
    boolean open_gui = false;
    if (GeneralConfig.AutoEquipPouches) {
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
    } else {
      open_gui = true;
    }
    if (!world.isRemote && open_gui) {
      player.openGui(Roots.getInstance(), GuiHandler.POUCH_ID, world, 0, 0, 0);
    }
    return new ActionResult<>(ActionResultType.SUCCESS, stack);
  }

  private static ItemStack createData(ItemStack stack, Herb herb, double quantity) {
    if (!stack.hasTag()) {
      stack.setTag(new CompoundNBT());
    }
    stack.getTag().setDouble(herb.getName(), quantity);
    return stack;
  }

  private static double getNbtQuantity(@Nonnull ItemStack stack, String plantName) {
    if (stack.hasTag()) {
      if (stack.getTag().contains(plantName)) {
        return stack.getTag().getDouble(plantName);
      }
    }
    return 0.0;
  }

  public static double useQuantity(@Nonnull ItemStack stack, Herb herb, double quantity) {
    double temp = quantity;
    if (stack.hasTag() && stack.getTag().contains(herb.getName())) {
      temp = temp - stack.getTag().getDouble(herb.getName());
      if (temp >= 0) {
        stack.getTag().removeTag(herb.getName());
        if (temp > 0 && addHerbToNbt(stack, herb)) {
          temp = useQuantity(stack, herb, temp);
        }
      } else {
        stack.getTag().setDouble(herb.getName(), stack.getTag().getDouble(herb.getName()) - quantity);
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
      if (!stack.isEmpty() && HerbRegistry.isHerb(stack.getItem()) && HerbRegistry.getHerbByItem(stack.getItem()).equals(herb)) {
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

    super.addInformation(stack, worldIn, tooltip, flagIn);
  }*/
}
