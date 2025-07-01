package mysticmods.roots.api.grove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

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

  public int getRank(int reputation) {
    if (reputation >= threshold4) {
      return 4;
    } else if (reputation >= threshold3) {
      return 3;
    } else if (reputation >= threshold2) {
      return 2;
    } else if (reputation >= threshold1) {
      return 1;
    } else {
      return 0;
    }
  }

  // Calculates how much reputation you have in the current rank
  public int getCurrentRankProgress(int reputation) {
    if (reputation >= threshold4) {
      return reputation - threshold4;
    } else if (reputation >= threshold3) {
      return reputation - threshold3;
    } else if (reputation >= threshold2) {
      return reputation - threshold2;
    } else if (reputation >= threshold1) {
      return reputation - threshold1;
    } else {
      return reputation;
    }
  }

  // Determines the reputation needed to complete this rank and move to the next
  public int getCurrentRankMax(int reputation) {
    if (reputation >= threshold4) {
      return 0; // already at highest rank
    } else if (reputation >= threshold3) {
      return threshold4 - threshold3;
    } else if (reputation >= threshold2) {
      return threshold3 - threshold2;
    } else if (reputation >= threshold1) {
      return threshold2 - threshold1;
    } else {
      return threshold1;
    }
  }

  public Progress getProgress (int reputation) {
    int rank = getRank(reputation);
    int progress = getCurrentRankProgress(reputation);
    int nextRank = getCurrentRankMax(reputation);
    return new Progress(rank, progress, nextRank, reputation);
  }

  public record Progress (int rank, int progress, int nextRank, int total) {};
}
