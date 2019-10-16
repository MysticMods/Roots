package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualPurity;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.ritual.conditions.ConditionItems;
import epicsquid.roots.util.types.Property;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class RitualPurity extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(1200);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 15);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15);
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(20);
  public static Property<Integer> PROP_ZOMBIE_COUNT = new Property<>("zombie_count", 1);

  public double radius_x, radius_y, radius_z;
  public int interval, zombie_count;


  public RitualPurity(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_INTERVAL, PROP_ZOMBIE_COUNT);
  }

  @Override
  public void init () {
    addCondition(new ConditionItems(
        new ItemStack(ModItems.terra_moss),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine),
        new ItemStack(ModBlocks.baffle_cap_mushroom),
        new ItemStack(Items.MILK_BUCKET),
        new ItemStack(Items.GLASS_BOTTLE)
    ));
    setIcon(ModItems.ritual_purity);
    setColor(TextFormatting.LIGHT_PURPLE);
    setBold(true);
  }

  @Override
  public void doFinalise() {
    duration = properties.getProperty(PROP_DURATION);
    int[] radius = properties.getRadius();
    radius_x = radius[0] + 0.5;
    radius_y = radius[1] + 0.5;
    radius_z = radius[2] + 0.5;
    interval = properties.getProperty(PROP_INTERVAL);
    zombie_count = properties.getProperty(PROP_ZOMBIE_COUNT);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualPurity.class);
  }
}
