package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualPurity;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.ritual.conditions.ConditionRunedPillars;
import epicsquid.roots.ritual.conditions.ConditionStandingStones;
import epicsquid.roots.util.RitualUtil;
import epicsquid.roots.properties.Property;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class RitualPurity extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(1200);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15).setDescription("Radius on the X Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 15).setDescription("Radius on the Y Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15).setDescription("Radius on the Z Axis of the cube in which the ritual takes place");
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(20).setDescription("interval in ticks between each conversion boost");
  public static Property<Integer> PROP_ZOMBIE_COUNT = new Property<>("zombie_count", 1).setDescription("the rate at which the zombie villager is converted back to a villager");

  public double radius_x, radius_y, radius_z;
  public int interval, zombie_count;


  public RitualPurity(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_INTERVAL, PROP_ZOMBIE_COUNT);
    setEntityClass(EntityRitualPurity.class);
  }

  @Override
  public void init () {
    recipe = new RitualRecipe(this,
        new ItemStack(ModItems.terra_moss),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine),
        new ItemStack(ModBlocks.baffle_cap_mushroom),
        new ItemStack(Items.MILK_BUCKET),
        new ItemStack(Items.GLASS_BOTTLE)
    );
    addCondition(new ConditionRunedPillars(RitualUtil.RunedWoodType.OAK, 3, 2));
    addCondition(new ConditionStandingStones(3, 1));
    setIcon(ModItems.ritual_purity);
    setColor(TextFormatting.LIGHT_PURPLE);
    setBold(true);
  }

  @Override
  public void doFinalise() {
    duration = properties.get(PROP_DURATION);
    int[] radius = properties.getRadius();
    radius_x = radius[0] + 0.5;
    radius_y = radius[1] + 0.5;
    radius_z = radius[2] + 0.5;
    interval = properties.get(PROP_INTERVAL);
    zombie_count = properties.get(PROP_ZOMBIE_COUNT);
  }
}
