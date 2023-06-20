package mysticmods.roots.block.crop;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.Supplier;

public class ElementalCropBlock extends ThreeStageCropBlock {
    public static final int BASE_TICK = 9;
    public static final int ELEMENTAL_TICK = 3;

    public ElementalCropBlock(Properties builder, Supplier<Supplier<? extends ItemLike>> seedProvider) {
        super(builder, seedProvider);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        // TODO: Properly implement
        BlockState stateBelow = pLevel.getBlockState(pPos.below());
        if (pRandom.nextInt(stateBelow.is(RootsAPI.Tags.Blocks.ELEMENTAL_SOIL) ? ELEMENTAL_TICK : BASE_TICK) != 0) {
            if (!pLevel.isAreaLoaded(pPos, 1))
                return; // Forge: prevent loading unloaded chunks when checking neighbor's light
            if (pLevel.getRawBrightness(pPos, 0) >= 9) {
                int i = this.getAge(pState);
                if (i < this.getMaxAge()) {
                    float f = getGrowthSpeed(this, pLevel, pPos);
                    if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt((int) (25.0F / f) + 1) == 0)) {
                        BlockState newState = this.getStateForAge(i + 1);
                        if (pState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                            newState = newState.setValue(BlockStateProperties.WATERLOGGED, pState.getValue(BlockStateProperties.WATERLOGGED));
                        }
                        pLevel.setBlock(pPos, newState, 2);
                        net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
                    }
                }
            }
        }
    }

    @Override
    public void growCrops(Level pLevel, BlockPos pPos, BlockState pState) {
        int i = this.getAge(pState) + this.getBonemealAgeIncrease(pLevel);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }

        BlockState newState = this.getStateForAge(i);
        if (pState.hasProperty(BlockStateProperties.WATERLOGGED)) {
            newState = newState.setValue(BlockStateProperties.WATERLOGGED, pState.getValue(BlockStateProperties.WATERLOGGED));
        }
        pLevel.setBlock(pPos, newState, 2);
    }
}
