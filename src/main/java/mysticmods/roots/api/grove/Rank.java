package mysticmods.roots.api.grove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Locale;

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
  private final MutableComponent shortName;

  Rank() {
    this.name = Component.translatable("roots.grove.ranks." + name().toLowerCase(Locale.ROOT));
    this.shortName = Component.translatable("roots.grove.ranks." + name().toLowerCase(Locale.ROOT) + ".short");
  }

  public MutableComponent getShortName () {
    return this.shortName;
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
      case FIRST -> ranks.threshold1();
      case SECOND -> ranks.threshold2();
      case THIRD -> ranks.threshold3();
      case FOURTH -> ranks.threshold4();
    };
  }

  public int progress(ReputationRanks ranks, int reputation) {
    return reputation - threshold(ranks);
  }

  public int nextThreshold(ReputationRanks ranks) {
    return switch (this.next()) {
      case FIRST -> ranks.threshold1();
      case SECOND -> ranks.threshold2();
      case THIRD -> ranks.threshold3();
      case FOURTH -> ranks.threshold4();
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
    if (reputation >= ranks.threshold4()) {
      return Rank.FOURTH;
    } else if (reputation >= ranks.threshold3()) {
      return Rank.THIRD;
    } else if (reputation >= ranks.threshold2()) {
      return Rank.SECOND;
    } else if (reputation >= ranks.threshold1()) {
      return Rank.FIRST;
    } else {
      return Rank.UNRANKED;
    }
  }
}
