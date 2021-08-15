package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualSpreadingForest;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.properties.Property;
import epicsquid.roots.ritual.conditions.ConditionRunedPillars;
import epicsquid.roots.ritual.conditions.ConditionStandingStones;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class RitualSpreadingForest extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(2400);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 35).setDescription("Radius on the X Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 30).setDescription("Radius on the Y Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 35).setDescription("Radius on the Z Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_PLACE_INTERVAL = new Property<>("place_interval", 30).setDescription("interval in ticks for each sapling to be planted");
  public static Property<Integer> PROP_GROWTH_INTERVAL = new Property<>("growth_interval", 20).setDescription("interval in ticks for each planted saplings to grow into a full tree");
  public static Property<Float> PROP_DOUBLE_CHANCE = new Property<>("double_chance", 0.05f).setDescription("chances of growing a double sapling tree (the higher the value is the lower the chance becomes)");

  public int radius_x, radius_y, radius_z, place_interval, growth_interval;
  public float double_chance;

  public RitualSpreadingForest(String name, boolean disabled) {
    super(name, disabled);
    properties.add(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_PLACE_INTERVAL, PROP_GROWTH_INTERVAL, PROP_DOUBLE_CHANCE);
    setEntityClass(EntityRitualSpreadingForest.class);
  }

  @Override
  public void init() {
    recipe = new RitualRecipe(this,
        new ItemStack(ModItems.terra_moss),
        new ItemStack(ModItems.spirit_herb),
        new OreIngredient("rootsBark"),
        new OreIngredient("treeSapling"),
        new OreIngredient("treeSapling")
    );
    addCondition(new ConditionRunedPillars(RitualUtil.RunedWoodType.ACACIA, 4, 1));
    addCondition(new ConditionRunedPillars(RitualUtil.RunedWoodType.JUNGLE, 4, 1));
    addCondition(new ConditionRunedPillars(RitualUtil.RunedWoodType.SPRUCE, 4, 1));
    setIcon(ModItems.ritual_spreading_forest);
    setColor(TextFormatting.GREEN);
    setBold(true);
  }

  @Override
  public void doFinalise() {
    duration = properties.get(PROP_DURATION);
    int[] radius = properties.getRadius();
    radius_x = radius[0];
    radius_y = radius[1];
    radius_z = radius[2];
    place_interval = properties.get(PROP_PLACE_INTERVAL);
    growth_interval = properties.get(PROP_GROWTH_INTERVAL);
    double_chance = properties.get(PROP_DOUBLE_CHANCE);
  }
}