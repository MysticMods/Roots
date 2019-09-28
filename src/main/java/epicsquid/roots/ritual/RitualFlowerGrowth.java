package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualFlowerGrowth;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.BlockRedFlower;
import net.minecraft.block.BlockYellowFlower;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class RitualFlowerGrowth extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(3200);
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(100);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 10);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 0);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 10);

  public int interval, radius_x, radius_y, radius_z;

  public RitualFlowerGrowth(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_INTERVAL, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z);
  }

  @Override
  public void init () {
    addCondition(new ConditionItems(
        new ItemStack(Blocks.YELLOW_FLOWER, 1, BlockYellowFlower.EnumFlowerType.DANDELION.getMeta()),
        new ItemStack(ModItems.wildroot),
        new ItemStack(ModItems.terra_moss),
        new ItemStack(ModItems.petals),
        new ItemStack(Blocks.RED_FLOWER, 1, BlockRedFlower.EnumFlowerType.POPPY.getMeta())
    ));

    setIcon(ModItems.ritual_flower_growth);
    setColor(TextFormatting.LIGHT_PURPLE);
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
    return this.spawnEntity(world, pos, EntityRitualFlowerGrowth.class);
  }
}
