package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualHealingAura;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.ritual.conditions.ConditionItems;
import epicsquid.roots.ritual.conditions.ConditionStandingStones;
import epicsquid.roots.ritual.conditions.ConditionTrees;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class RitualHealingAura extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(800);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15);
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 15);
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15);
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(60);
  public static Property<Float> PROP_AMOUNT = new Property<>("amount", 1.0f);

  public double radius_x, radius_y, radius_z;
  public float amount;
  public int interval;

  public RitualHealingAura(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_INTERVAL, PROP_AMOUNT);
    setEntityClass(EntityRitualHealingAura.class);
  }

  @Override
  public void init() {
    addCondition(new ConditionItems(
        new ItemStack(ModItems.terra_moss),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine),
        new ItemStack(ModItems.bark_birch),
        new ItemStack(ModItems.wildroot),
        new ItemStack(Blocks.SAPLING, 1, 2)
    ));
    addCondition(new ConditionStandingStones(3, 1));
    addCondition(new ConditionTrees(BlockPlanks.EnumType.BIRCH, 1));
    setIcon(ModItems.ritual_healing_aura);
    setColor(TextFormatting.GOLD);
  }

  @Override
  public void doFinalise() {
    duration = properties.getProperty(PROP_DURATION);
    int[] radius = properties.getRadius();
    radius_x = radius[0] + 0.5;
    radius_y = radius[1] + 0.5;
    radius_z = radius[2] + 0.5;
    amount = properties.getProperty(PROP_AMOUNT);
    interval = properties.getProperty(PROP_INTERVAL);
  }
}