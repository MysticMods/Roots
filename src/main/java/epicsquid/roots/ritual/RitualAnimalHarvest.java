package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualAnimalHarvest;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.properties.Property;
import epicsquid.roots.ritual.conditions.ConditionStandingStones;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

public class RitualAnimalHarvest extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(3200);
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(110).setDescription("the number of ticks between each ritual harvest");
  public static Property<Integer> PROP_COUNT = new Property<>("count", 5).setDescription("the number of times harvesting will be performed per interval");
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15).setDescription("Radius on the X Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 10).setDescription("Radius on the X Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15).setDescription("Radius on the X Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_GLOW_DURATION = new Property<>("glowing", 30).setDescription("duration (in ticks) mobs will glow after being harvested");
  public static Property<Integer> PROP_LOOTING_CHANCE = new Property<>("looting_chance", 6).setDescription("chance (1 in X) that the the looting chance will be set to looting_value");
  public static Property<Integer> PROP_LOOTING_VALUE = new Property<>("looting_value", 1).setDescription("the value passed to the loot function for 'looting' if looting_chance was successful");
  public static Property<Integer> PROP_FISH_CHANCE = new Property<>("fish_chance", 5).setDescription("chance (1 in X) that, if suitable water source thaumcraft.blocks are found, fish will be produced instead of animal drops being produced");
  public static Property<Integer> PROP_FISH_COUNT = new Property<>("fish_count", 3).setDescription("number of fish that are guaranteed to drop");
  public static Property<Integer> PROP_FISH_ADDITIONAL = new Property<>("fish_additional", 3).setDescription("additional fish (random 0-X)-2 that are produced if fish are produced");

  public int interval;
  public int count;
  public int radius_x, radius_y, radius_z;
  public int looting_chance, looting_value;
  public int glowing;
  public int fish_chance, fish_count, fish_additional;

  public RitualAnimalHarvest(String name, boolean disabled) {
    super(name, disabled);
    properties.add(PROP_DURATION, PROP_INTERVAL, PROP_COUNT, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_LOOTING_CHANCE, PROP_LOOTING_VALUE, PROP_GLOW_DURATION, PROP_FISH_CHANCE, PROP_FISH_COUNT, PROP_FISH_ADDITIONAL);
    setEntityClass(EntityRitualAnimalHarvest.class);
  }

  @Override
  public void init() {
    recipe = new RitualRecipe(this,
        new ItemStack(ModItems.wildewheet),
        new OreIngredient("wool"),
        new OreIngredient("cropCarrot"),
        new OreIngredient("slimeball"),
        new OreIngredient("wildroot")
    );
    addCondition(new ConditionStandingStones(3, 3));
    setIcon(ModItems.ritual_animal_harvest);
    setColor(TextFormatting.GOLD);
    setBold(true);
  }

  @Override
  public void doFinalise() {
    duration = properties.get(PROP_DURATION);
    interval = properties.get(PROP_INTERVAL);
    int[] radius = properties.getRadius();
    radius_x = radius[0];
    radius_y = radius[1];
    radius_z = radius[2];
    looting_chance = properties.get(PROP_LOOTING_CHANCE);
    looting_value = properties.get(PROP_LOOTING_VALUE);
    glowing = properties.get(PROP_GLOW_DURATION);
    fish_chance = properties.get(PROP_FISH_CHANCE);
    fish_count = properties.get(PROP_FISH_COUNT);
    fish_additional = properties.get(PROP_FISH_ADDITIONAL);
    count = properties.get(PROP_COUNT);
  }
}
