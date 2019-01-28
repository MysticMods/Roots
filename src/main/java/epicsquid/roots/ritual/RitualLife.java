package epicsquid.roots.ritual;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.ritual.EntityRitualLife;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualLife extends RitualBase {

  public RitualLife(String name, int duration) {
    super(name, duration);
    addIngredients(
            new ItemStack(ModItems.terra_moss), 
            new ItemStack(ModItems.bark_oak), 
            new ItemStack(ModItems.bark_birch),
            new ItemStack(ModItems.wildroot), 
            new ItemStack(Blocks.SAPLING, 1, 2)
    );
  }

  @Override
  public boolean canFire(World world, BlockPos pos, EntityPlayer player) {
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