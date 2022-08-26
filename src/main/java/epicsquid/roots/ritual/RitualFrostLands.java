package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualFrostLands;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.ritual.conditions.ConditionItems;
import epicsquid.roots.util.types.Property;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class RitualFrostLands extends RitualBase implements IColdRitual {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(6400);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 10);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 10);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 10);
  public static Property<Integer> PROP_INTERVAL_HEAL = new Property<>("interval_snowman_heal", 30);
  public static Property<Integer> PROP_INTERVAL_SPAWN = new Property<>("interval_snowman_spawn", 150);

  public int interval_heal, interval_spawn;
  public int radius_x, radius_y, radius_z;

  public RitualFrostLands(String name, boolean disabled) {
    super(name, disabled);
    this.properties.addProperties(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_INTERVAL_HEAL, PROP_INTERVAL_SPAWN);
  }

  @Override
  public void init () {
    addCondition(new ConditionItems(
        new ItemStack(Items.SNOWBALL),
        new ItemStack(ModItems.dewgonia),
        new ItemStack(Blocks.SNOW),
        new ItemStack(ModItems.bark_spruce),
        new ItemStack(ModItems.bark_spruce)
    ));
    setIcon(ModItems.ritual_frost_lands);
    setColor(TextFormatting.AQUA);
    setBold(true);
  }

  @Override
  public void doFinalise() {
    duration = properties.getProperty(PROP_DURATION);
    int[] radius = properties.getRadius();
    radius_x = radius[0];
    radius_y = radius[1];
    radius_z = radius[2];
    interval_heal = properties.getProperty(PROP_INTERVAL_HEAL);
    interval_spawn = properties.getProperty(PROP_INTERVAL_SPAWN);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualFrostLands.class);
  }

}
