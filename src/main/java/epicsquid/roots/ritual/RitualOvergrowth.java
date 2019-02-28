package epicsquid.roots.ritual;

import epicsquid.mysticallib.particle.particles.ParticleGlitter;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.recipe.conditions.ConditionItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
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
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    // Effect has a 19 Block radius around the pyre
    for (int i = -19; i < 20; i++) {
      for (int j = -19; j < 20; j++) {
        // Attempt to overgrow it
        BlockPos topBlockPos = new BlockPos(pos.getX() + i, pos.getY() - 1, pos.getZ() + j);
        overgrowBlock(world, topBlockPos, world.getBlockState(topBlockPos));
      }
    }
  }

  /**
   * Has all valid possibilities for changing the block from a normal block to an overgrown one.
   * TODO add a way for crafttweaker to do this
   */
  private void overgrowBlock(World world, BlockPos pos, IBlockState state) {
    if (state.getBlock() == Blocks.COBBLESTONE && isAdjacentToWater(world, pos)) {
      world.setBlockState(pos, Blocks.MOSSY_COBBLESTONE.getDefaultState());

      if (world.isRemote) {
        for (int i = 0; i < 50; i++) {
          ClientProxy.particleRenderer
              .spawnParticle(world, Util.getLowercaseClassName(ParticleGlitter.class), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                  random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1),
                  random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), 120, 0.607, 0.698 + random.nextDouble() * 0.05, 0.306, 1,
                  random.nextDouble() + 0.5, random.nextDouble() * 2);
        }
      }
    }
  }

  /**
   * Checks if the given block has water adjacent to it
   *
   * @return True if at least one side is touching a water source block
   */
  private boolean isAdjacentToWater(World world, BlockPos pos) {
    return world.getBlockState(pos.offset(EnumFacing.EAST)).getBlock() == Blocks.WATER
        || world.getBlockState(pos.offset(EnumFacing.NORTH)).getBlock() == Blocks.WATER
        || world.getBlockState(pos.offset(EnumFacing.WEST)).getBlock() == Blocks.WATER
        || world.getBlockState(pos.offset(EnumFacing.SOUTH)).getBlock() == Blocks.WATER;
  }

}
