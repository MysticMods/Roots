package epicsquid.mysticallib.entity.villager;

import java.util.Random;

import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class EmeraldForRandomItem implements VillagerEntity.ITradeList {
  private Item[] buyingItems;
  private VillagerEntity.PriceInfo price;

  public EmeraldForRandomItem(VillagerEntity.PriceInfo priceIn, Item... items) {
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
