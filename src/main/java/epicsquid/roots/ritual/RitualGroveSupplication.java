package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualGroveSupplication;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.ritual.conditions.ConditionItems;
import epicsquid.roots.util.types.Property;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class RitualGroveSupplication extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(120);
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(100).setDescription("interval in ticks between each generated piece of generated flora");

  public int interval;

  public RitualGroveSupplication(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_INTERVAL);
    setEntityClass(EntityRitualGroveSupplication.class);
  }

  @Override
  public void init() {
    addCondition(new ConditionItems(
        new OreIngredient("doorWood"),
        new ItemStack(Blocks.MOSSY_COBBLESTONE),
        new OreIngredient("treeSapling"),
        new OreIngredient("wildroot"),
        new ItemStack(ModItems.petals)
    ));
    setIcon(ModItems.ritual_grove_supplication);
    setColor(TextFormatting.YELLOW);
    setBold(true);
  }

  @Override
  public void doFinalise() {
    duration = properties.get(PROP_DURATION);
    interval = properties.get(PROP_INTERVAL);
  }
}