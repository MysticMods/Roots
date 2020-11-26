package epicsquid.roots.entity.ritual;

import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.ritual.RitualFlowerGrowth;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityRitualFlowerGrowth extends EntityRitualBase {
  private final RitualFlowerGrowth ritual;

  public EntityRitualFlowerGrowth(World worldIn) {
    super(worldIn);
    getDataManager().register(lifetime, RitualRegistry.ritual_flower_growth.getDuration() + 20);
    this.ritual = (RitualFlowerGrowth) RitualRegistry.ritual_flower_growth;
  }

  @Override
  public void onUpdate() {
    super.onUpdate();

    if (this.ticksExisted % ritual.interval == 0) {
      BlockPos baseOfRandomColumn = RitualUtil.getRandomPosRadialXZ(
              getPosition().down(ritual.radius_y), ritual.radius_x, ritual.radius_z);
      // If there are multiple "platforms", we want an upper platform block to be a candidate for planting
      // even if the lower platform block below it isn't, so start at a random offset.
      int blocksToScan = ritual.radius_y * 2 + 1;
      int offset = RitualUtil.getRandomInteger(0, blocksToScan);
      for (int i = 0; i < blocksToScan; i++) {
        int height = (offset + i) % blocksToScan;
        if (generateFlower(baseOfRandomColumn.up(height))) {
          break;
        }
      }
    }
  }

  private boolean generateFlower(BlockPos pos) {
    if (!world.isAirBlock(pos)) {
      return false;
    }

    IBlockState flower = ModRecipes.getRandomFlowerRecipe(world.getBlockState(pos.down()));

    if (!flower.getBlock().canPlaceBlockAt(world, pos)) {
      return false;
    }

    if (!world.isRemote) {
      world.setBlockState(pos, flower);
    }

    return true;
  }
}
