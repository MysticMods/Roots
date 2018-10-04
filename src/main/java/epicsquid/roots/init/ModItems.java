package epicsquid.roots.init;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.mysticallib.item.ItemBase;
import epicsquid.roots.Roots;
import epicsquid.roots.item.ItemPetalDust;
import epicsquid.roots.item.ItemPouch;
import epicsquid.roots.item.ItemStaff;
import net.minecraft.item.Item;

public class ModItems {

  // All mod items
  public static Item pestle, pouch, petal_dust, staff;

  /**
   * Register all items
   */
  public static void registerItems(@Nonnull RegisterContentEvent event) {
    event.addItem(pestle = new ItemBase("pestle").setModelCustom(true).setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(pouch = new ItemPouch("pouch").setModelCustom(true).setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(petal_dust = new ItemPetalDust("petal_dust").setModelCustom(true).setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(staff = new ItemStaff("staff").setModelCustom(true).setCreativeTab(Roots.tab).setMaxStackSize(1));
  }

  /**
   * Register item oredicts here
   */
  public static void registerOredict() {

  }
}
