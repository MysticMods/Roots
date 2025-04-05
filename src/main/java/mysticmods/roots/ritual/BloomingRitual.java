package mysticmods.roots.ritual;

import com.mojang.datafixers.util.Pair;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PedestalBlockEntity;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.util.RitualPositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

public class BloomingRitual extends Ritual {
  // TODO: Caching of positions based on predicate
  private static final BiPredicate<Level, BlockPos> TWO_AIR_ABOVE = (level, pos) -> {
    BlockPos above = pos.above();
    return level.getFluidState(pos).isEmpty() && level.getFluidState(pos.above())
        .isEmpty() && level.isEmptyBlock(pos) && level.isEmptyBlock(above) || level.isEmptyBlock(above) && level.getBlockState(pos)
        .canBeReplaced() && level.getFluidState(pos).isEmpty();
  };
  private static final List<BiPredicate<Level, BlockPos>> PREDICATES = Arrays.asList(TWO_AIR_ABOVE);
  // TODO: Data map for block costs that increase the interval
  private int count;
  private int nextTick;

  @Override
  public List<BiPredicate<Level, BlockPos>> getPredicates() {
    return PREDICATES;
  }

  // TODO:
  @SuppressWarnings("deprecation")
  @Override
  protected void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, RitualPositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    if (duration % getInterval() == 0) {
      List<Pair<BlockPos, PedestalBlockEntity>> pedestals = blockEntity.pedestals(RootsTags.Blocks.RITUAL_PEDESTALS, RootsTags.Blocks.DISPLAY_PEDESTALS);

      BlockItem flowerToPlace = null;

      if (!pedestals.isEmpty()) {
        List<ItemStack> stacks = pedestals.stream().map(Pair::getSecond).map(PedestalBlockEntity::getHeldItem)
            .filter(o -> !o.isEmpty()).filter(o -> o.is(RootsTags.Items.BLOOMING_ELIGIBLE_PEDESTAL_FLOWERS)).toList();
        ItemStack stack = stacks.size() == 1 ? stacks.getFirst() : stacks.isEmpty() ? ItemStack.EMPTY : stacks.get(randomSource.nextInt(stacks.size()));
        if (!stack.isEmpty() && stack.getItem() instanceof BlockItem blockItem) {
          if (blockItem.getBlock().builtInRegistryHolder().is(RootsTags.Blocks.BLOOMING_ELIGIBLE_PEDESTAL_FLOWERS)) {
            flowerToPlace = blockItem;
          }
        }
      }

      if (flowerToPlace == null) {
        HolderSet.Named<Block> tag = BuiltInRegistries.BLOCK.getTag(RootsTags.Blocks.BLOOMING_ELIGIBLE_FLOWERS)
            .orElse(null);
        if (tag == null) {
          return;
        }
        Optional<Holder<Block>> optionalHolder = tag.getRandomElement(randomSource);
        if (optionalHolder.isEmpty()) {
          return;
        }

        if (optionalHolder.get().value().asItem() instanceof BlockItem blockItem) {
          flowerToPlace = blockItem;
        } else {
          // TODO: Log some sort of error
        }
      }

      if (flowerToPlace == null) {
        return;
      }

      int placed = 0;
      for (BlockPos chosen : pCache.iterate(TWO_AIR_ABOVE, randomSource)) {
        if (placed >= count) {
          break;
        }
        Vec3 center = Vec3.atCenterOf(chosen);
        BlockPlaceContext context = new BlockPlaceContext(pLevel, null, InteractionHand.MAIN_HAND, new ItemStack(flowerToPlace), new BlockHitResult(center, Direction.UP, chosen, false));
        // TODO: Supress sound
        if (flowerToPlace.place(context).consumesAction()) {
          placed++;
        }
      }
    }
  }

  @Override
  protected void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {

  }

  @Override
  protected void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModRituals.BLOOMING_COUNT);
  }

  @Override
  protected void initialize(Holder<Ritual> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.RITUAL_PROPERTY_DATA);
    this.count = properties.get(ModRituals.BLOOMING_COUNT);
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getDurationProperty() {
    return ModRituals.BLOOMING_DURATION;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusXZProperty() {
    return ModRituals.BLOOMING_RADIUS_XZ;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModRituals.BLOOMING_RADIUS_Y;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getIntervalProperty() {
    return ModRituals.BLOOMING_INTERVAL;
  }
}
