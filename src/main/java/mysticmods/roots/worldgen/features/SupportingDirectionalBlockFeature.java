package mysticmods.roots.worldgen.features;

import com.mojang.serialization.Codec;
import mysticmods.roots.block.WildRootsBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;

public class SupportingDirectionalBlockFeature extends Feature<SimpleBlockConfiguration> {

    public SupportingDirectionalBlockFeature(Codec<SimpleBlockConfiguration> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> context) {

        WorldGenLevel level = context.level();
        BlockPos rootPos = context.origin();
        BlockState rootState = context.config().toPlace().getState(context.random(), rootPos);
        BlockState worldState = level.getBlockState(rootPos);

        if (!worldState.isAir()) {
            return false;
        }

        for(Direction direction : Direction.values()) {
            rootState = rootState.setValue(WildRootsBlock.FACING, direction);
            if(rootState.canSurvive(level, rootPos)) {
                level.setBlock(rootPos, rootState, 3);
                return true;
            }
        }

        return false;
    }
}