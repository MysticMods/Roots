package mysticmods.roots.api.grove.power.consumer;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.IGroveInstance;
import mysticmods.roots.api.grove.Rank;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record PowerAccumulationRequest (TagKey<Grove> tagForPower, int totalRequired, int rate, @Nullable ValueForRank rankedRate){
  public static final MapCodec<PowerAccumulationRequest> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
      instance.group(TagKey.codec(RootsRegistries.Keys.GROVES).fieldOf("tag")
                  .forGetter(o -> o.tagForPower), Codec.INT.fieldOf("totalRequired")
                  .forGetter(o -> o.totalRequired),
              Codec.either(Codec.INT, ValueForRank.CODEC).fieldOf("rate")
                  .forGetter(o -> o.rankedRate == null ? Either.left(o.rate) : Either.right(o.rankedRate)))
          .apply(instance, PowerAccumulationRequest::new));
  public static final Codec<PowerAccumulationRequest> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, PowerAccumulationRequest> STREAM_CODEC = StreamCodec.composite(
      ExtraStreamCodecs.GROVE_TAG_STREAM_CODEC, o -> o.tagForPower,
      ByteBufCodecs.VAR_INT, o -> o.totalRequired,
      ByteBufCodecs.either(ByteBufCodecs.VAR_INT, ValueForRank.STREAM_CODEC), o -> o.rankedRate != null ? Either.right(o.rankedRate) : Either.left(o.rate),
      PowerAccumulationRequest::new
  );
  public static final Codec<List<PowerAccumulationRequest>> LIST_CODEC = CODEC.listOf();
  public static final StreamCodec<RegistryFriendlyByteBuf, List<PowerAccumulationRequest>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

  public PowerAccumulationRequest(PowerRequest request) {
    this(request.tag(), request.value(), request.value());
  }

  public PowerAccumulationRequest(TagKey<Grove> tagForPower, int totalRequired, int rate) {
    this(tagForPower, totalRequired, rate, null);
  }

  public PowerAccumulationRequest(TagKey<Grove> groveTagKey, int total, Either<Integer, ValueForRank> integerValueForRankEither) {
    this(groveTagKey, total, integerValueForRankEither.left().orElse(-1), integerValueForRankEither.right()
        .orElse(null));
  }

  public int rate(IGroveInstance grove) {
    if (rankedRate != null) {
      return rankedRate.value(grove.getRank());
    }

    return rate;
  }

  public PowerRequest request(IGroveInstance grove) {
    return new PowerRequest(tagForPower, Math.min(rate(grove), totalRequired));
  }

  public record ValueForRank(int unranked, int first, int second, int third, int fourth) {
    public static final Codec<ValueForRank> CODEC = Codec.INT.listOf(5, 5)
        .xmap(ValueForRank::new, ValueForRank::asList);
    public static final StreamCodec<ByteBuf, ValueForRank> STREAM_CODEC = ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list(5))
        .map(ValueForRank::new, ValueForRank::asList);

    public ValueForRank(List<Integer> values) {
      this(values.get(0), values.get(1), values.get(2), values.get(3), values.get(4));
    }

    public List<Integer> asList() {
      return List.of(unranked, first, second, third, fourth);
    }

    public int value(Rank rank) {
      return switch (rank) {
        case UNRANKED -> unranked;
        case FIRST -> first;
        case SECOND -> second;
        case THIRD -> third;
        case FOURTH -> fourth;
      };
    }
  }
}
