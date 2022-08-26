package epicsquid.roots.block;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.block.BlockBase;
import epicsquid.roots.api.CustomPlantType;
import epicsquid.roots.item.itemblock.ItemBlockElementalSoil;
import epicsquid.roots.network.fx.ElementalSoilTransformFX;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.util.EnumElementalSoilType;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockElementalSoil extends BlockBase {
  private final @Nonnull
  Item itemBlock;
  private final EnumElementalSoilType soilType;

  public BlockElementalSoil(@Nonnull Material mat, @Nonnull SoundType type, @Nonnull String name, @Nonnull EnumElementalSoilType soilType) {
    super(mat, type, 0.8f, name);
    this.soilType = soilType;
    this.itemBlock = new ItemBlockElementalSoil(this).setRegistryName(LibRegistry.getActiveModid(), name);
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

  @SideOnly(Side.CLIENT)
  @Override
  public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    if (soilType == EnumElementalSoilType.FIRE && rand.nextInt(5) == 0) {
      double d8 = pos.getX() + (double) rand.nextFloat();
      double d4 = pos.getY() + stateIn.getBoundingBox(worldIn, pos).maxY - 0.2f;
      double d6 = pos.getZ() + (double) rand.nextFloat();
      worldIn.spawnParticle(EnumParticleTypes.LAVA, d8, d4, d6, 0.0D, 0.0D, 0.0D);
    }

    if (soilType == EnumElementalSoilType.WATER) {
      for (int i = 0; i < 2; i++) {
        double x_offset = Math.min(Math.max(0.05, rand.nextDouble()), 0.95);
        double y_offset = Math.min(Math.max(0.05, rand.nextDouble()), 0.95);

        double d0 = (double) pos.getX() + x_offset;
        double d1 = (double) pos.getY() + 1D;
        double d2 = (double) pos.getZ() + y_offset;
        worldIn.spawnParticle(EnumParticleTypes.WATER_SPLASH, d0, d1, d2, 0, 1.5D, 0);
      }
    }

    if (soilType == EnumElementalSoilType.AIR && rand.nextInt(13) == 0) {
      for (int i = 0; i < 2; i++) {
        double x_offset = Math.min(Math.max(0.05, rand.nextDouble()), 0.95);
        double y_offset = Math.min(Math.max(0.05, rand.nextDouble()), 0.95);

        double d0 = (double) pos.getX() + x_offset;
        double d1 = (double) pos.getY() + 0.5D;
        double d2 = (double) pos.getZ() + y_offset;

        worldIn.spawnParticle(EnumParticleTypes.CLOUD, d0, d1, d2, 0.0D, 0.05D, 0.0D);
      }
    }

    if (soilType == EnumElementalSoilType.EARTH && rand.nextInt(6) == 0) {
      for (int i = 0; i < 2 + rand.nextInt(3); i++) {
        double x_offset = Math.min(Math.max(0.05, rand.nextDouble()), 0.95);
        double y_offset = Math.min(Math.max(0.05, rand.nextDouble()), 0.95);

        double d0 = (double) pos.getX() + x_offset;
        double d1 = (double) pos.getY() + 1D;
        double d2 = (double) pos.getZ() + y_offset;
        worldIn.spawnParticle(EnumParticleTypes.BLOCK_CRACK, d0, d1, d2, 0, 1D, 0, Block.getStateId(stateIn));
      }
    }
  }

  @Override
  public Item getItemBlock() {
    return itemBlock;
  }
}

