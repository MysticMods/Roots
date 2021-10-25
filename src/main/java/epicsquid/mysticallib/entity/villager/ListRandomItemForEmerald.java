package epicsquid.mysticallib.entity.villager;

import java.util.Random;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class ListRandomItemForEmerald implements EntityVillager.ITradeList {
  private Item[] sellingItems;
  private EntityVillager.PriceInfo price;

  public ListRandomItemForEmerald(EntityVillager.PriceInfo priceIn, Item... items) {
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
