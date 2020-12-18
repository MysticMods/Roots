package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualFlowerGrowth;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.properties.Property;
import net.minecraft.block.BlockRedFlower;
import net.minecraft.block.BlockYellowFlower;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class RitualFlowerGrowth extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(3200);
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(100).setDescription("interval in ticks between each generated flower");
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 10).setDescription("Radius on the X Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 10).setDescription("Radius on the Y Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 10).setDescription("Radius on the Z Axis of the cube in which the ritual takes place");

  public int interval, radius_x, radius_y, radius_z;

  public RitualFlowerGrowth(String name, boolean disabled) {
    super(name, disabled);
    properties.add(PROP_DURATION, PROP_INTERVAL, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z);
    setEntityClass(EntityRitualFlowerGrowth.class);
  }

  @Override
  public void init() {
    recipe = new RitualRecipe(this,
        new ItemStack(Blocks.YELLOW_FLOWER, 1, BlockYellowFlower.EnumFlowerType.DANDELION.getMeta()),
        new OreIngredient("wildroot"),
        new ItemStack(ModItems.terra_moss),
        new ItemStack(ModItems.petals),
        new ItemStack(Blocks.RED_FLOWER, 1, BlockRedFlower.EnumFlowerType.POPPY.getMeta())
    );

    setIcon(ModItems.ritual_flower_growth);
    setColor(TextFormatting.LIGHT_PURPLE);
  }

  @Override
  public void doFinalise() {
    duration = properties.get(PROP_DURATION);
    int[] radius = properties.getRadius();
    radius_x = radius[0];
    radius_y = radius[1];
    radius_z = radius[2];
    interval = properties.get(PROP_INTERVAL);
  }
}
