package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualGathering;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.ritual.conditions.ConditionItems;
import epicsquid.roots.ritual.conditions.ConditionStandingStones;
import epicsquid.roots.util.types.Property;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

public class RitualGathering extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(6000);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 15);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15);
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(80);

  public int radius_x, radius_y, radius_z, interval;

  public RitualGathering(String name, boolean disabled) {
    super(name, disabled);
    this.properties.addProperties(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_INTERVAL);
  }

  @Override
  public void init () {
    addCondition(new ConditionItems(
        new ItemStack(ModItems.wildewheet),
        new ItemStack(ModItems.wildewheet),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine_seed),
        new OreIngredient("ingotIron"),
        new OreIngredient("dustRedstone")
    ));
    addCondition(new ConditionStandingStones(3, 1));
    setIcon(ModItems.ritual_gathering);
    setColor(TextFormatting.YELLOW);
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
    return this.spawnEntity(world, pos, EntityRitualGathering.class);
  }
}
