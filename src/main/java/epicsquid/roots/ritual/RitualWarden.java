package epicsquid.roots.ritual;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.EntityRitualWarden;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualWarden extends RitualBase {

  public RitualWarden(String name, int duration, boolean doUpdateValidity) {
    super(name, duration, doUpdateValidity);
    addIngredient(new ItemStack(Items.DYE, 1, 15));
    addIngredient(new ItemStack(ModItems.pereskia_bulb, 1));
    addIngredient(new ItemStack(ModItems.spirit_herb, 1));
    addIngredient(new ItemStack(ModItems.bark_oak, 1));
    addIngredient(new ItemStack(Items.IRON_CHESTPLATE, 1));
  }

  @Override
  public boolean isValidForPos(World world, BlockPos pos) {
    int threeHighCount = 0;
    int fourHighCount = 0;
    for (int i = -6; i < 7; i++) {
      for (int j = -6; j < 7; j++) {
        IBlockState state = world.getBlockState(pos.add(i, 3, j));
        if (state.getBlock() == ModBlocks.chiseled_runestone) {
          if (world.getBlockState(pos.add(i, 2, j)).getBlock() == ModBlocks.runestone && world.getBlockState(pos.add(i, 1, j)).getBlock() == ModBlocks.runestone
              && world.getBlockState(pos.add(i, 0, j)).getBlock() == ModBlocks.runestone) {
            fourHighCount++;
          }
        } else {
          state = world.getBlockState(pos.add(i, 2, j));
          if (state.getBlock() == ModBlocks.chiseled_runestone) {
            if (world.getBlockState(pos.add(i, 1, j)).getBlock() == ModBlocks.runestone
                && world.getBlockState(pos.add(i, 0, j)).getBlock() == ModBlocks.runestone) {
              threeHighCount++;
            }
          }
        }
      }
    }
    return threeHighCount == 3 && fourHighCount == 3;
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualWarden.class);
  }
}