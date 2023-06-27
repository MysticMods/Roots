package mysticmods.roots.worldgen.features.placements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.init.ModFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.stream.Stream;

public class HeightmapYRange extends PlacementModifier {

    public static final Codec<HeightmapYRange> CODEC = RecordCodecBuilder.create((yRangeInstance) -> yRangeInstance.group(
            HeightProvider.CODEC.fieldOf("height").forGetter((range) -> range.minHeightProvider),
            Heightmap.Types.CODEC.fieldOf("heightmapToUse").forGetter((range) -> range.heightmapToUse)
    ).apply(yRangeInstance, HeightmapYRange::new));

    private final HeightProvider minHeightProvider;
    private final Heightmap.Types heightmapToUse;

    public HeightmapYRange(HeightProvider minHeightProvider, Heightmap.Types heightmapToUse) {
        this.minHeightProvider = minHeightProvider;
        this.heightmapToUse = heightmapToUse;
    }

    @Override
    public PlacementModifierType<?> type() {
        return ModFeatures.HEIGHTMAP_Y_RANGE.get();
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext, RandomSource random, BlockPos blockPos) {
        int heightmapY = placementContext.getHeight(heightmapToUse, blockPos.getX(), blockPos.getZ());
        int minY = this.minHeightProvider.sample(random, placementContext);

        int diff = heightmapY - minY;

        if (diff < 1) {
            return Stream.empty();
        }

        int chosenDiff = random.nextInt(diff);
        int chosenFinalY = minY + chosenDiff;

        return Stream.of(blockPos.atY(chosenFinalY));
    }
}
