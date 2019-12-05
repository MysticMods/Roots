package epicsquid.roots.entity.ritual;

import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.FlowerRecipe;
import epicsquid.roots.ritual.RitualFlowerGrowth;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FlowerGrowthRitualEntity extends BaseRitualEntity {
  private RitualFlowerGrowth ritual;

  public FlowerGrowthRitualEntity(EntityType<?> entityTypeIn, World worldIn) {
    super(entityTypeIn, worldIn);
    this.ritual = (RitualFlowerGrowth) RitualRegistry.ritual_flower_growth;
  }

  @Override
  protected void registerData() {
    getDataManager().register(lifetime, RitualRegistry.ritual_flower_growth.getDuration() + 20);
  }

  @Override
  public void tick() {
    super.tick();

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
    BlockState flower = getRandomFlower();
    // TODO: What's the replacement for canPlaceBlockAt?
/*    if (world.isAirBlock(pos) && flower.getBlock().canPlaceBlockAt(world, pos)) {
      if (!world.isRemote) {
        world.setBlockState(pos, flower);
      }
      return true;
    }*/
    return false;
  }

  private BlockState getRandomFlower() {
    FlowerRecipe recipe = ModRecipes.getRandomFlowerRecipe();
    // TODO: Meta!
    //if (recipe == null) return Blocks.YELLOW_FLOWER.getStateFromMeta(FlowerBlock.FlowerType.DANDELION.getMeta());

    BlockState state = recipe.getFlower();
    if (state == null) return Blocks.AIR.getDefaultState();

    return state;
  }
}
