package mysticmods.roots.worldgen.features.placements;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.init.ModFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.stream.Stream;

public class AllAroundLogPlacement extends PlacementModifier {
  private static final AllAroundLogPlacement INSTANCE = new AllAroundLogPlacement();

  public static AllAroundLogPlacement around() {
    return INSTANCE;
  }

  public static MapCodec<AllAroundLogPlacement> CODEC = MapCodec.unit(INSTANCE);

  @Override
  public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
    return Stream.of(pos.north(), pos.south(), pos.east(), pos.west());
  }

  @Override
  public PlacementModifierType<?> type() {
    return ModFeatures.ALL_AROUND_LOG_PLACEMENT.get();
  }
}
