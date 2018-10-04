package epicsquid.roots.ritual;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.ritual.EntityRitualLife;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualLife extends RitualBase {
  public RitualLife(String name, int duration, boolean doUpdateValidity) {
    super(name, duration, doUpdateValidity);
    addIngredients(
        new ItemStack(ModItems.terra_moss, 1),
        new ItemStack(ModItems.bark_oak, 1),
        new ItemStack(ModItems.bark_birch, 1),
        new ItemStack(ModItems.bark_birch, 1),
        new ItemStack(Blocks.SAPLING, 1, 2)
    );
  }

  @Override
  public boolean isValidForPos(World world, BlockPos pos) {
    int birchTreeCount = 0;
    for (int i = -9; i < 10; i++) {
      for (int j = -9; j < 10; j++) {
        IBlockState state = world.getBlockState(pos.add(i, 1, j));
        if (state.getBlock() instanceof BlockOldLog) {
          if (state.getValue(BlockOldLog.VARIANT) == EnumType.BIRCH) {
            birchTreeCount++;
          }
        }
      }
    }
    return birchTreeCount >= 4;
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    this.spawnEntity(world, pos, EntityRitualLife.class);
  }
}