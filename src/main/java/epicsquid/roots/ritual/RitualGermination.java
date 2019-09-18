package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualGermination;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.recipe.conditions.ConditionStandingStones;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

public class RitualGermination extends RitualBase {

  public RitualGermination(String name, int duration, boolean disabled) {
    super(name, disabled);
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

  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualGermination.class);
  }
}
