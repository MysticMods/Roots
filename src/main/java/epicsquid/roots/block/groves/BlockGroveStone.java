package epicsquid.roots.block.groves;

import epicsquid.roots.config.GeneralConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class BlockGroveStone extends Block {
  public static final EnumProperty<Half> HALF = EnumProperty.create("half", Half.class);
  public static final DirectionProperty FACING = DirectionProperty.create("facing", (facing) -> facing == Direction.NORTH || facing == Direction.EAST);
  public static final BooleanProperty VALID = BooleanProperty.create("valid");

  public BlockGroveStone(Properties props) {
    super(props);
    //super(Material.ROCK, SoundType.STONE, 2.5f, name);

    this.setDefaultState(this.getDefaultState().with(VALID, false).with(HALF, Half.BOTTOM).with(FACING, Direction.NORTH));
    // TODO: TICK RANDOMLY
    //useNeighborBrightness = true;
  }

  /*@Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(@Nonnull BlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
    if (state.get(FACING) == Direction.NORTH) {
      if (state.get(HALF) == Half.TOP) {
        return new AxisAlignedBB(0.15, 0, 0.2, 0.85, 0.7, 0.8);
      } else {
        return new AxisAlignedBB(0.15, 0, 0.2, 0.85, 1, 0.8);
      }
    } else {
      if (state.get(HALF) == Half.TOP) {
        return new AxisAlignedBB(0.2, 0, 0.15, 0.8, 0.7, 0.85);
      } else {
        return new AxisAlignedBB(0.2, 0, 0.15, 0.8, 1, 0.85);
      }
    }
  }*/

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(HALF, VALID, FACING);
  }

  // TODO: What did this become?

