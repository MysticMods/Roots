package epicsquid.roots.init;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.mysticallib.item.ItemBase;
import epicsquid.roots.Roots;
import net.minecraft.item.Item;

public class ModItems {

  // All mod items
  public static Item pestle, totem_fragment;

  /**
   * Register all items
   */
  public static void registerItems(@Nonnull RegisterContentEvent event) {
    event.addItem(pestle = new ItemBase("pestle").setModelCustom(true).setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(totem_fragment = new ItemBase("totem_fragment").setModelCustom(true).setCreativeTab(Roots.tab));
  }

  /**
   * Register item oredicts here
   */
  public static void registerOredict() {

  }
}
