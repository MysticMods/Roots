package epicsquid.roots.block;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.block.BlockBase;
import epicsquid.mysticalworld.api.CustomPlantType;
import epicsquid.mysticalworld.util.EnumRunicSoilType;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class BlockRunicSoil extends BlockBase {

  private final EnumRunicSoilType soilType;

  public BlockRunicSoil(@Nonnull Material mat, @Nonnull SoundType type, @Nonnull String name, @Nonnull EnumRunicSoilType soilType) {
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
    return plant == CustomPlantType.ELEMENT_FIRE && soilType == EnumRunicSoilType.FIRE
        || plant == CustomPlantType.ELEMENT_AIR && soilType == EnumRunicSoilType.AIR
        || plant == CustomPlantType.ELEMENT_EARTH && soilType == EnumRunicSoilType.EARTH
        || plant == CustomPlantType.ELEMENT_WATER && soilType == EnumRunicSoilType.WATER;
  }
}

