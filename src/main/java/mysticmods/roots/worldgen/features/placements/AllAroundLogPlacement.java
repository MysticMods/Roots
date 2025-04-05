package mysticmods.roots.worldgen.features.placements;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.init.ModFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class AllAroundLogPlacement extends PlacementModifier {
  private static final AllAroundLogPlacement INSTANCE = new AllAroundLogPlacement();
  public static MapCodec<AllAroundLogPlacement> CODEC = MapCodec.unit(INSTANCE);
  private final List<Direction> DIRECTIONS = new ArrayList<>(Arrays.asList(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST));

  public static AllAroundLogPlacement around() {
    return INSTANCE;
  }

  @Override
  public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
    Collections.shuffle(DIRECTIONS);
    for (Direction direction : DIRECTIONS) {
      if (context.getBlockState(pos.relative(direction)).isAir()) {
        return Stream.of(pos.relative(direction));
      }
    }
    return Stream.empty();
  }

  @Override
  public PlacementModifierType<?> type() {
    return ModFeatures.ALL_AROUND_LOG_PLACEMENT.get();
  }
}
