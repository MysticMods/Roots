package epicsquid.roots.item;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import epicsquid.mysticallib.item.ItemArrowBase;
import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.gui.GuiHandler;
import epicsquid.roots.handler.PouchHandler;
import epicsquid.roots.handler.QuiverHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.integration.baubles.quiver.BaubleQuiverInventoryUtil;
import epicsquid.roots.network.MessageServerTryPickupArrows;
import epicsquid.roots.util.QuiverInventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.List;

@Mod.EventBusSubscriber(modid=Roots.MODID)
public class ItemQuiver extends ItemArrowBase {
  private static AxisAlignedBB bounding = new AxisAlignedBB(-1.5, -1.5, -1.5, 1.5, 1.5, 1.5);
  private static boolean lastSneak = false;

  public ItemQuiver(@Nonnull String name) {
    super(name);
    this.setMaxStackSize(1);
    this.setMaxDamage(128);
  }

  @Override
  public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
    ItemStack livingArrow = findLivingArrow(stack);
    if (!livingArrow.isEmpty()) {
      return ((ItemArrow) livingArrow.getItem()).createArrow(worldIn, livingArrow, shooter);
    }

    ItemStack normalArrow = findOtherArrow(stack);
    if (!normalArrow.isEmpty()) {
      EntityArrow arrow = ((ItemArrow) normalArrow.getItem()).createArrow(worldIn, normalArrow, shooter);
      arrow.setDamage(arrow.getDamage() + 3.0D);
      stack.damageItem(2, shooter);
      return arrow;
    }

    EntityArrow arrow = new EntityTippedArrow(worldIn, shooter);
    arrow.setDamage(1.5D);
    arrow.getEntityData().setBoolean("generated", true);
    stack.damageItem(5, shooter);
    return arrow;
  }

  @Override
  public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) {
    return true;
  }

  public static ItemStack findLivingArrow (ItemStack quiver) {
    QuiverHandler handler = QuiverHandler.getHandler(quiver);
    ItemStack result = ItemStack.EMPTY;
    for (int i = 0; i < handler.getInventory().getSlots(); i++) {
      ItemStack stack = handler.getInventory().getStackInSlot(i);
      if (stack.isEmpty() || !(stack.getItem() instanceof ItemArrow)) continue;
      if (stack.getItem() != ModItems.living_arrow) continue;
      result = handler.getInventory().extractItem(i, 1, false);
      break;
    }

    return result;
  }

  public static ItemStack findOtherArrow (ItemStack quiver) {
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

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public static void onPlayerSneak (TickEvent.ClientTickEvent event) {
    Minecraft mc = Minecraft.getMinecraft();
    if (mc.player == null) return;

    if (lastSneak != mc.player.isSneaking() && !lastSneak) {
      lastSneak = mc.player.isSneaking();

      List<EntityArrow> arrows = mc.world.getEntitiesWithinAABB(EntityArrow.class, bounding.offset(mc.player.getPosition()));
      if (arrows.isEmpty()) return;

      ItemStack quiver = QuiverInventoryUtil.getQuiver(mc.player);
      if (quiver.isEmpty()) return;

      QuiverHandler handler = QuiverHandler.getHandler(quiver);
      if (!handler.canConsume()) return;

      MessageServerTryPickupArrows packet = new MessageServerTryPickupArrows();
      PacketHandler.INSTANCE.sendToServer(packet);
    }

    lastSneak = mc.player.isSneaking();
  }

  public static void tryPickupArrows (EntityPlayer player) {
    ItemStack quiver = QuiverInventoryUtil.getQuiver(player);
    if (quiver.isEmpty()) return;

    List<EntityArrow> arrows = player.world.getEntitiesWithinAABB(EntityArrow.class, bounding.offset(player.getPosition()));
    if (arrows.isEmpty()) return;

    QuiverHandler handler = QuiverHandler.getHandler(quiver);
    if (!handler.canConsume()) {
      player.sendStatusMessage(new TextComponentTranslation("roots.quiver.quiver_full").setStyle(new Style().setColor(TextFormatting.RED).setBold(true)), true);
      return;
    }

    int consumed = 0;
    int broke = 0;
    for (EntityArrow arrow : arrows) {
      arrow.setDead();
      if (arrow.getEntityData().hasKey("generated")) {
        continue;
      }
      if (Util.rand.nextInt(3) == 0) {
        broke++;
      } else {
        if (!handler.consume()) {
          break;
        } else {
          consumed++;
        }
      }
    }
    ITextComponent comp = new TextComponentString("");
    if (broke == 1) {
      comp = new TextComponentTranslation("roots.quiver.one_broke");
    } else if (broke > 1) {
      comp = new TextComponentTranslation("roots.quiver.some_broke");
    }
    if (consumed > 0) {
      if (consumed == 1) {
        player.sendStatusMessage(new TextComponentTranslation("roots.quiver.picked_up_arrow", comp).setStyle(new Style().setColor(TextFormatting.GREEN).setBold(true)), true);
      } else {
        player.sendStatusMessage(new TextComponentTranslation("roots.quiver.picked_up_arrows", comp).setStyle(new Style().setColor(TextFormatting.GREEN).setBold(true)), true);
      }
    } else if (consumed == 0 && broke == 0) {
      player.sendStatusMessage(new TextComponentTranslation("roots.quiver.fragile").setStyle(new Style().setColor(TextFormatting.DARK_GREEN).setBold(true)), true);
    } else if (consumed == 0 && broke > 0) {
      player.sendStatusMessage(new TextComponentTranslation("roots.quiver.broke").setStyle(new Style().setColor(TextFormatting.DARK_GREEN).setBold(true)), true);
    }
  }

  public static int repairChance = 750;

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    int chance = Util.rand.nextInt(repairChance);
    if(chance == 0){
      stack.setItemDamage(stack.getItemDamage()-1);
    }
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }

  @Override
  public EnumRarity getRarity(ItemStack stack) {
    return EnumRarity.RARE;
  }

  @SubscribeEvent
  @Optional.Method(modid="baubles")
  public static void onBaubleTick (TickEvent.PlayerTickEvent event) {
    ItemStack quiver = BaubleQuiverInventoryUtil.getQuiver(event.player);
    if (quiver.isEmpty() || quiver.getItemDamage() == 0) return;

    if (Util.rand.nextInt(repairChance) == 0) {
      quiver.setItemDamage(quiver.getItemDamage()-1);
    }
  }
}