package mysticmods.roots.ritual;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.StateProperties;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.block.GroveStoneBlock;
import mysticmods.roots.blockentity.GroveStoneBlockEntity;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.util.PositionCache;
import mysticmods.roots.util.ReputationHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiPredicate;

public class GroveSupplicationRitual extends Ritual {
  private static final BiPredicate<Level, BlockPos> GROVE_STONE_PREDICATE = (level, pos) -> {
    BlockState state = level.getBlockState(pos);
    return state.is(RootsTags.Blocks.GROVE_STONES) && state.hasProperty(StateProperties.GroveStone.PART) && state.hasProperty(StateProperties.ACTIVE);
  };

  private static final List<BiPredicate<Level, BlockPos>> PREDICATES = List.of(GROVE_STONE_PREDICATE);

  @Override
  public List<BiPredicate<Level, BlockPos>> getPredicates() {
    return PREDICATES;
  }

  @Override
  public void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, @Nullable PositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    // TODO: Move this to super.functionalTick
    // TODO: call super.functionalTick
    if (pCache == null && requiresCache()) {
      RootsAPI.LOG.error("Ritual {} requires a PositionCache but none was provided. This will cause the ritual to not function correctly.", getOrCreateDescriptionId());
      return;
    }

    if (duration % getInterval() == 0) {
      if (blockEntity.getBoundingBox() != null) {
        for (BlockPos pos : pCache.iterate(GROVE_STONE_PREDICATE, randomSource)) {
          if (blockEntity.getLevel().getBlockEntity(pos) instanceof GroveStoneBlockEntity groveStoneBlockEntity) {
            groveStoneBlockEntity.tryActivating(this, blockEntity, blockEntity.getLastPlayer());
          }

          BlockState state = blockEntity.getLevel().getBlockState(pos);

          if (!state.hasProperty(StateProperties.GroveStone.RANK) || !state.hasProperty(StateProperties.ACTIVE)) {
            continue;
          }

          // Activate non-wild stones if player has the correct rank
          if (!state.is(RootsTags.Blocks.GROVE_STONE_WILD) && state.getBlock() instanceof GroveStoneBlock groveStone && blockEntity.getLastPlayer() != null) {
            Grove grove = groveStone.getGrove().value();
            Player player = blockEntity.getLastPlayer();
            int rank = ReputationHelper.getRank(player, grove);
            // TODO: Max rank???
            BlockState newState = state;

            if (rank > 0 && !state.getValue(GroveStoneBlock.ACTIVE)) {
              newState = state.setValue(GroveStoneBlock.ACTIVE, true);
            }

            int currentRank = state.getValue(GroveStoneBlock.RANK);
            if (rank > 0 && rank > currentRank) {
              newState = newState.setValue(GroveStoneBlock.RANK, rank);
            }

            if (newState != state) {
              blockEntity.getLevel().setBlockAndUpdate(pos, newState);

              if (blockEntity.getLevel().getBlockEntity(pos) instanceof GroveStoneBlockEntity groveStoneEntity) {
                groveStoneEntity.setRank(rank);
              }
            }
          }
        }
      }
    }
  }

  @Override
  public void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity
      blockEntity, int duration, RandomSource randomSource) {

  }

  @Override
  public void initialize(Holder<Ritual> holder) {
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getDurationProperty() {
    return ModRituals.GROVE_SUPPLICATION_DURATION;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusXZProperty() {
    return ModRituals.GROVE_SUPPLICATION_RADIUS_XZ;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModRituals.GROVE_SUPPLICATION_RADIUS_Y;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getIntervalProperty() {
    return ModRituals.GROVE_SUPPLICATION_INTERVAL;
  }
}
