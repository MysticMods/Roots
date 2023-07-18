package mysticmods.roots.worldgen.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.init.ModFeatures;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.minecraft.world.level.levelgen.blockpredicates.StateTestingPredicate;

public class MatchingTreePredicate extends StateTestingPredicate {
    public static final Codec<MatchingTreePredicate> CODEC = RecordCodecBuilder.create((p_204688_) -> stateTestingCodec(p_204688_).apply(p_204688_, MatchingTreePredicate::new));

    protected MatchingTreePredicate(Vec3i offset) {
        super(offset);
    }

    @Override
    protected boolean test(BlockState pState) {
        return pState.is(BlockTags.LOGS_THAT_BURN) && pState.hasProperty(RotatedPillarBlock.AXIS) && pState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y;
    }

    @Override
    public BlockPredicateType<?> type() {
        return ModFeatures.MATCHING_TREE_PREDICATE.get();
    }

    public static MatchingTreePredicate create() {
        return new MatchingTreePredicate(Vec3i.ZERO);
    }

    public static MatchingTreePredicate create(Vec3i offset) {
        return new MatchingTreePredicate(offset);
    }
}
