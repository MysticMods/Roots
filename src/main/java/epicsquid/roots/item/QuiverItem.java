package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemArrowBase;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.entity.projectile.LivingArrowEntity;
import epicsquid.roots.gui.GuiHandler;
import epicsquid.roots.handler.QuiverHandler;
import epicsquid.roots.init.ModItems;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.util.QuiverInventoryUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class QuiverItem extends ItemArrowBase {
  public static AxisAlignedBB bounding = new AxisAlignedBB(-2.5, -2.5, -2.5, 2.5, 2.5, 2.5);

  public static Method getArrowStack = null;

  public QuiverItem(@Nonnull String name) {
    super(name);
    this.setMaxStackSize(1);
    this.setMaxDamage(256);
  }

  @Override
  public AbstractArrowEntity createArrow(World worldIn, ItemStack stack, LivingEntity shooter) {
    ItemStack arrow = findArrow(stack);
    if (!arrow.isEmpty()) {
      AbstractArrowEntity entityArrow = ((ArrowItem) arrow.getItem()).createArrow(worldIn, arrow, shooter);
      entityArrow.getEntityData().setBoolean("return", true);
      return entityArrow;
    }

    AbstractArrowEntity entityArrow = new ArrowEntity(worldIn, shooter);
    entityArrow.setDamage(1.5D);
    entityArrow.getEntityData().setBoolean("generated", true);

    stack.damageItem(itemRand.nextInt(2), shooter);
    return entityArrow;
  }

  @Override
  public boolean isInfinite(ItemStack stack, ItemStack bow, PlayerEntity player) {
    return true;
  }

  public static ItemStack findArrow(ItemStack quiver) {
    QuiverHandler handler = QuiverHandler.getHandler(quiver);
    ItemStack result = ItemStack.EMPTY;
    for (int i = 0; i < handler.getInventory().getSlots(); i++) {
      ItemStack stack = handler.getInventory().getStackInSlot(i);
      if (stack.isEmpty()) continue;
      result = handler.getInventory().extractItem(i, 1, false);
      break;
    }

    return result;
  }

  @Override
  @Nonnull
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
    player.openGui(Roots.getInstance(), GuiHandler.QUIVER_ID, world, 0, 0, 0);
    return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
  }

  public static void tryPickupArrows(PlayerEntity player) {
    ItemStack quiver = QuiverInventoryUtil.getQuiver(player);
    if (quiver.isEmpty()) return;

    List<AbstractArrowEntity> arrows = player.world.getEntitiesWithinAABB(AbstractArrowEntity.class, bounding.offset(player.getPosition()));
    if (arrows.isEmpty()) return;

    QuiverHandler handler = QuiverHandler.getHandler(quiver);
    int consumed = 0;
    int generated = 0;
    for (AbstractArrowEntity arrow : arrows) {
      ItemStack stack = getArrowStack(arrow);

      if (stack.isEmpty()) {
        if (arrow instanceof LivingArrowEntity) {
          stack = new ItemStack(ModItems.living_arrow);
        } else {
          stack = new ItemStack(Items.ARROW);
        }
      }

      arrow.setDead();
      if (arrow.getEntityData().contains("generated")) {
        generated++;
        continue;
      }
      if (Util.rand.nextInt(3) != 0 || arrow.getEntityData().contains("return")) {
        ItemStack result = ItemHandlerHelper.insertItemStacked(handler.getInventory(), stack, false);
        if (result.isEmpty()) {
          consumed++;
        } else {
          result = ItemHandlerHelper.insertItemStacked(player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP), result, false);
          if (!result.isEmpty()) {
            ItemUtil.spawnItem(player.world, player.getPosition(), result);
          }
        }
      }
    }

    if (consumed > 0) {
      player.sendStatusMessage(new TranslationTextComponent("roots.quiver.picked_up_arrow").setStyle(new Style().setColor(TextFormatting.GREEN).setBold(true)), true);
    } else if (consumed == 0 && generated > 0) {
      player.sendStatusMessage(new TranslationTextComponent("roots.quiver.fragile").setStyle(new Style().setColor(TextFormatting.DARK_GREEN).setBold(true)), true);
    } else {
      player.sendStatusMessage(new TranslationTextComponent("roots.quiver.broke").setStyle(new Style().setColor(TextFormatting.DARK_GREEN).setBold(true)), true);
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public Rarity getRarity(ItemStack stack) {
    return Rarity.RARE;
  }

  @Override
  public int getItemEnchantability() {
    return 22;
  }

  @Override
  public boolean isEnchantable(ItemStack stack) {
    return true;
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return toRepair.getItem() == this && repair.getItem() == ModItems.bark_wildwood;
  }

  @Override
  public boolean isRepairable() {
    return true;
  }

  public static ItemStack getArrowStack (AbstractArrowEntity arrow) {
    if (getArrowStack == null) {
      try {
        getArrowStack = AbstractArrowEntity.class.getDeclaredMethod("getArrowStack");
      } catch (NoSuchMethodException e) {
        return ItemStack.EMPTY;
      }
      getArrowStack.setAccessible(true);
    }
    try {
      return (ItemStack) getArrowStack.invoke(arrow);
    } catch (IllegalAccessException | InvocationTargetException e) {
      return ItemStack.EMPTY;
    }
  }
}