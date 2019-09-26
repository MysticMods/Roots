package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualGermination;
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

public class RitualGermination extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(6400);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 19);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 19);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 19);
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(80);
  public static Property<Integer> PROP_COUNT = new Property<>("count", 5);
  public static Property<Integer> PROP_TICKS = new Property<>("ticks", 3);
  public static Property<Integer> PROP_BONUS_TICKS = new Property<>("bonus_ticks", 0);
  public static Property<Integer> PROP_BLOCK_CROP_TICKS = new Property<>("block_crop_ticks", 15);

  public int radius_x, radius_y, radius_z, interval;
  public int count, ticks, bonus_ticks, crop_ticks;

  public RitualGermination(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_INTERVAL, PROP_COUNT, PROP_TICKS, PROP_BONUS_TICKS, PROP_BLOCK_CROP_TICKS);
  }

  @Override
  public void init () {
    addCondition(new ConditionItems(
        new ItemStack(ModItems.spirit_herb),
        new ItemStack(ModItems.wildroot),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine),
        new ItemStack(Items.DYE, 1, 15),
        new OreIngredient("rootsBark")
    ));
    addCondition(new ConditionStandingStones(3, 2));
    setIcon(ModItems.ritual_germination);
    setColor(TextFormatting.DARK_RED);
    setBold(true);
  }

  @Override
  public void finalise() {
    int[] radius = properties.getRadius();
    radius_x = radius[0];
    radius_y = radius[1];
    radius_z = radius[2];
    interval = properties.getProperty(PROP_INTERVAL);
    count = properties.getProperty(PROP_COUNT);
    ticks = properties.getProperty(PROP_TICKS);
    bonus_ticks = properties.getProperty(PROP_BONUS_TICKS);
    crop_ticks = properties.getProperty(PROP_BLOCK_CROP_TICKS);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualGermination.class);
  }
}
