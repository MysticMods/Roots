package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualHeavyStorms;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.ritual.conditions.ConditionItems;
import epicsquid.roots.util.types.Property;
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
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(20);

  public double radius_x, radius_y, radius_z;
  public int interval;

  public RitualHeavyStorms(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_INTERVAL);
    setEntityClass(EntityRitualHeavyStorms.class);
  }

  @Override
  public void init() {
    addCondition(new ConditionItems(
        new ItemStack(Blocks.WATERLILY),
        new ItemStack(ModItems.dewgonia),
        new OreIngredient("vine"),
        new ItemStack(ModItems.cloud_berry),
        new ItemStack(Items.BEETROOT_SEEDS)
    ));
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
    interval = properties.get(PROP_INTERVAL);
  }
}