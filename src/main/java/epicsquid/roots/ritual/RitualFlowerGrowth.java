package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualFlowerGrowth;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.ritual.RitualBase;
import net.minecraft.block.BlockRedFlower;
import net.minecraft.block.BlockYellowFlower;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class RitualFlowerGrowth extends RitualBase {

  public RitualFlowerGrowth(String name, int duration, boolean disabled) {
    super(name, duration, disabled);
    addCondition(new ConditionItems(
        new ItemStack(Blocks.YELLOW_FLOWER, 1, BlockYellowFlower.EnumFlowerType.DANDELION.getMeta()),
        new ItemStack(ModItems.wildroot),
        new ItemStack(ModItems.terra_moss),
        new ItemStack(ModItems.petals),
        new ItemStack(Blocks.RED_FLOWER, 1, BlockRedFlower.EnumFlowerType.POPPY.getMeta())
    ));

    //addCondition(new ConditionWorldTime(0, 13000));
    setIcon(ModItems.ritual_flower_growth);
    setColor(TextFormatting.LIGHT_PURPLE);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualFlowerGrowth.class);
  }
}
