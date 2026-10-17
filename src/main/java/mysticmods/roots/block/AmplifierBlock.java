package mysticmods.roots.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.BlockHitResult;

public class AmplifierBlock extends FaceAttachedHorizontalDirectionalBlock {
  public static final MapCodec<AmplifierBlock> CODEC = simpleCodec(AmplifierBlock::new);

  @Override
  public MapCodec<AmplifierBlock> codec() {
    return CODEC;
  }

  public AmplifierBlock(Properties properties) {
    super(properties);

    this.registerDefaultState(
        this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL)
    );
  }

/*    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        switch ((AttachFace)state.getValue(FACE)) {
            case FLOOR:
                if (direction.getAxis() == Direction.Axis.X) {
                }

            case WALL:
                return switch (direction) {
                };
            case CEILING:
            default:
                if (direction.getAxis() == Direction.Axis.X) {
                }
        }
    }*/

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
    return InteractionResult.sidedSuccess(level.isClientSide);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, FACE);
  }
}
