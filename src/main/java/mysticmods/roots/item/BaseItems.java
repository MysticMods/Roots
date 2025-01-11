package mysticmods.roots.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;

import java.util.List;

public class BaseItems {
  @Deprecated
  public static class BowlItem extends MultiReturnItem {
    public BowlItem(Properties properties) {
      super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.EAT;
    }

    @Override
    protected Item getReturnItem(ItemStack stack) {
      return Items.BOWL;
    }
  }

  @Deprecated
  @SuppressWarnings("NullableProblems")
  public static class DrinkItem extends MultiReturnItem {
    public DrinkItem(Properties properties) {
      super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.DRINK;
    }

    @Override
    protected Item getReturnItem(ItemStack stack) {
      return Items.GLASS_BOTTLE;
    }
  }

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

  public static class KnifeItem extends DiggerItem {
    // TODO rework knives to strip logs of bark with right click, or drop bark by mining it
    public KnifeItem(Tier tier, Properties props) {
      super(tier, BlockTags.MINEABLE_WITH_AXE, props);
    }
  }
}
