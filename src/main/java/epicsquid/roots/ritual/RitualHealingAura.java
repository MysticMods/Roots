package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualHealingAura;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.recipe.conditions.ConditionStandingStones;
import epicsquid.roots.recipe.conditions.ConditionTrees;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class RitualHealingAura extends RitualBase {

  public RitualHealingAura(String name, int duration, boolean disabled) {
    super(name, duration, disabled);
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
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualHealingAura.class);
  }
}