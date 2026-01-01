package mysticmods.roots.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FastFoodItem extends Item {
  public FastFoodItem(Properties properties) {
    super(properties);
  }

  @Override
  public int getUseDuration(ItemStack pStack, LivingEntity entity) {
    FoodProperties foodproperties = pStack.getFoodProperties(null);
    return foodproperties != null ? 6 : 0;
  }
}
