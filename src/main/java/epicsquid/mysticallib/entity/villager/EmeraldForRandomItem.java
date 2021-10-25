package epicsquid.mysticallib.entity.villager;

import java.util.Random;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class EmeraldForRandomItem implements EntityVillager.ITradeList {
  private Item[] buyingItems;
  private EntityVillager.PriceInfo price;

  public EmeraldForRandomItem(EntityVillager.PriceInfo priceIn, Item... items) {
    this.buyingItems = items;
    this.price = priceIn;
  }

  @Override
  public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
    int i = 1;

    if (this.price != null) {
      i = this.price.getPrice(random);
    }

    Item item = buyingItems[random.nextInt(buyingItems.length)];

    recipeList.add(new MerchantRecipe(new ItemStack(item, i, 0), Items.EMERALD));
  }
}
