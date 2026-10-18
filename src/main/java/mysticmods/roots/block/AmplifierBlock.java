package mysticmods.roots.block;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.reference.Shapes;
import mysticmods.roots.util.VoxelUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class AmplifierBlock extends FaceAttachedHorizontalDirectionalBlock {
  public static final MapCodec<AmplifierBlock> CODEC = simpleCodec(AmplifierBlock::new);
  private final Map<BlockState, VoxelShape> shapesCache;

  @Override
  public MapCodec<AmplifierBlock> codec() {
    return CODEC;
  }

  public AmplifierBlock(Properties properties) {
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
  protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
    return InteractionResult.sidedSuccess(level.isClientSide);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, FACE);
  }
}
