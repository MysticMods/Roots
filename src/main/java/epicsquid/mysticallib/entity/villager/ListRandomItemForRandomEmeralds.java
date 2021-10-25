package epicsquid.mysticallib.entity.villager;

import java.util.Random;

import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class ListRandomItemForRandomEmeralds implements VillagerEntity.ITradeList {
  public Item[] items;
  public VillagerEntity.PriceInfo price;

  public ListRandomItemForRandomEmeralds(VillagerEntity.PriceInfo price, Item... items) {
    this.items = items;
    this.price = price;
  }

  @Override
  public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
    ItemStack emeralds = new ItemStack(Items.EMERALD, price.getPrice(random), 0);

    Item item = items[random.nextInt(items.length)];

    recipeList.add(new MerchantRecipe(new ItemStack(item), emeralds));
  }
}
