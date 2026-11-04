package mysticmods.roots.worldgen.trees;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.init.ModFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class MinLobeFancyTrunkPlacer extends FancyTrunkPlacer {
  public static final MapCodec<MinLobeFancyTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance ->
      trunkPlacerParts(instance).and(instance.group(
          Codec.intRange(1, 16).optionalFieldOf("min_lobes", 4).forGetter(p -> p.minLobes),
          Codec.intRange(1, 16).optionalFieldOf("lobe_spacing", 3).forGetter(p -> p.lobeSpacing)
      )).apply(instance, MinLobeFancyTrunkPlacer::new));

  private static final int CANDIDATE_ANGLES = 16;
  private static final int EXTRA_REACH = 3;

  private final int minLobes;
  private final int lobeSpacing;

  public MinLobeFancyTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, int minLobes, int lobeSpacing) {
    super(baseHeight, heightRandA, heightRandB);
    this.minLobes = minLobes;
    this.lobeSpacing = lobeSpacing;
  }

  @Override
  protected TrunkPlacerType<?> type() {
    return ModFeatures.MIN_LOBE_FANCY_TRUNK_PLACER.get();
  }

  @Override
  public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter,
                                                          RandomSource random, int freeTreeHeight, BlockPos pos, TreeConfiguration config) {
    List<FoliagePlacer.FoliageAttachment> attachments =
        new ArrayList<>(super.placeTrunk(level, blockSetter, random, freeTreeHeight, pos, config));
    List<BlockPos> positions = new ArrayList<>(attachments.size() + minLobes);
    for (FoliagePlacer.FoliageAttachment attachment : attachments) {
      positions.add(attachment.pos());
    }
    int lobes = countLobes(positions, lobeSpacing);
    if (lobes >= minLobes) {
      return attachments;
    }

    int height = freeTreeHeight + 2;
    int trunkTop = pos.getY() + Mth.floor((double) height * 0.618);
    int low = Mth.ceil((float) height * 0.3F) - 1;
    int high = height - 5;
    if (high < low) {
      low = 1;
      high = Math.max(1, height - 5);
    }
    int minBase = pos.getY() + Mth.ceil((double) height * 0.2);
    double startAngle = random.nextDouble() * Math.PI * 2.0;

    while (lobes < minLobes) {
      BlockPos bestEnd = null;
      BlockPos bestBase = null;
      int bestScore = -1;
      for (int reach = lobeSpacing; reach <= lobeSpacing + EXTRA_REACH; reach++) {
        for (int y = high; y >= low; y--) {
          for (int m = 0; m < CANDIDATE_ANGLES; m++) {
            double angle = startAngle + m * Math.PI * 2.0 / CANDIDATE_ANGLES;
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);
            double scale = reach / Math.max(Math.abs(cos), Math.abs(sin));
            BlockPos end = pos.offset(Mth.floor(cos * scale + 0.5), y, Mth.floor(sin * scale + 0.5));

            int score = Integer.MAX_VALUE;
            for (BlockPos p : positions) {
              score = Math.min(score, chebyshev(p, end));
            }
            if (score <= bestScore) {
              continue;
            }

            int dx = pos.getX() - end.getX();
            int dz = pos.getZ() - end.getZ();
            double slope = (double) end.getY() - Math.sqrt((double) (dx * dx + dz * dz)) * 0.381;
            int branchBase = slope > (double) trunkTop ? trunkTop : (int) slope;
            branchBase = Math.min(Math.max(branchBase, minBase), Math.min(end.getY(), trunkTop));
            BlockPos base = new BlockPos(pos.getX(), branchBase, pos.getZ());

            if (!limb(level, blockSetter, random, end, end.above(5), false, config)) {
              continue;
            }
            if (!limb(level, blockSetter, random, base, end, false, config)) {
              continue;
            }
            bestEnd = end;
            bestBase = base;
            bestScore = score;
          }
        }
        if (bestScore >= lobeSpacing) {
          break; // shortest reach that gives a separate lobe
        }
      }
      if (bestEnd == null || bestScore < lobeSpacing) {
        break; // no room for another separate lobe
      }
      limb(level, blockSetter, random, bestBase, bestEnd, true, config);
      attachments.add(new FoliagePlacer.FoliageAttachment(bestEnd, 0, false));
      positions.add(bestEnd);
      lobes++;
    }
    return attachments;
  }

  private static int countLobes(List<BlockPos> positions, int spacing) {
    int n = positions.size();
    int[] parent = new int[n];
    for (int i = 0; i < n; i++) {
      parent[i] = i;
    }
    int clusters = n;
    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        if (chebyshev(positions.get(i), positions.get(j)) < spacing) {
          int a = find(parent, i);
          int b = find(parent, j);
          if (a != b) {
            parent[a] = b;
            clusters--;
          }
        }
      }
    }
    return clusters;
  }

  private static int find(int[] parent, int i) {
    while (parent[i] != i) {
      parent[i] = parent[parent[i]];
      i = parent[i];
    }
    return i;
  }

  private static int chebyshev(BlockPos a, BlockPos b) {
    return Math.max(Math.abs(a.getX() - b.getX()), Math.max(Math.abs(a.getY() - b.getY()), Math.abs(a.getZ() - b.getZ())));
  }

  private boolean limb(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random,
                       BlockPos basePos, BlockPos offsetPos, boolean modifyWorld, TreeConfiguration config) {
    if (!modifyWorld && Objects.equals(basePos, offsetPos)) {
      return true;
    }
    BlockPos delta = offsetPos.offset(-basePos.getX(), -basePos.getY(), -basePos.getZ());
    int steps = getSteps(delta);
    float fx = (float) delta.getX() / (float) steps;
    float fy = (float) delta.getY() / (float) steps;
    float fz = (float) delta.getZ() / (float) steps;
    for (int j = 0; j <= steps; j++) {
      BlockPos at = basePos.offset(Mth.floor(0.5F + (float) j * fx), Mth.floor(0.5F + (float) j * fy), Mth.floor(0.5F + (float) j * fz));
      if (modifyWorld) {
        placeLog(level, blockSetter, random, at, config, state -> state.trySetValue(RotatedPillarBlock.AXIS, getLogAxis(basePos, at)));
      } else if (!isFree(level, at)) {
        return false;
      }
    }
    return true;
  }

  private static int getSteps(BlockPos pos) {
    return Math.max(Mth.abs(pos.getX()), Math.max(Mth.abs(pos.getY()), Mth.abs(pos.getZ())));
  }

  private static Direction.Axis getLogAxis(BlockPos pos, BlockPos otherPos) {
    int dx = Math.abs(otherPos.getX() - pos.getX());
    int dz = Math.abs(otherPos.getZ() - pos.getZ());
    int max = Math.max(dx, dz);
    if (max > 0) {
      return dx == max ? Direction.Axis.X : Direction.Axis.Z;
    }
    return Direction.Axis.Y;
  }
}
