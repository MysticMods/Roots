package mysticmods.roots.worldgen.features.placements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.init.ModFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class DimensionPlacement extends PlacementFilter {
  public static final Codec<DimensionPlacement> CODEC = RecordCodecBuilder.create((codec) -> codec.group(
      ResourceLocation.CODEC.listOf().fieldOf("dimensions").forGetter(o -> o.dimensions.stream().map(ResourceKey::location).collect(Collectors.toList()))).apply(codec, (r) -> new DimensionPlacement(r.stream().map(o -> ResourceKey.create(Registry.DIMENSION_REGISTRY, o)).collect(Collectors.toSet()))));

  private final Set<ResourceKey<Level>> dimensions;

  protected DimensionPlacement(Set<ResourceKey<Level>> dimensions) {
    this.dimensions = dimensions;
  }

  public static DimensionPlacement of(Set<ResourceKey<Level>> dimensions) {
    return new DimensionPlacement(dimensions);
  }

  @Override
  protected boolean shouldPlace(PlacementContext pContext, RandomSource pRandom, BlockPos pPos) {
    ServerLevel level = pContext.getLevel().getLevel();
    return dimensions.contains(level.dimension());
  }

  @Override
  public PlacementModifierType<?> type() {
    return ModFeatures.DIMENSION_PLACEMENT.get();
  }

  public static class Type implements PlacementModifierType<DimensionPlacement> {
    @Override
    public Codec<DimensionPlacement> codec() {
      return DimensionPlacement.CODEC;
    }
  }
}
