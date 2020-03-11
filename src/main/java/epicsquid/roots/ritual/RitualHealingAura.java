package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualHealingAura;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.ritual.conditions.ConditionRunedPillars;
import epicsquid.roots.ritual.conditions.ConditionStandingStones;
import epicsquid.roots.util.RitualUtil;
import epicsquid.roots.util.types.Property;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class RitualHealingAura extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(800);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15).setDescription("Radius on the X Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 15).setDescription("Radius on the Y Axis of the cube in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15).setDescription("Radius on the Z Axis of the cube in which the ritual takes place");
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(60).setDescription("interval in ticks between each ritual healing pulse");
  public static Property<Float> PROP_AMOUNT = new Property<>("amount", 1.0f).setDescription("the amount of life points an entity is healed of each ritual pulse");

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
    recipe = new RitualRecipe(this,
        new ItemStack(ModItems.terra_moss),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine),
        new ItemStack(ModItems.bark_birch),
        new OreIngredient("wildroot"),
        new ItemStack(Blocks.SAPLING, 1, 2)
    );
    addCondition(new ConditionStandingStones(3, 1));
    addCondition(new ConditionRunedPillars(RitualUtil.RunedWoodType.BIRCH, 3, 1));
    setIcon(ModItems.ritual_healing_aura);
    setColor(TextFormatting.GOLD);
  }

  @Override
  public void doFinalise() {
    duration = properties.get(PROP_DURATION);
    int[] radius = properties.getRadius();
    radius_x = radius[0] + 0.5;
    radius_y = radius[1] + 0.5;
    radius_z = radius[2] + 0.5;
    amount = properties.get(PROP_AMOUNT);
    interval = properties.get(PROP_INTERVAL);
  }
}