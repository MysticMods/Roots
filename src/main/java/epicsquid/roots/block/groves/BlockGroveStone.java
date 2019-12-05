package epicsquid.roots.block.groves;

import epicsquid.mysticallib.block.BlockBase;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.particle.particles.ParticleLeafArc;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.network.fx.MessageOvergrowthEffectFX;
import net.minecraft.block.*;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.TallGrassBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class BlockGroveStone extends BlockBase {
  public static final PropertyEnum<Half> HALF = PropertyEnum.create("half", Half.class);
  public static final PropertyDirection FACING = PropertyDirection.create("facing", (facing) -> facing == Direction.NORTH || facing == Direction.EAST);
  public static final PropertyBool VALID = PropertyBool.create("valid");

  public BlockGroveStone(@Nonnull String name) {
    super(Material.ROCK, SoundType.STONE, 2.5f, name);

    this.setDefaultState(this.blockState.getBaseState().withProperty(VALID, false).withProperty(HALF, Half.BOTTOM).withProperty(FACING, Direction.NORTH));
    this.setTickRandomly(true);
    useNeighborBrightness = true;
  }

  @Nonnull
  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }

  @Override
  public boolean isFullCube(@Nonnull BlockState state) {
    return false;
  }

  @Override
  public boolean isOpaqueCube(@Nonnull BlockState state) {
    return false;
  }

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(@Nonnull BlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
    if (state.getValue(FACING) == Direction.NORTH) {
      if (state.getValue(HALF) == Half.TOP) {
        return new AxisAlignedBB(0.15, 0, 0.2, 0.85, 0.7, 0.8);
      } else /*if (state.getValue(HALF) == Half.BOTTOM)*/ {
        return new AxisAlignedBB(0.15, 0, 0.2, 0.85, 1, 0.8);
      }
    } else {
      if (state.getValue(HALF) == Half.TOP) {
        return new AxisAlignedBB(0.2, 0, 0.15, 0.8, 0.7, 0.85);
      } else /*if (state.getValue(HALF) == Half.BOTTOM)*/ {
        return new AxisAlignedBB(0.2, 0, 0.15, 0.8, 1, 0.85);
      }
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(VALID, (meta & 1) == 1).withProperty(HALF, Half.fromInt((meta & 7) >> 1)).withProperty(FACING, (meta >> 3) == 0 ? Direction.NORTH : Direction.EAST);
  }

  @Override
  public int getMetaFromState(BlockState state) {
    int meta = (((state.getValue(FACING) == Direction.NORTH ? 0 : 1) << 2 ^ state.getValue(HALF).ordinal())) << 1 ^ (state.getValue(VALID) ? 1 : 0);
    return meta;
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, HALF, VALID, FACING);
  }

  @Override
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    if (worldIn.getBlockState(pos.down()).getBlock() == this) return false;

    BlockState up = worldIn.getBlockState(pos.up());
    BlockState upup = worldIn.getBlockState(pos.up().up());

    return up.getBlock() != this && upup.getBlock() != this && super.canPlaceBlockAt(worldIn, pos) && up.getBlock().isReplaceable(worldIn, pos.up()) && upup.getBlock().isReplaceable(worldIn, pos.up().up());
  }

  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    worldIn.setBlockState(pos.up(), this.getDefaultState().withProperty(HALF, Half.MIDDLE).withProperty(FACING, state.getValue(FACING)));
    worldIn.setBlockState(pos.up().up(), this.getDefaultState().withProperty(HALF, Half.TOP).withProperty(FACING, state.getValue(FACING)));
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockState getStateForPlacement(World worldIn, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer) {
    // Work out the facing
    Direction f = Direction.fromAngle(placer.rotationYaw).getOpposite();
    if (f == Direction.NORTH || f == Direction.SOUTH || f == Direction.DOWN || f == Direction.UP) {
      f = Direction.NORTH;
    } else {
      f = Direction.EAST;
    }
    return this.getDefaultState().withProperty(HALF, Half.BOTTOM).withProperty(FACING, f);
  }

  @Override
  public void breakBlock(World worldIn, BlockPos pos, BlockState state) {
    super.breakBlock(worldIn, pos, state);

    BlockState down = worldIn.getBlockState(pos.down());
    BlockState downdown = worldIn.getBlockState(pos.down().down());
    BlockState up = worldIn.getBlockState(pos.up());
    BlockState upup = worldIn.getBlockState(pos.up().up());

    if (down.getBlock() == this) {
      worldIn.setBlockToAir(pos.down());
    }
    if (downdown.getBlock() == this) {
      worldIn.setBlockToAir(pos.down().down());
    }
    if (up.getBlock() == this) {
      worldIn.setBlockToAir(pos.up());
    }
    if (upup.getBlock() == this) {
      worldIn.setBlockToAir(pos.up());
    }
  }

  @Override
  public int damageDropped(BlockState state) {
    return 0;
  }

  public enum Half implements IStringSerializable {
    TOP,
    MIDDLE,
    BOTTOM;

    @Override
    public String getName() {
      switch (this) {
        case TOP:
          return "top";
        case MIDDLE:
          return "middle";
        default:
          return "bottom";
      }
    }

    public static Half fromInt(int x) {
      for (Half half : values()) {
        if (half.ordinal() == x) {
          return half;
        }
      }
      return BOTTOM;
    }
  }

  @Override
  public void randomTick(World world, BlockPos pos, BlockState state, Random random) {
    super.randomTick(world, pos, state, random);

    if (!GeneralConfig.EnableGroveStoneEnvironment) return;

    if (world.isRemote) return;

    if (!state.getValue(VALID)) return;

    if (random.nextInt(GeneralConfig.GroveStoneChance) == 1) {
      int effectsCount = 1 + random.nextInt(1);

      List<BlockPos> positions = Util.getBlocksWithinRadius(world, pos.down(), 4, 5, 4, (p) -> {
        if (world.isAirBlock(p.up())) {
          BlockState s = world.getBlockState(p);
          return s.getMaterial() == Material.GRASS;
        }
        return false;
      });

      Collections.shuffle(positions);

      for (BlockPos p : positions) {
        if (effectsCount <= 0) break;

        BlockState s = world.getBlockState(p);
        Block b = s.getBlock();
        // TODO: Improve this somehow
        if (s.getMaterial() == Material.GRASS && world.isAirBlock(p.up().up())) {
          switch (random.nextInt(50)) {
            case 0:
              if (b.canSustainPlant(s, world, p, Direction.UP, Blocks.DOUBLE_PLANT)) {
                Blocks.DOUBLE_PLANT.placeAt(world, p.up(), DoublePlantBlock.EnumPlantType.ROSE, 3);
                break;
              }
            case 1:
              if (b.canSustainPlant(s, world, p, Direction.UP, Blocks.DOUBLE_PLANT)) {
                Blocks.DOUBLE_PLANT.placeAt(world, p.up(), DoublePlantBlock.EnumPlantType.SUNFLOWER, 3);
                break;
              }
            case 2:
            case 3:
              if (b.canSustainPlant(s, world, p, Direction.UP, Blocks.DOUBLE_PLANT)) {
                Blocks.DOUBLE_PLANT.placeAt(world, p.up(), DoublePlantBlock.EnumPlantType.GRASS, 3);
                break;
              }
            case 4:
              if (b.canSustainPlant(s, world, p, Direction.UP, Blocks.DOUBLE_PLANT)) {
                Blocks.DOUBLE_PLANT.placeAt(world, p.up(), DoublePlantBlock.EnumPlantType.PAEONIA, 3);
                break;
              }
            case 5:
              if (b.canSustainPlant(s, world, p, Direction.UP, Blocks.DOUBLE_PLANT)) {
                Blocks.DOUBLE_PLANT.placeAt(world, p.up(), DoublePlantBlock.EnumPlantType.SYRINGA, 3);
                break;
              }
            default:
              if (b.canSustainPlant(s, world, p, Direction.UP, Blocks.TALLGRASS)) {
                world.setBlockState(p.up(), Blocks.TALLGRASS.getDefaultState().withProperty(TallGrassBlock.TYPE, TallGrassBlock.EnumType.GRASS), 3);
                break;
              }
              continue;
          }
          MessageOvergrowthEffectFX message = new MessageOvergrowthEffectFX(p.getX() + 0.5, p.getY() + 0.3, p.getZ() + 0.5);
          PacketHandler.sendToAllTracking(message, world, p.up());
          effectsCount--;
        }
      }
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    super.randomDisplayTick(stateIn, worldIn, pos, rand);

    if (stateIn.getValue(VALID) && stateIn.getValue(HALF) == Half.MIDDLE) {
      for (int i = -2; i <= 2; ++i) {
        for (int j = -2; j <= 2; ++j) {
          if (i > -2 && i < 2 && j == -1) {
            j = 2;
          }
          if (rand.nextInt(32) == 0) {
            for (int k = 0; k <= 1; ++k) {
              if (!worldIn.isAirBlock(pos.add(i / 2, 0, j / 2))) {
                break;
              }
              ClientProxy.particleRenderer.spawnParticle(worldIn, Util.getLowercaseClassName(ParticleLeafArc.class), (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, (i + rand.nextDouble() - 0.05) * 0.04, -0.0001, (j + rand.nextFloat() - 0.05) * 0.04,
                  100, (232 / 255.0) + rand.nextDouble() * 0.05, 167 / 255.0, 111 / 255.0, 0.385, 0.117 + rand.nextDouble() * 0.05, 1, rand.nextDouble() + 0.5, rand.nextDouble() * 2);

            }
          }
        }
      }
    }
  }

  @Override
  @Nonnull
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
    if (face == Direction.UP) {
      return BlockFaceShape.UNDEFINED;
    }
    return super.getBlockFaceShape(worldIn, state, pos, face);
  }
}
