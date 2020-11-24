package epicsquid.roots.entity.ritual;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.mc1120.item.MCItemStack;
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
      BlockPos topBlockPos = RitualUtil.getRandomPosRadialXZ(new BlockPos(getPosition().getX(), getPosition().getY() - ritual.radius_y, getPosition().getZ()), ritual.radius_x, ritual.radius_z);
      while (!generateFlower(topBlockPos)) {
        topBlockPos = topBlockPos.up();
        // TODO: Make bounds configurable
        if (topBlockPos.getY() > 256 || Math.abs(topBlockPos.getY() - posY) < 20)
          break;
      }
    }
  }

  private boolean generateFlower(BlockPos pos) {
    if (!world.isAirBlock(pos)) {
      return false;
    }

    List<IIngredient> allowedSoils = Collections.emptyList();
    // If the user removed all recipes, we'll fall back to making dandelions.
    IBlockState flower = Blocks.YELLOW_FLOWER.getStateFromMeta(BlockFlower.EnumFlowerType.DANDELION.getMeta());

    // Ideally, we'd iterate through all recipes in a random order and try to use each.
    // That would allow us to place one flower per iteration more reliably instead of trying a random recipe
    // at every point in a giant column.
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
        // Can't create an item stack of air.
        return false;
      }
      IBlockState blockState = world.getBlockState(pos.down());
      Block block = blockState.getBlock();

      IItemStack soil = new MCItemStack(new ItemStack(block, block.getMetaFromState(blockState)));
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
