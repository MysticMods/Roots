package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualSpreadingForest;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.util.types.Property;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

public class RitualSpreadingForest extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(2400);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 35);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 30);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 35);
  public static Property<Integer> PROP_PLACE_INTERVAL = new Property<>("place_interval", 30);
  public static Property<Integer> PROP_GROWTH_INTERVAL = new Property<>("growth_interval", 20);
  public static Property<Integer> PROP_DOUBLE_CHANCE = new Property<>("double_chance", 20);

  public int radius_x, radius_y, radius_z, place_interval, growth_interval, double_chance;

  public RitualSpreadingForest(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_PLACE_INTERVAL, PROP_GROWTH_INTERVAL, PROP_DOUBLE_CHANCE);
  }

  @Override
  public void init () {
    addCondition(new ConditionItems(
        new ItemStack(ModItems.terra_moss),
        new ItemStack(ModItems.spirit_herb),
        new OreIngredient("rootsBark"),
        new OreIngredient("treeSapling"),
        new OreIngredient("treeSapling")
    ));
    setIcon(ModItems.ritual_spreading_forest);
    setColor(TextFormatting.GREEN);
    setBold(true);
  }

  @Override
  public void finalise() {
    int[] radius = properties.getRadius();
    radius_x = radius[0];
    radius_y = radius[1];
    radius_z = radius[2];
    place_interval = properties.getProperty(PROP_PLACE_INTERVAL);
    growth_interval = properties.getProperty(PROP_GROWTH_INTERVAL);
    double_chance = properties.getProperty(PROP_DOUBLE_CHANCE);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualSpreadingForest.class);
  }
}