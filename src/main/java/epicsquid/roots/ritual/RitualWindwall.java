package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualWindwall;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.ritual.conditions.ConditionItems;
import epicsquid.roots.util.types.Property;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class RitualWindwall extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(3000);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 31);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 31);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 31);
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(5);
  public static Property<Integer> PROP_DISTANCE = new Property<>("distance", 31);
  public static Property<Float> PROP_KNOCKBACK = new Property<>("knockback", 1.0f);

  public double radius_x, radius_y, radius_z;
  public int interval, distance;
  public float knockback;

  public RitualWindwall(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_INTERVAL, PROP_DISTANCE, PROP_KNOCKBACK);
  }

  @Override
  public void init () {
    addCondition(new ConditionItems(
        new ItemStack(ModItems.cloud_berry),
        new ItemStack(ModItems.moonglow_leaf),
        new ItemStack(ModItems.bark_spruce),
        new ItemStack(ModItems.bark_birch),
        new ItemStack(Items.FEATHER)
    ));
    setIcon(ModItems.ritual_windwall);
    setColor(TextFormatting.DARK_AQUA);
  }

  @Override
  public void doFinalise() {
    duration = properties.getProperty(PROP_DURATION);
    int[] radius = properties.getRadius();
    radius_x = radius[0] + 0.5;
    radius_y = radius[1] + 0.5;
    radius_z = radius[2] + 0.5;
    interval = properties.getProperty(PROP_INTERVAL);
    distance = (int) Math.pow(properties.getProperty(PROP_DISTANCE), 2);
    knockback = properties.getProperty(PROP_KNOCKBACK);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualWindwall.class);
  }
}