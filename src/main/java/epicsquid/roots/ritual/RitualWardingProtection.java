package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualWardingProtection;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.properties.Property;
import epicsquid.roots.ritual.conditions.ConditionStandingStones;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class RitualWardingProtection extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(1200);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15).setDescription("Radius on the X Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 15).setDescription("Radius on the Y Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15).setDescription("Radius on the Z Axis of the cube in which the ritual takes place");
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(20).setDescription("interval in ticks between each time you get provided the invulnerability effect");
  public static Property<Integer> PROP_INVULN_DURATION = new Property<>("invuln_duration", 22).setDescription("duration in ticks of the invulnerability effect");

  public double radius_x, radius_y, radius_z;
  public int interval, invuln_duration;

  public RitualWardingProtection(String name, boolean disabled) {
    super(name, disabled);
    properties.add(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_INTERVAL, PROP_INVULN_DURATION);
    setEntityClass(EntityRitualWardingProtection.class);
  }

  @Override
  public void init() {
    recipe = new RitualRecipe(this,
        new ItemStack(Items.SPECKLED_MELON),
        new ItemStack(ModItems.stalicripe),
        new OreIngredient("wildroot"),
        new OreIngredient("rootsBark"),
        new ItemStack(Items.SHIELD)
    );
    addCondition(new ConditionStandingStones(3, 2));
    addCondition(new ConditionStandingStones(4, 2));
    setIcon(ModItems.ritual_warding_protection);
    setColor(TextFormatting.DARK_BLUE);
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
    invuln_duration = properties.get(PROP_INVULN_DURATION);
  }
}