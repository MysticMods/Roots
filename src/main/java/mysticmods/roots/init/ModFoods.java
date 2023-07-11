package mysticmods.roots.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
  public static final FoodProperties FISH_AND_CHIPS = (new FoodProperties.Builder().nutrition(10).meat().saturationMod(0.8f)).build();
  public static final FoodProperties VENISON = (new FoodProperties.Builder().nutrition(3).meat().saturationMod(0.3f)).build();
  public static final FoodProperties COOKED_VENISON = (new FoodProperties.Builder().nutrition(7).meat().saturationMod(0.8f)).build();
  public static final FoodProperties AUBERGINE = (new FoodProperties.Builder().nutrition(4).saturationMod(0.3f)).build();
  public static final FoodProperties COOKED_BEETROOT = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.8F).build();
  public static final FoodProperties COOKED_CARROT = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.6F).build();
  public static final FoodProperties SLICED_CARROT = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.6F).fast().build();
  public static final FoodProperties COOKED_AUBERGINE = (new FoodProperties.Builder().nutrition(5).saturationMod(0.8f)).build();
  public static final FoodProperties STUFFED_AUBERGINE = (new FoodProperties.Builder().nutrition(10).saturationMod(0.8f)).build();
  public static final FoodProperties RAW_SQUID = (new FoodProperties.Builder().nutrition(1).saturationMod(0.3f)).meat().build();
  public static final FoodProperties COOKED_SQUID = (new FoodProperties.Builder().nutrition(3).saturationMod(0.8f)).meat().build();
  public static final FoodProperties COOKED_SEEDS = (new FoodProperties.Builder().nutrition(1).saturationMod(0.4f)).fast().build();
  // Drinks

  public static final FoodProperties APPLE_CORDIAL = (new FoodProperties.Builder().nutrition(1).saturationMod(7.5f)).build();
  public static final FoodProperties CACTUS_SYRUP = new FoodProperties.Builder().nutrition(1).saturationMod(7.5f).build();
  public static final FoodProperties DANDELION_CORDIAL = new FoodProperties.Builder().nutrition(1).saturationMod(7.5f).alwaysEat()./*effect(
      () -> new MobEffectInstance(ModEffects.WAKEFUL.get()), 1f)*/build();
  public static final FoodProperties LILAC_CORDIAL = new FoodProperties.Builder().nutrition(1).saturationMod(7.5f).build();
  public static final FoodProperties PEONY_CORDIAL = new FoodProperties.Builder().nutrition(1).saturationMod(7.5f).build();
  public static final FoodProperties ROSE_CORDIAL = new FoodProperties.Builder().nutrition(1).saturationMod(7.5f).build();
  public static final FoodProperties VINEGAR = new FoodProperties.Builder().nutrition(1).saturationMod(7.5f).alwaysEat().effect(
    () -> new MobEffectInstance(MobEffects.HUNGER, 300, 0, false, false), 1.0f).build();
  public static final FoodProperties VEGETABLE_JUICE = new FoodProperties.Builder().nutrition(1).saturationMod(7.5f).build();

  // Salads
  public static final FoodProperties AUBERGINE_SALAD = new FoodProperties.Builder().nutrition(5).saturationMod(0.4F).build();
  public static final FoodProperties BEETROOT_SALAD = new FoodProperties.Builder().nutrition(2).saturationMod(0.6F).build();
  public static final FoodProperties CACTUS_DANDELION_SALAD = new FoodProperties.Builder().nutrition(3).saturationMod(0.4F).build();
  public static final FoodProperties DANDELION_CORNFLOWER_SALAD = new FoodProperties.Builder().nutrition(3).saturationMod(0.4F).build();
  public static final FoodProperties STEWED_EGGPLANT = new FoodProperties.Builder().nutrition(6).saturationMod(0.8F).build();

  public static final FoodProperties WILDROOT_STEW = (new FoodProperties.Builder().nutrition(5).saturationMod(0.6f)).build();
}
