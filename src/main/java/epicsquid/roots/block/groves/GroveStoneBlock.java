package epicsquid.roots.block.groves;

import epicsquid.mysticallib.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class GroveStoneBlock extends Block {
  public static final EnumProperty<Half> HALF = EnumProperty.create("half", Half.class);
  public static final DirectionProperty FACING = DirectionProperty.create("facing", (facing) -> facing == Direction.NORTH || facing == Direction.EAST);
  public static final BooleanProperty VALID = BooleanProperty.create("valid");

  public GroveStoneBlock(Properties properties) {
    super(properties);
  }

/*  public GroveStoneBlock(@Nonnull String name) {
    super(Material.ROCK, SoundType.STONE, 2.5f, name);

    this.setDefaultState(this.blockState.getBaseState().withProperty(VALID, false).withProperty(HALF, Half.BOTTOM).withProperty(FACING, Direction.NORTH));
    this.setTickRandomly(true);

    TODO:
    useNeighborBrightness = true;
  }*/

  @Nonnull
  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }

  // TODO: Handle placement and states

/*  @Override
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
  }*/

/*  @Override
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
  }*/

  // TODO: Handle clearing of accessors/multiblocks

/*  @Override
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
  }*/

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


  // TODO: Override correctly
  //@Override
  public void randomTick(World world, BlockPos pos, BlockState state, Random random) {
    //super.randomTick(world, pos, state, random);

    /*    if (!GeneralConfig.EnableGroveStoneEnvironment) return;*/

    if (world.isRemote) return;

    if (!state.get(VALID)) return;

    if (random.nextInt(10 /*GeneralConfig.GroveStoneChance*/) == 1) {
      int effectsCount = 1 + random.nextInt(1);

      List<BlockPos> positions = Util.getBlocksWithinRadius(world, pos.down(), 4, 5, 4, (p) -> {
        if (world.isAirBlock(p.up())) {
          BlockState s = world.getBlockState(p);
          return s.getMaterial() == Material.EARTH;
        }
        return false;
      });

      Collections.shuffle(positions);

      for (BlockPos p : positions) {
        if (effectsCount <= 0) break;

        BlockState s = world.getBlockState(p);
        Block b = s.getBlock();
        // TODO: Improve this somehow
/*        if (s.getMaterial() == Material.EARTH && world.isAirBlock(p.up().up())) {
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
          PacketHandler.sendToAllTracking(message, world, p.up());*/
        effectsCount--;
      }
    }
  }
}

/*  @Override
  @OnlyIn(Dist.CLIENT)
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
  }*/
