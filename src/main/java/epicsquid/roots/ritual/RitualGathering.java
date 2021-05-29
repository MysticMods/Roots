package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualGathering;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.properties.Property;
import epicsquid.roots.ritual.conditions.ConditionStandingStones;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class RitualGathering extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(6000);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15).setDescription("Radius on the X Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 15).setDescription("Radius on the Y Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15).setDescription("Radius on the Z Axis of the cube in which the ritual takes place");
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(80).setDescription("interval in ticks between each time the ritual tries to gather thaumcraft.items on the ground");

  public int radius_x, radius_y, radius_z, interval;

  public RitualGathering(String name, boolean disabled) {
    super(name, disabled);
    this.properties.add(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_INTERVAL);
    setEntityClass(EntityRitualGathering.class);
  }

  @Override
  public void init() {
    recipe = new RitualRecipe(this,
        new OreIngredient("slimeball"),
        new ItemStack(ModItems.wildewheet),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine_seed),
        new OreIngredient("ingotIron"),
        new OreIngredient("dustRedstone")
    );
    setIcon(ModItems.ritual_gathering);
    setColor(TextFormatting.YELLOW);
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
