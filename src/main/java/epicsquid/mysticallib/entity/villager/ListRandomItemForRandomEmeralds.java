package epicsquid.mysticallib.entity.villager;

import java.util.Random;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class ListRandomItemForRandomEmeralds implements EntityVillager.ITradeList {
  public Item[] items;
  public EntityVillager.PriceInfo price;

  public ListRandomItemForRandomEmeralds(EntityVillager.PriceInfo price, Item... items) {
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
