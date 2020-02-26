package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualOvergrowth;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.ritual.conditions.ConditionItems;
import epicsquid.roots.util.types.Property;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class RitualOvergrowth extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(3000);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 10).setDescription("Radius on the X Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 20).setDescription("Radius on the Y Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 10).setDescription("Radius on the Z Axis of the cube in which the ritual takes place");
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(100);

  public int radius_x, radius_y, radius_z, interval;

  public RitualOvergrowth(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_INTERVAL);
    setEntityClass(EntityRitualOvergrowth.class);
  }

  @Override
  public void init () {
    addCondition(
        new ConditionItems(
            new OreIngredient("sugarcane"),
            new ItemStack(ModItems.terra_moss),
            new OreIngredient("grass"),
            new OreIngredient("rootsBark"),
            new OreIngredient("rootsBark")));
    setIcon(ModItems.ritual_overgrowth);
    setColor(TextFormatting.DARK_GREEN);
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
