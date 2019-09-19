package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualAnimalHarvest;
import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.recipe.conditions.ConditionStandingStones;
import epicsquid.roots.util.types.Property;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

public class RitualAnimalHarvest extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(3200);
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(110);
  public static Property<Integer> PROP_COUNT = new Property<>("count", 5);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 10);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15);
  public static Property<Integer> PROP_GLOW_DURATION = new Property<>("glowing", 30);
  public static Property<Integer> PROP_LOOTING_CHANCE = new Property<>("looting_chance", 6);
  public static Property<Integer> PROP_LOOTING_VALUE = new Property<>("looting_value", 1);

  public int interval;
  public int count;
  public int radius_x, radius_y, radius_z;
  public int looting_chance, looting_value;
  public int glowing;

  public RitualAnimalHarvest(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_INTERVAL, PROP_COUNT, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_LOOTING_CHANCE, PROP_LOOTING_VALUE, PROP_GLOW_DURATION);
  }

  @Override
  public void init () {
    addCondition(new ConditionItems(
        new ItemStack(ModItems.wildewheet),
        new OreIngredient("blockWool"),
        new ItemStack(Items.MELON),
        new ItemStack(Items.CARROT),
        new ItemStack(ModItems.wildroot)
    ));
    addCondition(new ConditionStandingStones(3, 3));
    setIcon(ModItems.ritual_animal_harvest);
    setColor(TextFormatting.GOLD);
    setBold(true);
  }

  @Override
  public void finalise() {
    duration = properties.getProperty(PROP_DURATION);
    interval = properties.getProperty(PROP_INTERVAL);
    int[] radius = properties.getRadius();
    radius_x = radius[0];
    radius_y = radius[1];
    radius_z = radius[2];
    looting_chance = properties.getProperty(PROP_LOOTING_CHANCE);
    looting_value = properties.getProperty(PROP_LOOTING_VALUE);
    glowing = properties.getProperty(PROP_GLOW_DURATION);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualAnimalHarvest.class);
  }


}
