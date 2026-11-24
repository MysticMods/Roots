package mysticmods.roots.api.grove;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.block.StateProperties;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public record RankRange(Optional<Rank> minimumRank, Optional<Rank> maximumRank) {
  private static final MapCodec<Pair<Optional<Rank>, Optional<Rank>>> RAW_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Rank.CODEC.optionalFieldOf("minimum").forGetter(Pair::getFirst),
      Rank.CODEC.optionalFieldOf("maximum").forGetter(Pair::getSecond)
  ).apply(instance, Pair::of));

  public static final MapCodec<RankRange> MAP_CODEC = RAW_CODEC.flatXmap(
      pair -> {
        Optional<Rank> min = pair.getFirst();
        Optional<Rank> max = pair.getSecond();
        if (min.isEmpty() && max.isEmpty()) {
          return DataResult.error(() -> "Minimum and maximum ranks cannot both be empty!");
        }
        if (min.isPresent() && max.isPresent() && min.get().compareTo(max.get()) > 0) {
          return DataResult.error(() -> "Minimum rank " + min.get()
              .getSerializedName() + " is above maximum rank " + max.get().getSerializedName() + "!");
        }
        return DataResult.success(new RankRange(min, max));
      },
      range -> DataResult.success(Pair.of(range.minimumRank(), range.maximumRank()))
  );
  public static final Codec<RankRange> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, RankRange> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.optional(Rank.STREAM_CODEC), RankRange::minimumRank,
      ByteBufCodecs.optional(Rank.STREAM_CODEC), RankRange::maximumRank,
      RankRange::new);

  public RankRange {
    if (minimumRank.isEmpty() && maximumRank.isEmpty()) {
      throw new IllegalStateException("Minimum and maximum ranks cannot both be empty!");
    }
    if (minimumRank.isPresent() && maximumRank.isPresent() && minimumRank.get().compareTo(maximumRank.get()) > 0) {
      throw new IllegalArgumentException("Minimum rank " + minimumRank.get().getSerializedName() + " is above maximum rank " + maximumRank.get().getSerializedName() + "!");
    }
  }

  public RankRange(Rank minimumRank, Rank maximumRank) {
    this(Optional.of(minimumRank), Optional.of(maximumRank));
  }

  public boolean valid(Rank rank) {
    return minimumRank.map(min -> rank.compareTo(min) >= 0)
        .orElse(true) && maximumRank.map(max -> rank.compareTo(max) <= 0).orElse(true);
  }

  public boolean valid(BlockState state) {
    if (!state.hasProperty(StateProperties.GroveStone.RANK)) {
      return false;
    }

    return valid(state.getValue(StateProperties.GroveStone.RANK));
  }

  public Rank minimum() {
    return minimumRank.orElse(Rank.minimum());
  }

  public Rank maximum() {
    return maximumRank.orElse(Rank.maximum());
  }

  public static RankRange minimum(Rank minimumRank) {
    return new RankRange(Optional.of(minimumRank), Optional.empty());
  }

  public static RankRange maximum(Rank maximumRank) {
    return new RankRange(Optional.empty(), Optional.of(maximumRank));
  }
}
