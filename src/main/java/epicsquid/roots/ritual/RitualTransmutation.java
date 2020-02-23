package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualTransmutation;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.ritual.conditions.ConditionItems;
import epicsquid.roots.ritual.conditions.ConditionStandingStones;
import epicsquid.roots.util.types.Property;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class RitualTransmutation extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(2400);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 8);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 8);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 8);
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(100);

  public int radius_x, radius_y, radius_z, interval;

  public RitualTransmutation(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_INTERVAL, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z);
    setEntityClass(EntityRitualTransmutation.class);
  }

  @Override
  public void init () {
    addCondition(
        new ConditionItems(
            new ItemStack(Blocks.FURNACE),
            new ItemStack(Blocks.MOSSY_COBBLESTONE),
            new ItemStack(ModItems.cloud_berry),
            new OreIngredient("rootsBark"),
            new ItemStack(ModBlocks.chiseled_runestone)));
    addCondition(new ConditionStandingStones(3, 1));
    setIcon(ModItems.ritual_transmutation);
    setColor(TextFormatting.DARK_PURPLE);
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
