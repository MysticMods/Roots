package mysticmods.roots.block;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.reference.Shapes;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.blockentity.template.BaseBlockEntity;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.init.ModSounds;
import mysticmods.roots.particle.ColorGravityParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PyreBlock extends UseDelegatedBlock implements EntityBlock {
  // Active -> flames of some description
  public static final BooleanProperty BURNING = BooleanProperty.create("burning");
  // Lit -> producing light!
  public static final BooleanProperty LIT = BooleanProperty.create("lit");

  public PyreBlock(Properties builder) {
    super(builder);
    registerDefaultState(defaultBlockState().setValue(LIT, false).setValue(BURNING, false));
  }

  @Override
  public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
    return Shapes.PYRE;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    super.createBlockStateDefinition(pBuilder);
    pBuilder.add(PyreBlock.LIT, PyreBlock.BURNING);
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
  public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
    if (pState.is(RootsTags.Blocks.PYRES) && pState.getValue(PyreBlock.BURNING)) {
      double x = pPos.getX() + 0.5f;
      double y = pPos.getY() + 0.5f;
      double z = pPos.getZ() + 0.5f;
      if (pRandom.nextDouble() < 0.1) {
        pLevel.playLocalSound(x, y, z, ModSounds.PYRE_CRACKLES.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
      }

      if (pRandom.nextInt(4) == 0) {
        pLevel.addParticle(
            new ColorGravityParticleOptions(
                ModParticles.PYRE,
                pRandom.nextBoolean() ? 0xc96c03 : 0xe9bd39,
                0x8b3100,
                -(pRandom.nextFloat() * 0.03f)
            ),
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
              new ColorGravityParticleOptions(
                  ModParticles.PYRE_LEAF,
                  pRandom.nextBoolean() ? 0x7abb75 : 0x2b6322,
                  0f
              ),
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
}
