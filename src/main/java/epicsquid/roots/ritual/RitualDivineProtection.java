package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualDivineProtection;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.recipe.conditions.ConditionWorldTime;
import epicsquid.roots.util.types.Property;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class RitualDivineProtection extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(1200);
  public static Property<Boolean> PROP_RAIN = new Property<>("rain", true);
  public static Property<Boolean> PROP_TIME = new Property<>("time", true);
  public static Property<Integer> PROP_NIGHT_REDUCTION = new Property<>("night_reduction", 1);
  public static Property<Integer> PROP_DAY_EXTENSION = new Property<>("day_extension", 3);
  public static Property<Float> PROP_CONSECRATION_DAMAGE = new Property<>("consecration_damage", 4.0f);
  public static Property<Float> PROP_FIRE_DAMAGE = new Property<>("fire_damage", 4.0f);
  public static Property<Integer> PROP_FIRE_DURATION = new Property<>("fire_duration", 2);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 15);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15);
  public static Property<Integer> PROP_DAY_LENGTH = new Property<>("day_length", 24000);
  public static Property<Integer> PROP_NIGHT_THRESHOLD = new Property<>("night_threshold", 12000);

  public boolean rain, time;
  public float radius_x, radius_y, radius_z, consecration_damage, fire_damage;
  public int night_reduction, day_extension, fire_duration, day_length, night_threshold;

  public RitualDivineProtection(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_RAIN, PROP_TIME, PROP_NIGHT_REDUCTION, PROP_DAY_EXTENSION, PROP_CONSECRATION_DAMAGE, PROP_FIRE_DAMAGE, PROP_FIRE_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_DAY_LENGTH, PROP_NIGHT_THRESHOLD);
  }

  @Override
  public void init () {
    addCondition(new ConditionItems(
        new ItemStack(ModItems.pereskia),
        new ItemStack(ModItems.cloud_berry),
        new ItemStack(ModItems.bark_birch),
        new ItemStack(ModItems.bark_oak),
        new ItemStack(Items.GLOWSTONE_DUST)
    ));
    setIcon(ModItems.ritual_divine_protection);
    setColor(TextFormatting.YELLOW);
    setBold(true);
  }

  @Override
  public void finalise() {
    duration = properties.getProperty(PROP_DURATION);
    rain = properties.getProperty(PROP_RAIN);
    time = properties.getProperty(PROP_TIME);
    night_reduction = properties.getProperty(PROP_NIGHT_REDUCTION);
    day_extension = properties.getProperty(PROP_DAY_EXTENSION);
    consecration_damage = properties.getProperty(PROP_CONSECRATION_DAMAGE);
    fire_damage = properties.getProperty(PROP_FIRE_DAMAGE);
    fire_duration = properties.getProperty(PROP_FIRE_DURATION);
    int[] radius = properties.getRadius();
    radius_x = radius[0] + 0.5f;
    radius_y = radius[1] + 0.5f;
    radius_z = radius[2] + 0.5f;
    day_length = properties.getProperty(PROP_DAY_LENGTH);
    night_threshold = properties.getProperty(PROP_NIGHT_THRESHOLD);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualDivineProtection.class);
  }
}