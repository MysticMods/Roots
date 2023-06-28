package mysticmods.roots.worldgen.features;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.block.WildRootsBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;

import java.util.Arrays;
import java.util.List;

public class SupportingDirectionalBlockFeature extends Feature<SimpleBlockConfiguration> {

    public SupportingDirectionalBlockFeature(Codec<SimpleBlockConfiguration> configFactory) {
        super(configFactory);
    }

    private List<Direction> directions = null;

    @Override
    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> context) {
        if (directions == null) {
            directions = Arrays.asList(Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.DOWN);
        }

        WorldGenLevel level = context.level();
        BlockPos rootPos = context.origin();
        BlockState rootState = context.config().toPlace().getState(context.random(), rootPos);
        BlockState worldState = level.getBlockState(rootPos);

        if (worldState.isAir() || worldState.is(BlockTags.REPLACEABLE_PLANTS) || worldState.is(Blocks.SNOW)) {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            for (Direction direction : directions) {
                mutableBlockPos = mutableBlockPos.set(rootPos).move(direction.getOpposite());

                rootState = rootState.setValue(WildRootsBlock.FACING, direction);
                worldState = level.getBlockState(mutableBlockPos);

                if (!worldState.is(RootsAPI.Tags.Blocks.SUPPORTS_WILD_ROOTS)) {
                    continue;
                }

                if (rootState.canSurvive(level, rootPos)) {
                    level.setBlock(rootPos, rootState, 3);
                    return true;
                }
            }
        }
        return false;

    }
}