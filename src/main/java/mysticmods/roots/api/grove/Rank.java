package mysticmods.roots.api.grove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum Rank implements StringRepresentable {
  UNRANKED("unranked"),
  FIRST("first"),
  SECOND("second"),
  THIRD("third"),
  FOURTH("fourth");

  public static final Codec<Rank> CODEC = StringRepresentable.fromEnum(Rank::values);
  public static final MapCodec<Rank> MAP_CODEC = CODEC.fieldOf("rank");
  public static final StreamCodec<ByteBuf, Rank> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(Rank::fromOrdinal, Rank::ordinal);

  private final String name;
  private final MutableComponent componentName;
  private final MutableComponent shortName;

  Rank(String name) {
    this.name = name;
    this.componentName = Component.translatable("roots.grove.ranks." + name);
    this.shortName = Component.translatable("roots.grove.ranks." + name + ".short");
  }

  public MutableComponent getShortName () {
    return this.shortName;
  }

  public MutableComponent getName() {
    return this.componentName;
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

  @Override
  public String getSerializedName() {
    return name;
  }

  public static Rank minimum () {
    return UNRANKED;
  }

  public static Rank maximum () {
    return FOURTH;
  }
}
