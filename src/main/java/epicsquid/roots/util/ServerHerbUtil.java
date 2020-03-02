package epicsquid.roots.util;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.api.Herb;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.integration.baubles.pouch.BaublePowderInventoryUtil;
import epicsquid.roots.item.ItemPouch;
import epicsquid.roots.item.ItemSylvanArmor;
import epicsquid.roots.network.MessageUpdateHerb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerHerbUtil {
  public static ItemStack getFirstPouch(EntityPlayer player) {
    if (player.world.isRemote) {
      return ItemStack.EMPTY;
    }

    List<ItemStack> pouches = getPouches(player);
    if (pouches.isEmpty()) {
      return ItemStack.EMPTY;
    }

    return pouches.get(0);
  }

  public static List<ItemStack> getPouches(EntityPlayer player) {
    if (player.world.isRemote) {
      return Collections.emptyList();
    }

    List<ItemStack> result = new ArrayList<>();
    if (Loader.isModLoaded("baubles")) {
      ItemStack stack = BaublePowderInventoryUtil.getPouch(player);
      if (!stack.isEmpty()) {
        result.add(stack);
      }
    }

    for (int i = 0; i < 36; i++) {
      if (player.inventory.getStackInSlot(i).getItem() instanceof ItemPouch) {
        result.add(player.inventory.getStackInSlot(i));
      }
    }

    return result;
  }

  public static double getPowderTotal(EntityPlayer player, Herb herb) {
    if (player.world.isRemote) {
      return 0;
    }

    List<ItemStack> pouches = getPouches(player);
    if (pouches.isEmpty()) return -1.0;

    double quantity = 0;
    for (ItemStack pouch : pouches) {
      // Hard-coding for creative pouch
      if (pouch.getItem() == ModItems.creative_pouch) {
        quantity += 999;
      } else {
        quantity += ItemPouch.getHerbQuantity(player, pouch, herb);
      }
    }

    return quantity;
  }

  public static void removePowder(EntityPlayer player, Herb herb, double amount) {
    if (player.world.isRemote) {
      return;
    }

    List<ItemStack> pouches = getPouches(player);

    double quantity = 0;

    // TODO: Cost reduction is calculated here
    double total = amount - amount * ItemSylvanArmor.sylvanBonus(player);
    for (ItemStack pouch : pouches) {
      if (pouch.getItem() == ModItems.creative_pouch) {
        continue;
      }

      if (total > 0) {
        total = ItemPouch.useQuantity(player, pouch, herb, total);
      }

      quantity += ItemPouch.getHerbQuantity(player, pouch, herb);
    }

    MessageUpdateHerb message = new MessageUpdateHerb(herb, quantity);
    PacketHandler.INSTANCE.sendTo(message, (EntityPlayerMP) player);
  }
}