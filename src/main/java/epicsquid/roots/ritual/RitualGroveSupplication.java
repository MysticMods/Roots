package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualGroveSupplication;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.util.types.Property;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class RitualGroveSupplication extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(120);
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(100).setDescription("delay from ritual start until activation of groves");
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15).setDescription("X range of the ritual");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 10).setDescription("Y range of the ritual");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15).setDescription("Z range of the ritual");

  public int interval, radius_x, radius_y, radius_z;

  public RitualGroveSupplication(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_INTERVAL);
    setEntityClass(EntityRitualGroveSupplication.class);
  }

  @Override
  public void init() {
    recipe = new RitualRecipe(this,
        new OreIngredient("doorWood"),
        new ItemStack(Blocks.MOSSY_COBBLESTONE),
        new OreIngredient("treeSapling"),
        new OreIngredient("wildroot"),
        new ItemStack(ModItems.petals)
    );
    setIcon(ModItems.ritual_grove_supplication);
    setColor(TextFormatting.YELLOW);
    setBold(true);
  }

  @Override
  public void doFinalise() {
    duration = properties.get(PROP_DURATION);
    interval = properties.get(PROP_INTERVAL);
    int r[] = properties.getRadius();
    radius_x = r[0];
    radius_y = r[1];
    radius_z = r[2];
  }
}