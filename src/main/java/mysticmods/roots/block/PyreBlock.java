package mysticmods.roots.block;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.blockentity.InventoryBlockEntity;
import mysticmods.roots.api.reference.Shapes;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.blockentity.template.BaseBlockEntity;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.init.ModSounds;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PyreBlock extends UseDelegatedBlock implements EntityBlock, SimpleWaterloggedBlock {
  // Active -> flames of some description
  public static final BooleanProperty ACTIVE = BooleanProperty.create("burning");
  // Lit -> producing light!
  public static final BooleanProperty LIT = BooleanProperty.create("lit");
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  public PyreBlock(Properties builder) {
    super(builder);
    registerDefaultState(defaultBlockState().setValue(LIT, false).setValue(ACTIVE, false)
        .setValue(WATERLOGGED, false));
  }

  @Override
  public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
    return Shapes.PYRE;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    super.createBlockStateDefinition(pBuilder);
    pBuilder.add(PyreBlock.LIT, PyreBlock.ACTIVE, PyreBlock.WATERLOGGED);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
    return new PyreBlockEntity(ModBlockEntities.PYRE.get(), pPos, pState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
    if (pLevel.isClientSide()) {
      return BaseBlockEntity::clientTick;
    } else {
      return BaseBlockEntity::serverTick;
    }
  }

  @Override
  protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
    BlockPos blockpos = hit.getBlockPos();
    if (!level.isClientSide
        && projectile.getOwner() instanceof Player player
        && level.getBlockEntity(blockpos) instanceof PyreBlockEntity pyre) {

      if (projectile.isOnFire() && projectile.mayInteract(level, blockpos) && !state.getValue(PyreBlock.ACTIVE) && !state.getValue(PyreBlock.WATERLOGGED)) {
        pyre.light(player);
      }
    }
  }

  @Override
  public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
    if (state.getValue(PyreBlock.ACTIVE) && !context.getLevel().isClientSide() && context.getLevel()
        .getBlockEntity(context.getClickedPos()) instanceof PyreBlockEntity pyre) {
      pyre.stopRitual(false);
      return state.setValue(PyreBlock.ACTIVE, false).setValue(PyreBlock.LIT, false);
    }
    return super.getToolModifiedState(state, context, itemAbility, simulate);
  }

  @Override
  public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
    if (pState.hasBlockEntity() && (!pState.is(pNewState.getBlock()) || !pNewState.hasBlockEntity()) && pLevel.getBlockEntity(pPos) instanceof InventoryBlockEntity ibe) {
      Containers.dropContents(pLevel, pPos, ibe.getItems());

      if (ibe instanceof PyreBlockEntity pyre) {
        pyre.removed();
        List<ItemStack> popped = pyre.popStoredItems();
        if (!popped.isEmpty()) {
          NonNullList<ItemStack> items = NonNullList.withSize(popped.size(), ItemStack.EMPTY);
          for (int i = 0; i < popped.size(); i++) {
            items.set(i, popped.get(i));
          }
          Containers.dropContents(pLevel, pPos, items);
        }
      }
    }
    super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
  }

  @Override
  public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
    if (pState.is(RootsTags.Blocks.PYRES) && pState.getValue(PyreBlock.ACTIVE)) {
      double x = pPos.getX() + 0.5f;
      double y = pPos.getY() + 0.5f;
      double z = pPos.getZ() + 0.5f;
      if (pRandom.nextDouble() < 0.1) {
        pLevel.playLocalSound(x, y, z, ModSounds.PYRE_CRACKLES.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
      }

      int color1 = pState.is(ModBlocks.SOUL_PYRE) || pState.is(ModBlocks.REINFORCED_SOUL_PYRE) ? 0x5be3e8 : 0xc96c03;
      if (pRandom.nextBoolean()) {
        color1 = pState.is(ModBlocks.SOUL_PYRE) || pState.is(ModBlocks.REINFORCED_SOUL_PYRE) ? 0x9adfe1 : 0xe9bd39;
      }

      int color2 = pState.is(ModBlocks.SOUL_PYRE) || pState.is(ModBlocks.REINFORCED_SOUL_PYRE) ? 0x088f92 : 0x8b3100;

      if (pRandom.nextInt(4) == 0) {
        pLevel.addParticle(
            RootsParticleOptions.builder(ModParticles.PYRE).color(color1, color2).build(),
            x + (pRandom.nextFloat() - 0.5f) * 0.3f,
            y + 0.1f + (pRandom.nextFloat()) * 0.2f,
            z + (pRandom.nextFloat() - 0.5f) * 0.3f,
            0,
            0,
            0
        );
      }

      BoundingBox bb = PyreBlockEntity.getPyreBoundingBox().moved(pPos.getX(), pPos.getY(), pPos.getZ());
      List<BlockPos> positions = BlockPos.betweenClosedStream(bb).filter(p -> {
        BlockState state = pLevel.getBlockState(p);
        return state.is(RootsTags.Blocks.CAPSTONES);
      }).map(BlockPos::immutable).toList();

      for (BlockPos pos : positions) {
        if (pRandom.nextInt(16) == 0) {
          double nx = (float) pos.getX() + 0.5f + pRandom.nextFloat() - 0.5f;
          double ny = (float) pos.getY() + 0.5f + pRandom.nextFloat() - 0.5f;
          double nz = (float) pos.getZ() + 0.5f + pRandom.nextFloat() - 0.5f;
          pLevel.addParticle(
              RootsParticleOptions.builder(
                  ModParticles.PYRE_LEAF).color(pRandom.nextBoolean() ? 0x7abb75 : 0x2b6322).build(),
              x,
              y + 1.5,
              z,
              (nx - x),
              (ny - y),
              (nz - z)
          );
        }
      }
    }
  }

  @javax.annotation.Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    BlockState state = defaultBlockState();
    FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
    if (fluidState.is(FluidTags.WATER)) {
      return state.setValue(WATERLOGGED, true);
    }

    return state;
  }

  @Override
  public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
    if (stateIn.getValue(WATERLOGGED)) {
      worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
    }

    return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }
}
