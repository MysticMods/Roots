package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualRegrowth;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualRegrowth extends RitualBase {
  public RitualRegrowth(String name, int duration) {
    super(name, duration);
    addCondition(new ConditionItems(
            new ItemStack(ModItems.terra_moss),
            new ItemStack(ModItems.spirit_herb),
            new ItemStack(ModItems.bark_spruce),
            new ItemStack(Blocks.SAPLING), 
            new ItemStack(Blocks.SAPLING)
    ));
    setIcon(ModItems.ritual_regrowth);
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualRegrowth.class);
  }
}