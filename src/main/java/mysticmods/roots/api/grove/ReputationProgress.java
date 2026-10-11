package mysticmods.roots.api.grove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ReputationProgress(Rank rank, int progressCurrentRank, int nextRankRequirement, int total) {
  public static final MapCodec<ReputationProgress> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Rank.CODEC.fieldOf("rank")
      .forGetter(ReputationProgress::rank), Codec.INT.fieldOf("progressCurrentRank")
      .forGetter(ReputationProgress::progressCurrentRank), Codec.INT.fieldOf("nextRankRequirement")
      .forGetter(ReputationProgress::nextRankRequirement), Codec.INT.fieldOf("total")
      .forGetter(ReputationProgress::total)).apply(instance, ReputationProgress::new));
  public static final Codec<ReputationProgress> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, ReputationProgress> STREAM_CODEC = StreamCodec.composite(Rank.STREAM_CODEC, ReputationProgress::rank,
      ByteBufCodecs.VAR_INT, ReputationProgress::progressCurrentRank,
      ByteBufCodecs.VAR_INT, ReputationProgress::nextRankRequirement,
      ByteBufCodecs.VAR_INT, ReputationProgress::total, ReputationProgress::new);

  public int percentage() {
    return (int) (((float) progressCurrentRank / (float) nextRankRequirement) * 100.0f);
  }
}
