package mysticmods.roots.ritual;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class FrostLandsRitual extends Ritual {
  private int healInterval, fluidCount, count;
  private float spawnChance, layerChance, powderedChance, iceChance;

  private static final BiPredicate<Level, BlockPos> WATER_OR_LAVA = (level, pos) -> {
    FluidState fluidState = level.getFluidState(pos.below());
    return fluidState.isSource() && fluidState.is(Tags.Fluids.WATER) || fluidState.is(Tags.Fluids.LAVA);
  };
  private static final BlockState snowLayer = Blocks.SNOW.defaultBlockState();

  private static final BiPredicate<Level, BlockPos> FROST_LANDS_PREDICATE = (level, pos) -> {
    BlockState state = level.getBlockState(pos);
    if (!state.isAir() && !state.canBeReplaced()) {
      return false;
    }

    BlockPos below = pos.below();

    BlockState belowState = level.getBlockState(below);

    if (belowState.isAir()) {
      return false;
    }

    if (belowState.is(Blocks.ICE) || belowState.is(Blocks.SNOW)) {
      return true;
    }

    return snowLayer.canSurvive(level, below);
  };

  private static final BiPredicate<Level, BlockPos> IS_FARMLAND = (level, pos) -> {
    BlockState state = level.getBlockState(pos);
    return state.is(Tags.Blocks.VILLAGER_FARMLANDS);
  };

  private static final BiPredicate<Level, BlockPos> IS_FIRE = (level, pos) -> {
    BlockState state = level.getBlockState(pos);
    return state.is(BlockTags.FIRE);
  };

  @Override
  protected void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    List<BlockPos> affectedPositions = new ArrayList<>();

    if (duration % healInterval == 0) {
      List<LivingEntity> entities = blockEntity.getLevel()
          .getEntitiesOfClass(LivingEntity.class, getAABB().move(blockEntity.getBlockPos()), EntitySelector.NO_SPECTATORS.and(Entity::isAlive)
              .and(o -> o.getType().is(RootsTags.Entities.HEALABLE_ICE_CREATURES)));
      if (!entities.isEmpty()) {
        for (LivingEntity entity : entities) {
          entity.heal(entity.getMaxHealth() - entity.getHealth());
          entity.clearFire();
          affectedPositions.add(entity.blockPosition());
        }
      }
    }

    // TODO: Positions should come from the cached positions
    if (duration % getInterval() == 0) {
      List<BlockPos> positions = BlockUtil.getBlocksWithinRadius(pLevel, pPos, getRadiusXZ(), getRadiusY(), WATER_OR_LAVA);
      for (int i = 0; i < fluidCount; i++) {
        if (!positions.isEmpty()) {
          BlockPos chosen = positions.remove(randomSource.nextInt(positions.size())).below();

          FluidState fluidState = pLevel.getFluidState(chosen);
          if (fluidState.isSource()) {
            if (fluidState.is(FluidTags.WATER)) {
              pLevel.setBlockAndUpdate(chosen, Blocks.ICE.defaultBlockState());
              affectedPositions.add(chosen);
            } else if (fluidState.is(FluidTags.LAVA)) {
              pLevel.setBlockAndUpdate(chosen, Blocks.OBSIDIAN.defaultBlockState());
              affectedPositions.add(chosen);
            }
          }
        }
      }

      positions = BlockUtil.getBlocksWithinRadius(pLevel, pPos, getRadiusXZ(), getRadiusY(), FROST_LANDS_PREDICATE);
      if (!positions.isEmpty()) {
        if (randomSource.nextFloat() < spawnChance) {
          SnowGolem golem = EntityType.SNOW_GOLEM.create(pLevel);
          if (golem != null) {
            BlockPos pos = positions.get(randomSource.nextInt(positions.size()));
            golem.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            pLevel.addFreshEntity(golem);
          }
        }
      }

      int breakout = 0;
      int affected = 0;
      while (!positions.isEmpty() && breakout < 50 && affected < count) {
        // All these positions are below
        BlockPos chosen = positions.remove(randomSource.nextInt(positions.size()));
        BlockPos below = chosen.below();

        breakout++;
        BlockState state = pLevel.getBlockState(chosen);
        BlockState belowState = pLevel.getBlockState(below);

        // If it's snow layer, increase the size of the layer
        if (belowState.is(Blocks.SNOW) && belowState.hasProperty(SnowLayerBlock.LAYERS) && belowState.getValue(SnowLayerBlock.LAYERS) < 8 && randomSource.nextFloat() < layerChance) {
          pLevel.setBlock(below, belowState.setValue(SnowLayerBlock.LAYERS, belowState.getValue(SnowLayerBlock.LAYERS) + 1), 3);
          affectedPositions.add(below);
          affected++;
          continue;
        } else if (state.is(Blocks.SNOW) && state.hasProperty(SnowLayerBlock.LAYERS) && state.getValue(SnowLayerBlock.LAYERS) < 8 && randomSource.nextFloat() < layerChance) {
          pLevel.setBlock(chosen, state.setValue(SnowLayerBlock.LAYERS, state.getValue(SnowLayerBlock.LAYERS) + 1), 3);
          affectedPositions.add(chosen);
          affected++;
          continue;
        }

        if (snowLayer.canSurvive(pLevel, chosen)) {
          pLevel.setBlock(chosen, Blocks.SNOW.defaultBlockState(), 3);
          affectedPositions.add(chosen);
          affected++;
          continue;
        }

        if (belowState.is(Blocks.SNOW) && belowState.getValue(SnowLayerBlock.LAYERS) == 8) {
          if (randomSource.nextFloat() < powderedChance) {
            pLevel.setBlock(below, Blocks.POWDER_SNOW.defaultBlockState(), 3);
            affectedPositions.add(below);
            affected++;
            continue;
          }
        }

        if (belowState.is(Blocks.ICE)) {
          if (randomSource.nextFloat() < iceChance) {
            pLevel.setBlock(below, randomSource.nextBoolean() ? Blocks.BLUE_ICE.defaultBlockState() : Blocks.PACKED_ICE.defaultBlockState(), 3);
            affectedPositions.add(below);
            affected++;
          }
        }
      }

      // Moisturize farmland
      positions = BlockUtil.getBlocksWithinRadius(pLevel, pPos, getRadiusXZ(), getRadiusY(), IS_FARMLAND);
      for (BlockPos pos : positions) {
        BlockState stateAt = pLevel.getBlockState(pos);
        if (stateAt.hasProperty(FarmBlock.MOISTURE) && stateAt.getValue(FarmBlock.MOISTURE) < FarmBlock.MAX_MOISTURE) {
          pLevel.setBlock(pos, stateAt.setValue(FarmBlock.MOISTURE, FarmBlock.MAX_MOISTURE), 3);
          affectedPositions.add(pos);
        }
      }

      // Extinguish fires
      positions = BlockUtil.getBlocksWithinRadius(pLevel, pPos, getRadiusXZ(), getRadiusY(), IS_FIRE);
      for (BlockPos pos : positions) {
        pLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        affectedPositions.add(pos);
      }
    }

  }

  @Override
  protected void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {

  }

  @Override
  protected void initialize(Holder<Ritual> holder) {
    var properties = holder.getData(DataMaps.RITUAL_PROPERTY_DATA);
    this.healInterval = properties.get(ModRituals.FROST_LANDS_INTERVAL_HEAL);
    this.spawnChance = properties.get(ModRituals.FROST_LANDS_SPAWN_CHANCE);
    this.fluidCount = properties.get(ModRituals.FROST_LANDS_FLUID_COUNT);
    this.count = properties.get(ModRituals.FROST_LANDS_COUNT);
    this.layerChance = properties.get(ModRituals.FROST_LANDS_LAYER_CHANCE);
    this.powderedChance = properties.get(ModRituals.FROST_LANDS_POWDER_CHANCE);
    this.iceChance = properties.get(ModRituals.FROST_LANDS_ICE_CHANCE);
  }

  @Override
  public boolean providesLight() {
    return false;
  }

  protected void buildProperties (List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModRituals.FROST_LANDS_INTERVAL_HEAL);
    properties.add(ModRituals.FROST_LANDS_SPAWN_CHANCE);
    properties.add(ModRituals.FROST_LANDS_FLUID_COUNT);
    properties.add(ModRituals.FROST_LANDS_COUNT);
    properties.add(ModRituals.FROST_LANDS_LAYER_CHANCE);
    properties.add(ModRituals.FROST_LANDS_POWDER_CHANCE);
    properties.add(ModRituals.FROST_LANDS_ICE_CHANCE);
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getDurationProperty() {
    return ModRituals.FROST_LANDS_DURATION;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusXZProperty() {
    return ModRituals.FROST_LANDS_RADIUS_XZ;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModRituals.FROST_LANDS_RADIUS_Y;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getIntervalProperty() {
    return ModRituals.FROST_LANDS_INTERVAL;
  }
}
