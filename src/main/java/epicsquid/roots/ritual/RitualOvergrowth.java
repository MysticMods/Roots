package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualOvergrowth;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.ritual.conditions.ConditionItems;
import epicsquid.roots.util.types.Property;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

public class RitualOvergrowth extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(3000);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 10);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 20);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 10);
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(100);

  public int radius_x, radius_y, radius_z, interval;

  public RitualOvergrowth(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_INTERVAL);
  }

  @Override
  public void init () {
    addCondition(
        new ConditionItems(
            new ItemStack(Items.REEDS),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(Blocks.TALLGRASS, 1, 1),
            new OreIngredient("rootsBark"),
            new OreIngredient("rootsBark")));
    setIcon(ModItems.ritual_overgrowth);
    setColor(TextFormatting.DARK_GREEN);
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
    return this.spawnEntity(world, pos, EntityRitualOvergrowth.class);
  }

}
