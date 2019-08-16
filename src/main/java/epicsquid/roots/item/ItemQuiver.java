package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemArrowBase;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.entity.item.EntityLivingArrow;
import epicsquid.roots.gui.GuiHandler;
import epicsquid.roots.handler.QuiverHandler;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.util.ItemUtil;
import epicsquid.roots.util.QuiverInventoryUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemQuiver extends ItemArrowBase {
  public static AxisAlignedBB bounding = new AxisAlignedBB(-2.5, -2.5, -2.5, 2.5, 2.5, 2.5);

  public ItemQuiver(@Nonnull String name) {
    super(name);
    this.setMaxStackSize(1);
    this.setMaxDamage(256);
  }

  @Override
  public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
    ItemStack livingArrow = findLivingArrow(stack);
    if (!livingArrow.isEmpty()) {
      return ((ItemLivingArrow) livingArrow.getItem()).createArrow(worldIn, livingArrow, shooter);
    }

    ItemStack normalArrow = findOtherArrow(stack);
    if (!normalArrow.isEmpty()) {
      return ((ItemArrow) normalArrow.getItem()).createArrow(worldIn, normalArrow, shooter);
    }

    EntityArrow arrow = new EntityTippedArrow(worldIn, shooter);
    arrow.setDamage(1.5D);
    arrow.getEntityData().setBoolean("generated", true);

    stack.damageItem(itemRand.nextInt(2), shooter);
    return arrow;
  }

  @Override
  public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) {
    return true;
  }

  public static ItemStack findLivingArrow(ItemStack quiver) {
    QuiverHandler handler = QuiverHandler.getHandler(quiver);
    ItemStack result = ItemStack.EMPTY;
    for (int i = 0; i < handler.getInventory().getSlots(); i++) {
      ItemStack stack = handler.getInventory().getStackInSlot(i);
      if (stack.isEmpty()) continue;
      if (stack.getItem() != ModItems.living_arrow) continue;
      result = handler.getInventory().extractItem(i, 1, false);
      break;
    }

    return result;
  }

  public static ItemStack findOtherArrow(ItemStack quiver) {
    QuiverHandler handler = QuiverHandler.getHandler(quiver);
    ItemStack result = ItemStack.EMPTY;
    for (int i = 0; i < handler.getInventory().getSlots(); i++) {
      ItemStack stack = handler.getInventory().getStackInSlot(i);
      if (stack.isEmpty() || !(stack.getItem() instanceof ItemArrow) || stack.getItem() == ModItems.living_arrow)
        continue;
      result = handler.getInventory().extractItem(i, 1, false);
      break;
    }
    return result;
  }

  @Override
  @Nonnull
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
    player.openGui(Roots.getInstance(), GuiHandler.QUIVER_ID, world, 0, 0, 0);
    return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
  }

  public static void tryPickupArrows(EntityPlayer player) {
    ItemStack quiver = QuiverInventoryUtil.getQuiver(player);
    if (quiver.isEmpty()) return;

    List<EntityArrow> arrows = player.world.getEntitiesWithinAABB(EntityArrow.class, bounding.offset(player.getPosition()));
    if (arrows.isEmpty()) return;

    QuiverHandler handler = QuiverHandler.getHandler(quiver);
    int consumed = 0;
    int generated = 0;
    for (EntityArrow arrow : arrows) {
      ItemStack stack = new ItemStack(Items.ARROW);

      if (arrow instanceof EntityLivingArrow) {
        stack = new ItemStack(ModItems.living_arrow);
      }

      arrow.setDead();
      if (arrow.getEntityData().hasKey("generated")) {
        generated++;
        continue;
      }
      if (Util.rand.nextInt(3) != 0 || stack.getItem() == ModItems.living_arrow) {
        ItemStack result = ItemHandlerHelper.insertItemStacked(handler.getInventory(), stack, false);
        if (result.isEmpty()) {
          consumed++;
        } else {
          result = ItemHandlerHelper.insertItemStacked(player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP), result, false);
          if (!result.isEmpty()) {
            ItemUtil.spawnItem(player.world, player.getPosition(), result);
          }
        }
      }
    }

    if (consumed > 0) {
      player.sendStatusMessage(new TextComponentTranslation("roots.quiver.picked_up_arrow").setStyle(new Style().setColor(TextFormatting.GREEN).setBold(true)), true);
    } else if (consumed == 0 && generated > 0) {
      player.sendStatusMessage(new TextComponentTranslation("roots.quiver.fragile").setStyle(new Style().setColor(TextFormatting.DARK_GREEN).setBold(true)), true);
    } else {
      player.sendStatusMessage(new TextComponentTranslation("roots.quiver.broke").setStyle(new Style().setColor(TextFormatting.DARK_GREEN).setBold(true)), true);
    }
  }

  @Override
  public EnumRarity getRarity(ItemStack stack) {
    return EnumRarity.RARE;
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
    if (repair.getItem() == ModItems.bark_wildwood) {
      return true;
    }

    return super.getIsRepairable(toRepair, repair);
  }

  @Override
  public boolean isRepairable() {
    return true;
  }
}