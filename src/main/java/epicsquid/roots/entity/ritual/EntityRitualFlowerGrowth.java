package epicsquid.roots.entity.ritual;

import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.FlowerRecipe;
import epicsquid.roots.ritual.RitualFlowerGrowth;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityRitualFlowerGrowth extends EntityRitualBase {
  private RitualFlowerGrowth ritual;

  public EntityRitualFlowerGrowth(World worldIn) {
    super(worldIn);
    getDataManager().register(lifetime, RitualRegistry.ritual_flower_growth.getDuration() + 20);
    this.ritual = (RitualFlowerGrowth) RitualRegistry.ritual_flower_growth;
  }

  @Override
  public void onUpdate() {
    super.onUpdate();

    if (this.ticksExisted % ritual.interval == 0) {
      BlockPos topBlockPos = RitualUtil.getRandomPosRadial(new BlockPos(getPosition().getX(), getPosition().getY() - ritual.radius_y, getPosition().getZ()), ritual.radius_x, ritual.radius_z);
      while (!generateFlower(topBlockPos)) {
        topBlockPos = topBlockPos.up();
        if (topBlockPos.getY() > 256)
          break;
      }
    }
  }

  private boolean generateFlower(BlockPos pos) {
    IBlockState flower = getRandomFlower();
    if (world.isAirBlock(pos) && flower.getBlock().canPlaceBlockAt(world, pos)) {
      if (!world.isRemote) {
        world.setBlockState(pos, flower);
      }
      return true;
    }
    return false;
  }

  private IBlockState getRandomFlower() {
    FlowerRecipe recipe = ModRecipes.getRandomFlowerRecipe();
    if (recipe == null) return Blocks.YELLOW_FLOWER.getStateFromMeta(BlockFlower.EnumFlowerType.DANDELION.getMeta());

    IBlockState state = recipe.getFlower();
    if (state == null) return Blocks.AIR.getDefaultState();

    return state;
  }
}
