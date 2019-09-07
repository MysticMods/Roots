package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualGathering;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.recipe.conditions.ConditionStandingStones;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

public class RitualGathering extends RitualBase {

  public RitualGathering(String name, int duration, boolean disabled) {
    super(name, duration, disabled);
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
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualGathering.class);
  }
}
