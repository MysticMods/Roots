package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualFrostLands;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.properties.Property;
import epicsquid.roots.ritual.conditions.ConditionStandingStones;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

public class RitualFrostLands extends RitualBase implements IColdRitual {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(6400);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 10).setDescription("Radius on the X Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 10).setDescription("Radius on the Y Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 10).setDescription("Radius on the Z Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_INTERVAL_HEAL = new Property<>("interval_snowman_heal", 30).setDescription("number of TICKS between each snowman healing pulse (every pulse heals all snowmen completely)");
  public static Property<Integer> PROP_INTERVAL_SPAWN = new Property<>("interval_snowman_spawn", 150).setDescription("chance every tick of spawning a snowman in the radius of the ritual (the higher the value is the lower the chance becomes) [default: 1/150]");

  public int interval_heal, interval_spawn;
  public int radius_x, radius_y, radius_z;

  public RitualFrostLands(String name, boolean disabled) {
    super(name, disabled);
    this.properties.add(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_INTERVAL_HEAL, PROP_INTERVAL_SPAWN);
    setEntityClass(EntityRitualFrostLands.class);
  }

  @Override
  public void init() {
    recipe = new RitualRecipe(this,
        new OreIngredient("sugarcane"),
        new ItemStack(ModItems.dewgonia),
        new OreIngredient("vine"),
        new ItemStack(ModItems.bark_spruce),
        new ItemStack(ModItems.bark_spruce)
    );
    setIcon(ModItems.ritual_frost_lands);
    setColor(TextFormatting.AQUA);
    setBold(true);
  }

  @Override
  public void doFinalise() {
    duration = properties.get(PROP_DURATION);
    int[] radius = properties.getRadius();
    radius_x = radius[0];
    radius_y = radius[1];
    radius_z = radius[2];
    interval_heal = properties.get(PROP_INTERVAL_HEAL);
    interval_spawn = properties.get(PROP_INTERVAL_SPAWN);
  }
}
