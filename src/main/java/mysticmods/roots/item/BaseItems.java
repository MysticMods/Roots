package mysticmods.roots.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;

import java.util.List;

public class BaseItems {
  public static class EffectItem extends Item {

    public EffectItem(Properties properties) {
      super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
      return true;
    }
  }

  public static class FastFoodItem extends Item {
    public FastFoodItem(Properties properties) {
      super(properties);
    }

    @Override
    public int getUseDuration(ItemStack pStack, LivingEntity entity) {
      FoodProperties foodproperties = pStack.getFoodProperties(null);
      return foodproperties != null ? 6 : 0;
    }
  }
}
