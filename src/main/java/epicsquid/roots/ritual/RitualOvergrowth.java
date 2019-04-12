package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualOvergrowth;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualOvergrowth extends RitualBase {

  public RitualOvergrowth(String name, int duration) {
    super(name, duration);
    addCondition(
        new ConditionItems(
            new ItemStack(Items.REEDS),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(Blocks.TALLGRASS, 1, 1),
            new ItemStack(ModItems.bark_oak),
            new ItemStack(ModItems.bark_oak)));
    setIcon(ModItems.ritual_overgrowth);
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualOvergrowth.class);
  }

}
