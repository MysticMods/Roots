package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualDivineProtection;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.properties.Property;
import epicsquid.roots.ritual.conditions.ConditionStandingStones;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class RitualDivineProtection extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(1200);
  public static Property<Boolean> PROP_RAIN = new Property<>("rain", true).setDescription("whether or not rain should be disabled");
  public static Property<Boolean> PROP_TIME = new Property<>("time", true).setDescription("whether or not time should be accelerated or slowed");
  public static Property<Integer> PROP_NIGHT_REDUCTION = new Property<>("night_reduction", 1).setDescription("the number of additional ticks (per tick) added to night");
  public static Property<Float> PROP_DAY_EXTENSION = new Property<>("day_extension", 0.3f).setDescription("the chance per tick that ticks will be subtracted, lengthening the day");
  public static Property<Float> PROP_CONSECRATION_DAMAGE = new Property<>("consecration_damage", 4.0f).setDescription("damage done to undead creatures if Consecration is installed");
  public static Property<Float> PROP_FIRE_DAMAGE = new Property<>("fire_damage", 4.0f).setDescription("amount of fire damage done to undead creatures");
  public static Property<Integer> PROP_FIRE_DURATION = new Property<>("fire_duration", 2).setDescription("duration in SECONDS undead creatures will be set on fire for");
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15).setDescription("Radius on the X Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 15).setDescription("Radius on the Y Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15).setDescription("Radius on the Z Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_DAY_LENGTH = new Property<>("day_length", 24000).setDescription("the length of the day for use in calculating addition/subtraction (modify if you have mods that adjust day/night length");
  public static Property<Integer> PROP_NIGHT_THRESHOLD = new Property<>("night_threshold", 12000).setDescription("the point at which day transitions into night (modify if you have mods that adjust day/night length");

  public boolean rain, time;
  public float radius_x, radius_y, radius_z, consecration_damage, fire_damage;
  public int night_reduction, fire_duration, day_length, night_threshold;
  public float day_extension;

  public RitualDivineProtection(String name, boolean disabled) {
    super(name, disabled);
    properties.add(PROP_DURATION, PROP_RAIN, PROP_TIME, PROP_NIGHT_REDUCTION, PROP_DAY_EXTENSION, PROP_CONSECRATION_DAMAGE, PROP_FIRE_DAMAGE, PROP_FIRE_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_DAY_LENGTH, PROP_NIGHT_THRESHOLD);
    setEntityClass(EntityRitualDivineProtection.class);
  }

  @Override
  public void init() {
    recipe = new RitualRecipe(this,
        new ItemStack(ModItems.pereskia),
        new ItemStack(ModItems.cloud_berry),
        new OreIngredient("rootsBark"),
        new ItemStack(ModItems.bark_oak),
        new OreIngredient("dustGlowstone")
    );
    addCondition(new ConditionStandingStones(3, 1));
    setIcon(ModItems.ritual_divine_protection);
    setColor(TextFormatting.YELLOW);
    setBold(true);
  }

  @Override
  public void doFinalise() {
    duration = properties.get(PROP_DURATION);
    rain = properties.get(PROP_RAIN);
    time = properties.get(PROP_TIME);
    night_reduction = properties.get(PROP_NIGHT_REDUCTION);
    day_extension = properties.get(PROP_DAY_EXTENSION);
    consecration_damage = properties.get(PROP_CONSECRATION_DAMAGE);
    fire_damage = properties.get(PROP_FIRE_DAMAGE);
    fire_duration = properties.get(PROP_FIRE_DURATION);
    int[] radius = properties.getRadius();
    radius_x = radius[0] + 0.5f;
    radius_y = radius[1] + 0.5f;
    radius_z = radius[2] + 0.5f;
    day_length = properties.get(PROP_DAY_LENGTH);
    night_threshold = properties.get(PROP_NIGHT_THRESHOLD);
  }
}