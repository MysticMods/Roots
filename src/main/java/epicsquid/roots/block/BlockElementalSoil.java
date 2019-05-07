package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockBase;
import epicsquid.roots.api.CustomPlantType;
import epicsquid.roots.util.EnumElementalSoilType;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockElementalSoil extends BlockBase {

  private final EnumElementalSoilType soilType;

  public BlockElementalSoil(@Nonnull Material mat, @Nonnull SoundType type, @Nonnull String name, @Nonnull EnumElementalSoilType soilType) {
    super(mat, type, 0.8f, name);
    this.soilType = soilType;
  }

  @Override
  public boolean canSustainPlant(@Nonnull IBlockState state, @Nonnull IBlockAccess world, BlockPos pos, @Nonnull EnumFacing direction, IPlantable plantable) {
    EnumPlantType plant = plantable.getPlantType(world, pos.offset(direction));
    switch (plant) {
      case Nether:
      case Cave:
      case Crop:
      case Desert:
      case Plains:
        return true;
    }
    return plant == CustomPlantType.ELEMENT_FIRE && soilType == EnumElementalSoilType.FIRE
        || plant == CustomPlantType.ELEMENT_AIR && soilType == EnumElementalSoilType.AIR
        || plant == CustomPlantType.ELEMENT_EARTH && soilType == EnumElementalSoilType.EARTH
        || plant == CustomPlantType.ELEMENT_WATER && soilType == EnumElementalSoilType.WATER;
  }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
    super.updateTick(world, pos, state, rand);

    if (world.isAirBlock(pos.up())) return;

    Block upBlock = world.getBlockState(pos.up()).getBlock();
    if (!(upBlock instanceof IGrowable)) return;

    // TODO: Who knows if this value is any good
    if (rand.nextInt(5) == 0) {
      upBlock.randomTick(world, pos.up(), state, rand);
    }
  }
}

