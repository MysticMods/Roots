package epicsquid.roots.util;

import java.util.HashMap;
import java.util.Map;

import epicsquid.mysticalworld.init.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class OfferingUtil {
  public static Map<Item, Integer> values = new HashMap<>();

  public static void init(){
    values.put(ModItems.moonglow_leaf, 2);
    values.put(ModItems.terra_moss, 2);
    values.put(ModItems.wildroot, 2);
    values.put(ModItems.aubergine, 2);
    values.put(ModItems.pereskia, 2);
    values.put(ModItems.pereskia_bulb, 2);
    values.put(ModItems.aubergine_seed, 2);

    values.put(Items.WHEAT, 1);
    values.put(Items.POTATO, 1);
    values.put(Items.CARROT, 1);
    values.put(Items.BEETROOT, 1);

    values.put(Items.BLAZE_POWDER, 16);
    values.put(Items.ENDER_PEARL, 16);
    values.put(Items.SPIDER_EYE, 16);

    values.put(Items.APPLE, 4);
  }

  public static int getValue(ItemStack heldItem) {
    if (!heldItem.isEmpty()){
      if (values.containsKey(heldItem.getItem())){
        return values.get(heldItem.getItem())*heldItem.getCount();
      }
    }
    return 0;
  }

}