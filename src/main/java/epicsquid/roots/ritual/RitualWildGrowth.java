package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualWildGrowth;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.util.types.Property;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

public class RitualWildGrowth extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(300);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 10);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 20);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 10);
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(250);

  public int radius_x, radius_y, radius_z, interval;

  public RitualWildGrowth(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_INTERVAL);
  }

  @Override
  public void init() {
    addCondition(new ConditionItems(
        new ItemStack(ModItems.wildroot),
        new OreIngredient("rootsBark"),
        new OreIngredient("rootsBark"),
        new ItemStack(ModItems.bark_dark_oak),
        new ItemStack(ModItems.spirit_herb))
    );
    setIcon(ModItems.ritual_wild_growth);
    setColor(TextFormatting.DARK_GRAY);
    setBold(true);
  }

  @Override
  public void doFinalise() {
    duration = properties.getProperty(PROP_DURATION);
    int[] radius = properties.getRadius();
    radius_x = radius[0];
    radius_y = radius[1];
    radius_z = radius[2];
    interval = properties.getProperty(PROP_INTERVAL);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualWildGrowth.class);
  }
}
