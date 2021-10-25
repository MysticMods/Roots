package epicsquid.mysticallib.entity.villager;

import java.util.Random;

import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class ListRandomItemForEmerald implements VillagerEntity.ITradeList {
  private Item[] sellingItems;
  private VillagerEntity.PriceInfo price;

  public ListRandomItemForEmerald(VillagerEntity.PriceInfo priceIn, Item... items) {
    this.sellingItems = items;
    this.price = priceIn;
  }

  @Override
  public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
    int i = 1;

    if (this.price != null) {
      i = this.price.getPrice(random);
    }

    Item item = sellingItems[random.nextInt(sellingItems.length)];

    recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, i, 0), new ItemStack(item, 1, 0)));
  }
}
