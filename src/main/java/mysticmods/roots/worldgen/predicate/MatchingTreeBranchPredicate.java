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

public class MatchingTreeBranchPredicate extends StateTestingPredicate {
    public static final Codec<MatchingTreeBranchPredicate> CODEC = RecordCodecBuilder.create((p_204688_) -> stateTestingCodec(p_204688_).apply(p_204688_, MatchingTreeBranchPredicate::new));

    protected MatchingTreeBranchPredicate(Vec3i offset) {
        super(offset);
    }

    @Override
    protected boolean test(BlockState pState) {
        return pState.is(BlockTags.LOGS_THAT_BURN) && pState.hasProperty(RotatedPillarBlock.AXIS) && pState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.X;
    }

    @Override
    public BlockPredicateType<?> type() {
        return ModFeatures.MATCHING_TREE_BRANCH_PREDICATE.get();
    }

    public static MatchingTreeBranchPredicate create() {
        return new MatchingTreeBranchPredicate(Vec3i.ZERO);
    }

    public static MatchingTreeBranchPredicate create(Vec3i offset) {
        return new MatchingTreeBranchPredicate(offset);
    }
}
