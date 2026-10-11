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

  public ReputationProgress getProgress(int reputation) {
    Rank rank = Rank.fromRanks(this, reputation);
    int progress = rank.progress(this, reputation);
    int nextRank = rank.nextThreshold(this) - rank.threshold(this);
    return new ReputationProgress(rank, progress, nextRank, reputation);
  }
}
