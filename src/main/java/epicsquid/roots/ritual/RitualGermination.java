package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualGermination;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.properties.Property;
import epicsquid.roots.ritual.conditions.ConditionStandingStones;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class RitualGermination extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(6400);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 19).setDescription("Radius on the X Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 19).setDescription("Radius on the Y Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 19).setDescription("Radius on the Z Axis of the cube in which the ritual takes place");
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(65).setDescription("interval in ticks between each ritual growth pulse");
  public static Property<Integer> PROP_COUNT = new Property<>("count", 6).setDescription("maximum number of crops boosted every pulse");
  public static Property<Integer> PROP_TICKS = new Property<>("ticks", 5).setDescription("the number of times a single crop is boosted by bonus and crop ticks every ritual interval");
  public static Property<Integer> PROP_BONUS_TICKS = new Property<>("bonus_ticks", 2).setDescription("bonus ticks to be added to the growth boost");
  public static Property<Integer> PROP_BLOCK_CROP_TICKS = new Property<>("block_crop_ticks", 15).setDescription("number of ticks the crop is boosted by each interval");

  public int radius_x, radius_y, radius_z, interval;
  public int count, ticks, bonus_ticks, crop_ticks;

  public RitualGermination(String name, boolean disabled) {
    super(name, disabled);
    properties.add(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_INTERVAL, PROP_COUNT, PROP_TICKS, PROP_BONUS_TICKS, PROP_BLOCK_CROP_TICKS);
    setEntityClass(EntityRitualGermination.class);
  }

  @Override
  public void init() {
    recipe = new RitualRecipe(this,
        new ItemStack(ModItems.spirit_herb),
        new ItemStack(ModItems.wildroot),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine),
        new OreIngredient("dyeWhite"),
        new OreIngredient("rootsBark")
    );
    addCondition(new ConditionStandingStones(3, 1));
    setIcon(ModItems.ritual_germination);
    setColor(TextFormatting.DARK_RED);
    setBold(true);
  }

  @Override
  public void doFinalise() {
    duration = properties.get(PROP_DURATION);
    int[] radius = properties.getRadius();
    radius_x = radius[0];
    radius_y = radius[1];
    radius_z = radius[2];
    interval = properties.get(PROP_INTERVAL);
    count = properties.get(PROP_COUNT);
    ticks = properties.get(PROP_TICKS);
    bonus_ticks = properties.get(PROP_BONUS_TICKS);
    crop_ticks = properties.get(PROP_BLOCK_CROP_TICKS);
  }
}
