package epicsquid.mysticallib.entity.villager;

import java.util.Random;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class ListRandomItemWithPrice implements EntityVillager.ITradeList {
  private ItemAndPriceInfo[] itemsList;

  public ListRandomItemWithPrice(ItemAndPriceInfo... itemsList) {
    this.itemsList = itemsList;
  }

  @Override
  public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
    ItemAndPriceInfo info = itemsList[random.nextInt(itemsList.length)];

    int price = info.getPrice(random);
    ItemStack stack = info.getItem(random);

    recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, price, 0), stack));
  }

  public static class ItemAndPriceInfo {
    private EntityVillager.PriceInfo price;
    private Item item;
    private int minCount = 1;
    private int maxCount = 1;

    public ItemAndPriceInfo(Item item, int lower, int upper) {
      this.price = new EntityVillager.PriceInfo(lower, upper);
      this.item = item;
    }

    public ItemAndPriceInfo(Item item, int minCount, int maxCount, int lower, int upper) {
      this.price = new EntityVillager.PriceInfo(lower, upper);
      this.item = item;
      this.minCount = minCount;
      this.maxCount = maxCount;
    }

    public int getPrice(Random random) {
      return price.getPrice(random);
    }

    public ItemStack getItem (Random random) {
      if (minCount == maxCount && maxCount == 1) {
        return new ItemStack(item);
      }
      return new ItemStack(item, minCount + random.nextInt(maxCount - minCount));
    }
  }
}
