package mysticmods.roots.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.StateProperties;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.reference.Shapes;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.blockentity.GroveStoneBlockEntity;
import mysticmods.roots.blockentity.template.BaseBlockEntity;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.particle.RootsParticleOptions;
import mysticmods.roots.util.VoxelUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class GroveStoneBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock {
  public static final DirectionProperty FACING = StateProperties.GroveStone.FACING;
  public static final EnumProperty<StateProperties.Part> PART = StateProperties.GroveStone.PART;
  public static final BooleanProperty ACTIVE = StateProperties.ACTIVE;
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  public static final IntegerProperty RANK = StateProperties.GroveStone.RANK;

  public static final VoxelShape[] EAST_WEST = {VoxelUtil.rotateHorizontal(Shapes.GROVE_STONE_TOP, Direction.EAST), VoxelUtil.rotateHorizontal(Shapes.GROVE_STONE_MIDDLE, Direction.EAST), VoxelUtil.rotateHorizontal(Shapes.GROVE_STONE_BOTTOM, Direction.EAST)};
  public static final VoxelShape[] NORTH_SOUTH = {Shapes.GROVE_STONE_TOP, Shapes.GROVE_STONE_MIDDLE, Shapes.GROVE_STONE_BOTTOM};

  private final Holder<Grove> grove;

  public GroveStoneBlock(Holder<Grove> grove, Properties builder) {
    super(builder);
    this.registerDefaultState(defaultBlockState().setValue(ACTIVE, false).setValue(PART, StateProperties.Part.BOTTOM)
        .setValue(WATERLOGGED, false));
    this.grove = grove;
  }

  public Holder<Grove> getGrove() {
    return grove;
  }

  @Override
  protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
    return RecordCodecBuilder.mapCodec(instance -> instance.group(RootsRegistries.GROVES.holderByNameCodec()
            .fieldOf("grove").forGetter(o -> ((GroveStoneBlock) o).getGrove()), propertiesCodec())
        .apply(instance, GroveStoneBlock::new));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    super.createBlockStateDefinition(pBuilder);
    pBuilder.add(PART, ACTIVE, FACING, WATERLOGGED, RANK);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    VoxelShape[] parts;
    Direction facing = state.getValue(FACING);

    if (facing == Direction.SOUTH || facing == Direction.NORTH) {
      parts = NORTH_SOUTH;
    } else {
      parts = EAST_WEST;
    }

    return switch (state.getValue(PART)) {
      case TOP -> parts[0];
      case MIDDLE -> parts[1];
      case BOTTOM -> parts[2];
    };
  }

  @Override
  @Nullable
  public BlockState getStateForPlacement(BlockPlaceContext pContext) {
    BlockPos blockpos = pContext.getClickedPos();
    boolean waterlogged = pContext.getLevel().getFluidState(blockpos).isSourceOfType(Fluids.WATER);
    BlockState newState = blockpos.getY() < pContext.getLevel().getMaxBuildHeight() - 1 && pContext.getLevel()
        .getBlockState(blockpos.above())
        .canBeReplaced(pContext) && pContext.getLevel().getBlockState(blockpos.above().above())
        .canBeReplaced(pContext) ? super.getStateForPlacement(pContext) : null;
    if (newState == null) {
      return null;
    }

    newState = newState.setValue(WATERLOGGED, waterlogged);

    for (Direction direction : pContext.getNearestLookingDirections()) {
      if (direction.getAxis().isHorizontal()) {
        return newState.setValue(FACING, direction);
      }
    }

    return newState;
  }

  @Override
  public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
    pLevel.setBlock(pPos.above(), this.defaultBlockState().setValue(PART, StateProperties.Part.MIDDLE)
        .setValue(FACING, pState.getValue(FACING)), 3);
    pLevel.setBlock(pPos.above().above(), this.defaultBlockState().setValue(PART, StateProperties.Part.TOP)
        .setValue(FACING, pState.getValue(FACING)), 3);
  }

  @Override
  public BlockState playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
    if (!pLevel.isClientSide) {
      breakLinkedBlocks(pLevel, pPos, pState, pPlayer);
    }
    return super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
  }

  @Override
  protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
    if (state.getValue(WATERLOGGED)) {
      level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
    }

    StateProperties.Part part = state.getValue(PART);

    if (facing.getAxis() == Direction.Axis.Y) {
      if (part == StateProperties.Part.BOTTOM && facing == Direction.UP) {
        if (!facingState.is(this) || facingState.getValue(PART) != StateProperties.Part.MIDDLE) {
          return Blocks.AIR.defaultBlockState();
        }
      } else if (part == StateProperties.Part.MIDDLE) {
        if (facing == Direction.UP) {
          if (!facingState.is(this) || facingState.getValue(PART) != StateProperties.Part.TOP) {
            return Blocks.AIR.defaultBlockState();
          }
        } else if (facing == Direction.DOWN) {
          if (!facingState.is(this) || facingState.getValue(PART) != StateProperties.Part.BOTTOM) {
            return Blocks.AIR.defaultBlockState();
          }
        }
      } else if (part == StateProperties.Part.TOP && facing == Direction.DOWN) {
        if (!facingState.is(this) || facingState.getValue(PART) != StateProperties.Part.MIDDLE) {
          return Blocks.AIR.defaultBlockState();
        }
      }
    }

    return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  protected void breakLinkedBlocks(LevelAccessor pLevel, BlockPos pPos, BlockState pState, @Nullable Player pPlayer) {
    boolean creative = pPlayer != null && pPlayer.isCreative();
    if (pState.getValue(PART) == StateProperties.Part.BOTTOM) {
      pLevel.destroyBlock(pPos.above(), false);
      pLevel.destroyBlock(pPos.above().above(), false);
    } else if (pState.getValue(PART) == StateProperties.Part.MIDDLE) {
      pLevel.destroyBlock(pPos.below(), !creative);
      pLevel.destroyBlock(pPos.above(), false);
    } else {
      pLevel.destroyBlock(pPos.below(), false);
      pLevel.destroyBlock(pPos.below().below(), !creative);
    }
  }

  @Override
  public @org.jetbrains.annotations.Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    if (state.is(RootsTags.Blocks.GROVE_STONE_PRIMAL)) {
      return null;
    }

    if (state.getValue(PART) != StateProperties.Part.TOP) {
      return null;
    }

    return new GroveStoneBlockEntity(pos, state);
  }

  @Override
  public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
    if (pState.getValue(PART) == StateProperties.Part.TOP && pState.getValue(ACTIVE)) {
      Direction facing = pState.getValue(FACING);
      double centerX = pPos.getX() + 0.5;
      double centerY = pPos.getY() + 0.3;
      double centerZ = pPos.getZ() + 0.5;

      int col1 = grove.value().getColor1();
      int col2 = grove.value().getColor2();
      if (pRandom.nextBoolean()) {
        int temp = col1;
        col1 = col2;
        col2 = temp;
      }

      double baseAngle1 = switch (facing) {
        case SOUTH -> Math.PI / 2;
        case WEST -> Math.PI;
        case NORTH -> -Math.PI / 2;
        default -> 0.0;
      };

      double baseAngle2 = switch (facing) {
        case SOUTH -> -Math.PI / 2;
        case WEST -> 0.0;
        case NORTH -> Math.PI / 2;
        default -> Math.PI;
      };

      for (int i = 0; i < 3; i++) {
        // Spread angle in radians: ±15° (≈0.26 radians)
        double angleOffset = (pRandom.nextDouble() - 0.5) * Math.toRadians(25);


        double finalAngle = baseAngle1 + angleOffset;

        double dx = Math.cos(finalAngle);
        double dz = Math.sin(finalAngle);

        double velocityScale = 0.05 + pRandom.nextDouble() * 0.06;

        dx *= velocityScale;
        dz *= velocityScale;

        double dy = 0.01 + (pRandom.nextDouble() - 0.5) * 0.05;

        pLevel.addParticle(
            new RootsParticleOptions(ModParticles.GROVE_STONE, col1, col2),
            centerX, centerY, centerZ,
            dx, dy, dz
        );

        finalAngle = baseAngle2 + angleOffset;

        dx = Math.cos(finalAngle);
        dz = Math.sin(finalAngle);

        dx *= velocityScale;
        dz *= velocityScale;

        dy = 0.01 + (pRandom.nextDouble() - 0.5) * 0.05;

        pLevel.addParticle(
            new RootsParticleOptions(ModParticles.GROVE_STONE, col1, col2),
            centerX, centerY, centerZ,
            dx, dy, dz
        );
      }
    }
  }

  @org.jetbrains.annotations.Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
    if (pLevel.isClientSide()) {
      return BaseBlockEntity::clientTick;
    } else {
      return BaseBlockEntity::serverTick;
    }
  }
}
