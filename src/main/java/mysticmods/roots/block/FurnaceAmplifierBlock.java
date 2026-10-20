package mysticmods.roots.block;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.reference.Shapes;
import mysticmods.roots.blockentity.FurnaceAmplifierBlockEntity;
import mysticmods.roots.blockentity.template.BaseBlockEntity;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.util.VoxelUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FurnaceAmplifierBlock extends FaceAttachedHorizontalDirectionalBlock implements EntityBlock {
  public static final MapCodec<FurnaceAmplifierBlock> CODEC = simpleCodec(FurnaceAmplifierBlock::new);
  private final Map<BlockState, VoxelShape> shapesCache;

  @Override
  public MapCodec<FurnaceAmplifierBlock> codec() {
    return CODEC;
  }

  public FurnaceAmplifierBlock(Properties properties) {
    super(properties);

    this.registerDefaultState(
        this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL)
    );
    this.shapesCache = getShapeForEachState(this::makeShape);
  }

  private VoxelShape makeShape(BlockState state) {
    Direction facing = state.getValue(FACING);
    return switch (state.getValue(FACE)) {
      case FLOOR -> VoxelUtil.rotateHorizontal(Shapes.AMPLIFIER, facing);
      case WALL -> VoxelUtil.rotate(Shapes.AMPLIFIER, box -> VoxelUtil.rotateHorizontal(rotateX90(box), facing));
      case CEILING ->
          VoxelUtil.rotate(Shapes.AMPLIFIER, box -> VoxelUtil.rotateHorizontal(rotateX180(box), facing.getOpposite()));
    };
  }

  private static AABB rotateX90(AABB box) {
    return new AABB(box.minX, box.minZ, -box.minY, box.maxX, box.maxZ, -box.maxY);
  }

  private static AABB rotateX180(AABB box) {
    return new AABB(box.minX, -box.minY, -box.minZ, box.maxX, -box.maxY, -box.maxZ);
  }

  @Override
  protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return shapesCache.get(state);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, FACE);
  }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new FurnaceAmplifierBlockEntity(pos, state);
  }

  @Override
  public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
    if (!level.isClientSide()) {
      return BaseBlockEntity::serverTick;
    } else {
      return BaseBlockEntity::clientTick;
    }
  }

  @Override
  protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
    super.onRemove(state, level, pos, newState, movedByPiston);
    if (!newState.is(state.getBlock())) {
      level.getData(ModAttachments.GROVE_CONSUMERS).remove(pos);
    }
  }
}
