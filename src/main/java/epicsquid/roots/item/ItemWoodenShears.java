package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemShearsBase;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemWoodenShears extends ItemShearsBase {
  public ItemWoodenShears(String name) {
    super(name);
    setMaxDamage(57); // 238 / 4.2
  }

  private static int planksWood = -1;

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    if (planksWood == -1) {
      planksWood = OreDictionary.getOreID("plankWood");
    }
    return toRepair.getItem() == this && new IntOpenHashSet(OreDictionary.getOreIDs(repair)).contains(planksWood);
  }

  @Override
  public boolean isRepairable() {
    return true;
  }
}
