package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualWildrootGrowth;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.util.types.Property;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class RitualWildrootGrowth extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(300);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 10).setDescription("Radius on the X Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 20).setDescription("Radius on the Y Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 10).setDescription("Radius on the Z Axis of the cube in which the ritual takes place");
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(250).setDescription("interval in ticks between each wildwood tree growth");

  public int radius_x, radius_y, radius_z, interval;

  public RitualWildrootGrowth(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_INTERVAL);
    setEntityClass(EntityRitualWildrootGrowth.class);
  }

  @Override
  public void init() {
    recipe = new RitualRecipe(this,
        new OreIngredient("wildroot"),
        new ItemStack(ModItems.bark_dark_oak),
        new OreIngredient("rootsBark"),
        new OreIngredient("rootsBark"),
        new ItemStack(ModItems.spirit_herb)
    );
    setIcon(ModItems.ritual_wildroot_growth);
    setColor(TextFormatting.DARK_GRAY);
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
  }
}