/*  @Override
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    if (worldIn.getBlockState(pos.down()).getBlock() == this) return false;

    BlockState up = worldIn.getBlockState(pos.up());
    BlockState upup = worldIn.getBlockState(pos.up().up());

    return up.getBlock() != this && upup.getBlock() != this && super.canPlaceBlockAt(worldIn, pos) && up.getBlock().isReplaceable(worldIn, pos.up()) && upup.getBlock().isReplaceable(worldIn, pos.up().up());
  }*/

  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    worldIn.setBlockState(pos.up(), this.getDefaultState().with(HALF, Half.MIDDLE).with(FACING, state.get(FACING)));
    worldIn.setBlockState(pos.up().up(), this.getDefaultState().with(HALF, Half.TOP).with(FACING, state.get(FACING)));
  }


  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    // Work out the facing
    Direction f = Direction.fromAngle(context.getPlayer().rotationYaw).getOpposite();
    if (f == Direction.NORTH || f == Direction.SOUTH || f == Direction.DOWN || f == Direction.UP) {
      f = Direction.NORTH;
    } else {
      f = Direction.EAST;
    }
    return this.getDefaultState().with(HALF, Half.BOTTOM).with(FACING, f);
  }

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
    public String getString() {
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
  public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    super.randomTick(state, world, pos, random);

    if (!GeneralConfig.EnableGroveStoneEnvironment) return;

    if (world.isRemote) return;

    if (!state.get(VALID)) return;

    if (random.nextInt(GeneralConfig.GroveStoneChance) == 1) {
      int effectsCount = 1 + random.nextInt(1);

      List<BlockPos> positions = Collections.emptyList();/*Util.getBlocksWithinRadius(world, pos.down(), 4, 5, 4, (p) -> {
        if (world.isAirBlock(p.up())) {
          BlockState s = world.getBlockState(p);
          return s.getMaterial() == Material.GRASS;
        }
        return false;
      });*/

      Collections.shuffle(positions);

      for (BlockPos p : positions) {
        if (effectsCount <= 0) break;

        BlockState s = world.getBlockState(p);
        Block b = s.getBlock();
        // TODO: Improve this somehow
/*        if (s.getMaterial() == Material.GRASS && world.isAirBlock(p.up().up())) {
          switch (random.nextInt(50)) {
            case 0:
              if (b.canSustainPlant(s, world, p, Direction.UP, net.minecraft.block.Blocks.DOUBLE_PLANT)) {
                net.minecraft.block.Blocks.DOUBLE_PLANT.placeAt(world, p.up(), DoublePlantBlock.PlantType.ROSE, 3);
                break;
              }
            case 1:
              if (b.canSustainPlant(s, world, p, Direction.UP, net.minecraft.block.Blocks.DOUBLE_PLANT)) {
                Blocks.DOUBLE_PLANT.placeAt(world, p.up(), DoublePlantBlock.PlantType.SUNFLOWER, 3);
                break;
              }
            case 2:
            case 3:
              if (b.canSustainPlant(s, world, p, Direction.UP, net.minecraft.block.Blocks.DOUBLE_PLANT)) {
                net.minecraft.block.Blocks.DOUBLE_PLANT.placeAt(world, p.up(), DoublePlantBlock.PlantType.GRASS, 3);
                break;
              }
            case 4:
              if (b.canSustainPlant(s, world, p, Direction.UP, net.minecraft.block.Blocks.DOUBLE_PLANT)) {
                net.minecraft.block.Blocks.DOUBLE_PLANT.placeAt(world, p.up(), DoublePlantBlock.PlantType.PAEONIA, 3);
                break;
              }
            case 5:
              if (b.canSustainPlant(s, world, p, Direction.UP, net.minecraft.block.Blocks.DOUBLE_PLANT)) {
                net.minecraft.block.Blocks.DOUBLE_PLANT.placeAt(world, p.up(), DoublePlantBlock.PlantType.SYRINGA, 3);
                break;
              }
            default:
              if (b.canSustainPlant(s, world, p, Direction.UP, net.minecraft.block.Blocks.TALLGRASS)) {
                world.setBlockState(p.up(), net.minecraft.block.Blocks.TALLGRASS.getDefaultState().with(TallGrassBlock.TYPE, TallGrassBlock.EnumType.GRASS), 3);
                break;
              }
              continue;
          }
          MessageOvergrowthEffectFX message = new MessageOvergrowthEffectFX(p.getX() + 0.5, p.getY() + 0.3, p.getZ() + 0.5);
          PacketHandler.sendToAllTracking(message, world, p.up());
          effectsCount--;*/
      }
    }
  }

  @Override
  public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    super.animateTick(stateIn, worldIn, pos, rand);

    if (stateIn.get(VALID) && stateIn.get(HALF) == Half.MIDDLE) {
      for (int i = -2; i <= 2; ++i) {
        for (int j = -2; j <= 2; ++j) {
          if (i > -2 && i < 2 && j == -1) {
            j = 2;
          }
          if (rand.nextInt(12) == 0) {
            if (!worldIn.isAirBlock(pos.add(i / 2, 0, j / 2))) {
              break;
            }
/*            ClientProxy.particleRenderer.spawnParticle(
                worldIn,
                ParticleLeafArc.class,
                (double) pos.getX() + 0.5D,
                (double) pos.getY() + 0.75D,
                (double) pos.getZ() + 0.5D,
                (i + rand.nextDouble() - 0.05) * 0.025,
                0,
                (j + rand.nextFloat() - 0.05) * 0.025,
                100,
                (232 / 255.0) + rand.nextDouble() * 0.05,
                167 / 255.0,
                111 / 255.0,
                0.385,
                1,
                1
            );*/
          }
        }
      }
      List<BlockPos> potentials = Collections.emptyList(); //Util.getBlocksWithinRadius(worldIn, pos, TileEntityFeyCrafter.GROVE_STONE_RADIUS, TileEntityFeyCrafter.GROVE_STONE_RADIUS, TileEntityFeyCrafter.GROVE_STONE_RADIUS, ModBlocks.fey_crafter, ModBlocks.runic_crafter);
      if (!potentials.isEmpty()) {
        Vector3d me = new Vector3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5, 0.75, 0.5);
        for (BlockPos fey : potentials) {
          if (rand.nextInt(3) == 0) {
            Vector3d fey2 = new Vector3d(fey.getX(), fey.getY(), fey.getZ()).add(0.5, 0.78, 0.5);
            Vector3d origAngle = fey2.subtract(me);
            Vector3d angle = origAngle.normalize().scale(0.07);
/*            ClientProxy.particleRenderer.spawnParticle(worldIn, ParticlePyreLeaf.class,
                me.x,
                me.y,
                me.z,
                angle.x * 0.5f,
                angle.y * 0.5f,
                angle.z * 0.5f,
                (origAngle.lengthSquared() * 10) - 5,
                (232 / 255.0) + rand.nextDouble() * 0.05,
                167 / 255.0,
                111 / 255.0,
                0.485,
                1,
                0,
                fey2.x,
                fey2.y,
                fey2.z,
                0.65
            );*/
          }
        }
      }
    }
  }
/*
  @Override
  @Nonnull
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
    if (face == Direction.UP) {
      return BlockFaceShape.UNDEFINED;
    }
    return super.getBlockFaceShape(worldIn, state, pos, face);
  }*/
}
