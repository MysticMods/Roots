package epicsquid.roots.entity.ritual;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.mc1120.item.MCItemStack;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.FlowerRecipe;
import epicsquid.roots.ritual.RitualFlowerGrowth;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Collections;
import java.util.List;

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

    List<IIngredient> allowedSoils = Collections.emptyList();
    IBlockState flower = Blocks.YELLOW_FLOWER.getStateFromMeta(BlockFlower.EnumFlowerType.DANDELION.getMeta());
    FlowerRecipe recipe = ModRecipes.getRandomFlowerRecipe();
    if (recipe != null) {
      flower = recipe.getFlower();
      allowedSoils = recipe.getAllowedSoils();
    }

    if (!flower.getBlock().canPlaceBlockAt(world, pos)) {
      return false;
    }

    if (allowedSoils.size() > 0) {
      // Only use whitelisting logic if there's a provided whitelist.
      if (world.isAirBlock(pos.down())) {
        // It is invalid to create an ItemStack of air, so don't even try.
        return false;
      }
      IBlockState blockState = world.getBlockState(pos.down());
      Block block = blockState.getBlock();
      IItemStack soil = new MCItemStack(new ItemStack(block, 1, block.getMetaFromState(blockState)));
      boolean found = false;
      for (IIngredient allowedSoil : allowedSoils) {
        if (allowedSoil.contains(soil)) {
          found = true;
          break;
        }
      }
      if (!found) {
        return false;
      }
    }

    if (!world.isRemote) {
      world.setBlockState(pos, flower);
    }

    return true;
  }
}
