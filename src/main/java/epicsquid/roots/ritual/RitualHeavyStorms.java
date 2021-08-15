package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualHeavyStorms;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.properties.Property;
import epicsquid.roots.ritual.conditions.ConditionRunedPillars;
import epicsquid.roots.ritual.conditions.ConditionStandingStones;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class RitualHeavyStorms extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(2400);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15).setDescription("Radius on the X Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 15).setDescription("Radius on the Y Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15).setDescription("Radius on the Z Axis of the cube in which the ritual takes place");
  public static Property<Float> PROP_LIGHTNING_CHANCE = new Property<>("lightning_chance", 0.1f).setDescription("Chance (per interval of 1 second) of the ritual duration for lightning to strike in the ritual area");
  public static Property<Integer> PROP_MAX_STRIKES = new Property<>("lightning_interval", 10).setDescription("Maximum number of lightning strikes that can happen per ritual. (Set to -1 for infinite)");

  public double radius_x, radius_y, radius_z;
  public int interval, max_strikes;
  public float lightning_chance;

  public RitualHeavyStorms(String name, boolean disabled) {
    super(name, disabled);
    properties.add(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_LIGHTNING_CHANCE, PROP_MAX_STRIKES);
    setEntityClass(EntityRitualHeavyStorms.class);
  }

  @Override
  public void init() {
    recipe = new RitualRecipe(this,
        new ItemStack(Blocks.WATERLILY),
        new ItemStack(ModItems.dewgonia),
        new OreIngredient("vine"),
        new ItemStack(ModItems.cloud_berry),
        new ItemStack(Items.SNOWBALL)
    );
    addCondition(new ConditionStandingStones(3, 1));
    addCondition(new ConditionRunedPillars(RitualUtil.RunedWoodType.DARK_OAK, 4, 1));
    setIcon(ModItems.ritual_heavy_storms);
    setColor(TextFormatting.DARK_AQUA);
    setBold(true);
  }

  @Override
  public void doFinalise() {
    duration = properties.get(PROP_DURATION);
    int[] radius = properties.getRadius();
    radius_x = radius[0] + 0.5;
    radius_y = radius[1] + 0.5;
    radius_z = radius[2] + 0.5;
    lightning_chance = properties.get(PROP_LIGHTNING_CHANCE);
    max_strikes = properties.get(PROP_MAX_STRIKES);
  }
}