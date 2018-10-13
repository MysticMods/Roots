package epicsquid.roots.util;

import java.util.HashMap;
import java.util.Map;

import epicsquid.roots.grove.GroveType;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class OfferingUtil {
  private static Map<Item, Float> values = new HashMap<>();
  private static Map<Item, GroveType> groveItems = new HashMap<>();

  public static void init() {
    //Wild Grove
    values.put(Items.WHEAT, 1f);
    values.put(Items.MELON, 0.5f);
    values.put(Items.CHICKEN, 1f);
    groveItems.put(Items.WHEAT, GroveType.WILD);
    groveItems.put(Items.MELON, GroveType.WILD);
    groveItems.put(Items.CHICKEN, GroveType.WILD);

    //    values.put(ModItems.moonglow_leaf, 2f);
    //    values.put(ModItems.terra_moss, 2f);
    //    values.put(ModItems.wildroot, 2f);
    //    values.put(ModItems.aubergine, 2f);
    //    values.put(ModItems.pereskia, 2f);
    //    values.put(ModItems.pereskia_bulb, 2f);
    //    values.put(ModItems.aubergine_seed, 2f);
    //

    //    values.put(Items.POTATO, 1f);
    //    values.put(Items.CARROT, 1f);
    //    values.put(Items.BEETROOT, 1f);

    //    values.put(Items.BLAZE_POWDER, 16f);
    //    values.put(Items.ENDER_PEARL, 16f);
    //    values.put(Items.SPIDER_EYE, 16f);
    //
    //    values.put(Items.APPLE, 4f);
    //
    //    groveItems.put(ModItems.moonglow_leaf, GroveType.WILD);
    //    groveItems.put(ModItems.terra_moss, GroveType.WILD);
    //    groveItems.put(ModItems.wildroot, GroveType.WILD);
    //    groveItems.put(ModItems.aubergine, GroveType.WILD);
    //    groveItems.put(ModItems.pereskia, GroveType.WILD);
    //    groveItems.put(ModItems.pereskia_bulb, GroveType.WILD);
    //    groveItems.put(ModItems.aubergine_seed, GroveType.WILD);

  }

  public static float getValue(ItemStack heldItem) {
    if (!heldItem.isEmpty()) {
      if (values.containsKey(heldItem.getItem())) {
        return values.get(heldItem.getItem()) * heldItem.getCount();
      }
    }
    return 0;
  }

  public static GroveType getGroveType(ItemStack heldItem) {
    if (!heldItem.isEmpty()) {
      if (groveItems.containsKey(heldItem.getItem())) {
        return groveItems.get(heldItem.getItem());
      }
    }

    return null;
  }

}