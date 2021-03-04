package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualWindwall;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.properties.Property;
import epicsquid.roots.ritual.conditions.ConditionRunedPillars;
import epicsquid.roots.ritual.conditions.ConditionStandingStones;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class RitualWindwall extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(3000);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 51).setDescription("Radius on the X Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 31).setDescription("Radius on the X Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 51).setDescription("Radius on the X Axis of the cube in which the ritual takes place");
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(10).setDescription("interval in ticks between each ritual knockback boost");
  public static Property<Integer> PROP_DISTANCE = new Property<>("distance", 51).setDescription("the distance at which hostile mobs are kept from the center of the ritual area");
  public static Property<Float> PROP_KNOCKBACK = new Property<>("knockback", 2.0f).setDescription("the knockback rate at which hostile mobs are pushed away from the center of the ritual");

  public double radius_x, radius_y, radius_z;
  public int interval, distance;
  public float knockback;

  public RitualWindwall(String name, boolean disabled) {
    super(name, disabled);
    properties.add(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_INTERVAL, PROP_DISTANCE, PROP_KNOCKBACK);
    setEntityClass(EntityRitualWindwall.class);
  }

  @Override
  public void init() {
    recipe = new RitualRecipe(this,
        new ItemStack(ModItems.moonglow_leaf),
        new OreIngredient("stonebrick"),
        new OreIngredient("rootsBark"),
        new OreIngredient("rootsBark"),
        new OreIngredient("allFlowers")
    );
    setIcon(ModItems.ritual_windwall);
    setColor(TextFormatting.DARK_AQUA);
    addCondition(new ConditionRunedPillars(RitualUtil.RunedWoodType.SPRUCE, 4, 1));
    addCondition(new ConditionStandingStones(4, 1));
  }

  @Override
  public void doFinalise() {
    duration = properties.get(PROP_DURATION);
    int[] radius = properties.getRadius();
    radius_x = radius[0] + 0.5;
    radius_y = radius[1] + 0.5;
    radius_z = radius[2] + 0.5;
    interval = properties.get(PROP_INTERVAL);
    distance = (int) Math.pow(properties.get(PROP_DISTANCE), 2);
    knockback = properties.get(PROP_KNOCKBACK);
  }
}