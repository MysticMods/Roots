package mysticmods.roots.block.crop;

import mysticmods.roots.api.RootsTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ElementalCropBlock extends ThreeStageCropBlock {
  public ElementalCropBlock(Supplier<? extends ItemLike> seedProvider, ElementalType type, Properties builder) {
    super(seedProvider, builder);
    this.registerDefaultState(this.getStateDefinition().any().setValue(ElementalType.ELEMENTAL_TYPE, type)
        .setValue(ElementalType.SOIL_TYPE, ElementalType.NONE));
  }

  @Override
  public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
    if (!pLevel.isAreaLoaded(pPos, 1)) {
      return; // Forge: prevent loading unloaded chunks when checking neighbor's light
    }
    int i = this.getAge(pState);
    if (i < this.getMaxAge()) {
      float f = getGrowthSpeed(pState, pLevel, pPos);
      // TODO: Should this be handled via the event instead of in the elemental crop block? PROBABLY.
      if (net.neoforged.neoforge.common.CommonHooks.canCropGrow(pLevel, pPos, pState, pRandom.nextInt((int) (25.0F / f) + 1) == 0)) {
        BlockState newState = this.getStateForAge(i + 1);

        newState = fillBlockState(pState, newState, pLevel.getBlockState(pPos.below()));

        pLevel.setBlock(pPos, newState, 2);
        net.neoforged.neoforge.common.CommonHooks.fireCropGrowPost(pLevel, pPos, pState);
      }
    }
  }

  private static @NotNull BlockState fillBlockState(BlockState pState, BlockState newState, BlockState belowState) {
    ElementalType thisType = pState.getValue(ElementalType.ELEMENTAL_TYPE);
    TagKey<Block> soilTag = thisType.getTag();
    newState.setValue(ElementalType.ELEMENTAL_TYPE, thisType);

    if (soilTag != null) {
      if (belowState.hasProperty(ElementalType.SOIL_TYPE)) {
        newState = newState.setValue(ElementalType.SOIL_TYPE, belowState.getValue(ElementalType.SOIL_TYPE));
      } else if (belowState.is(RootsTags.Blocks.BASE_ELEMENTAL_SOIL)) {
        newState = newState.setValue(ElementalType.SOIL_TYPE, ElementalType.DEFAULT);
      } else {
        newState = newState.setValue(ElementalType.SOIL_TYPE, ElementalType.NONE);
      }
    }

    if (pState.hasProperty(BlockStateProperties.WATERLOGGED)) {
      newState = newState.setValue(BlockStateProperties.WATERLOGGED, pState.getValue(BlockStateProperties.WATERLOGGED));
    }
    return newState;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(ElementalType.ELEMENTAL_TYPE, ElementalType.SOIL_TYPE);
  }

  @Override
  public void growCrops(Level level, BlockPos pos, BlockState state) {
    int i = this.getAge(state) + this.getBonemealAgeIncrease(level);
    int j = this.getMaxAge();
    if (i > j) {
      i = j;
    }

    BlockState newState = this.getStateForAge(i);

    newState = fillBlockState(state, newState, level.getBlockState(pos.below()));

    level.setBlock(pos, newState, 2);
  }

  @Override
  public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
    BlockState state = super.getStateForPlacement(context);
    Level level = context.getLevel();
    BlockPos pos = context.getClickedPos();
    BlockState stateBelow = level.getBlockState(pos.below());
    if (stateBelow.hasProperty(ElementalType.SOIL_TYPE) && state.hasProperty(ElementalType.SOIL_TYPE)) {
      state = state.setValue(ElementalType.SOIL_TYPE, stateBelow.getValue(ElementalType.SOIL_TYPE));
    }
    return state;
  }

  @Override
  protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
    // No ravagers breaking these crops!
  }
}
