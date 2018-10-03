package epicsquid.roots.ritual;

import epicsquid.roots.init.ModBlocks;
import epicsquid.mysticalworld.init.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualFrost extends RitualBase {

  public RitualFrost(String name, int duration, boolean doUpdateValidity) {
    super(name, duration, doUpdateValidity);
    addIngredient(new ItemStack(Items.SNOWBALL, 1));
    addIngredient(new ItemStack(Blocks.PACKED_ICE, 1));
    addIngredient(new ItemStack(Blocks.SNOW, 1));
    addIngredient(new ItemStack(ModItems.bark_acacia, 1));
    addIngredient(new ItemStack(ModItems.bark_acacia, 1));
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    super.doEffect(world, pos);

    for (int i = -19; i < 20; i++) {
      for (int j = -19; j < 20; j++) {
        BlockPos topBlockPos = world.getTopSolidOrLiquidBlock(pos.add(i, 0, j));
        if(world.getBlockState(topBlockPos).getBlock() == Blocks.WATER){
          world.setBlockState(topBlockPos, Blocks.ICE.getDefaultState());
        }
        else{
          if(!world.getBlockState(topBlockPos).getBlock().isLeaves(world.getBlockState(topBlockPos), world, topBlockPos)
              && world.getBlockState(topBlockPos.add(0, -1, 0)).getBlock() != ModBlocks.bonfire
              && world.getBlockState(topBlockPos.add(0, -1, 0)).getBlock() != ModBlocks.mortar){
            world.setBlockState(topBlockPos, Blocks.SNOW_LAYER.getDefaultState());
          }
        }
      }
    }

    //todo: add nice particle effects in the area
  }

}
