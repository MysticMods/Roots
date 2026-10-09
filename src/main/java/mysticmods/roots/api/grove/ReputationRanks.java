package mysticmods.roots.api.grove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Locale;

public record ReputationRanks(int threshold1, int threshold2, int threshold3, int threshold4) {
  public static MapCodec<ReputationRanks> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
      instance.group(Codec.INT.fieldOf("threshold1").forGetter(ReputationRanks::threshold1),
              Codec.INT.fieldOf("threshold2").forGetter(ReputationRanks::threshold2),
              Codec.INT.fieldOf("threshold3").forGetter(ReputationRanks::threshold3),
              Codec.INT.fieldOf("threshold4").forGetter(ReputationRanks::threshold4))
          .apply(instance, ReputationRanks::new));
  public static Codec<ReputationRanks> CODEC = MAP_CODEC.codec();
  public static StreamCodec<ByteBuf, ReputationRanks> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ReputationRanks::threshold1,
      ByteBufCodecs.VAR_INT, ReputationRanks::threshold2,
      ByteBufCodecs.VAR_INT, ReputationRanks::threshold3,
      ByteBufCodecs.VAR_INT, ReputationRanks::threshold4, ReputationRanks::new);

  public Progress getProgress(int reputation) {
    Rank rank = Rank.fromRanks(this, reputation);
    int progress = rank.progress(this, reputation);
    int nextRank = rank.nextThreshold(this) - rank.threshold(this);
    return new Progress(rank, progress, nextRank, reputation);
  }

  public record Progress(Rank rank, int progress, int nextRank, int total) {
  }

  public enum Rank {
    UNRANKED,
    FIRST,
    SECOND,
    THIRD,
    FOURTH;

    public static final MapCodec<Rank> MAP_CODEC = Codec.INT.fieldOf("rank").xmap(Rank::fromOrdinal, Rank::ordinal);
    public static final Codec<Rank> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<ByteBuf, Rank> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(Rank::fromOrdinal, Rank::ordinal);

    private final MutableComponent name;

    Rank() {
      this.name = Component.translatable("roots.grove.ranks." + name().toLowerCase(Locale.ROOT));
    }

    public MutableComponent getName() {
      return this.name;
    }

    public static Rank fromOrdinal(int rank) {
      return switch (rank) {
        case 1 -> FIRST;
        case 2 -> SECOND;
        case 3 -> THIRD;
        case 4 -> FOURTH;
        default -> UNRANKED;
      };
    }

    public int threshold(ReputationRanks ranks) {
      return switch (this) {
        case UNRANKED -> 0;
        case FIRST -> ranks.threshold1;
        case SECOND -> ranks.threshold2;
        case THIRD -> ranks.threshold3;
        case FOURTH -> ranks.threshold4;
      };
    }

    public int progress(ReputationRanks ranks, int reputation) {
      return reputation - threshold(ranks);
    }

    public int nextThreshold(ReputationRanks ranks) {
      return switch (this.next()) {
        case FIRST -> ranks.threshold1;
        case SECOND -> ranks.threshold2;
        case THIRD -> ranks.threshold3;
        case FOURTH -> ranks.threshold4;
        case UNRANKED -> 0;
      };
    }

    public Rank next() {
      return switch (this) {
        case UNRANKED -> FIRST;
        case FIRST -> SECOND;
        case SECOND -> THIRD;
        case THIRD, FOURTH -> FOURTH;
      };
    }

    public static Rank fromRanks(ReputationRanks ranks, int reputation) {
      if (reputation >= ranks.threshold4) {
        return Rank.FOURTH;
      } else if (reputation >= ranks.threshold3) {
        return Rank.THIRD;
      } else if (reputation >= ranks.threshold2) {
        return Rank.SECOND;
      } else if (reputation >= ranks.threshold1) {
        return Rank.FIRST;
      } else {
        return Rank.UNRANKED;
      }
    }
  }
}
