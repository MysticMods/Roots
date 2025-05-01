package mysticmods.roots.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.attachment.ReputationStorage;
import mysticmods.roots.api.condition.IPlayerCondition;
import mysticmods.roots.api.condition.IPlayerConditionType;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModConditions;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public record GroveRankReputation(Grove grove, int minimumRank) implements IPlayerCondition {
  public static final MapCodec<GroveRankReputation> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(RootsRegistries.GROVES.byNameCodec()
      .fieldOf("grove").forGetter(GroveRankReputation::grove), Codec.INT.fieldOf("minimum_rank")
      .forGetter(GroveRankReputation::minimumRank)).apply(instance, GroveRankReputation::new));
  public static final Codec<GroveRankReputation> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, GroveRankReputation> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.registry(RootsRegistries.Keys.GROVES), GroveRankReputation::grove, ByteBufCodecs.INT, GroveRankReputation::minimumRank, GroveRankReputation::new);

  @Override
  public boolean test(Level level, @NotNull Player player) {
    ReputationStorage rep = player.getData(ModAttachments.REPUTATION_STORAGE);
    return rep.getRank(grove) >= minimumRank;
  }

  @Override
  public String getName() {
    return grove.builtInRegistryHolder().getKey().location().getPath() + "_reputation_rank_" + minimumRank;
  }

  @Override
  public IPlayerConditionType<?> type() {
    return ModConditions.GROVE_RANK_CONDITION_TYPE.get();
  }

  public static class Type implements IPlayerConditionType<GroveRankReputation> {

    @Override
    public Codec<GroveRankReputation> codec() {
      return CODEC;
    }

    @Override
    public MapCodec<GroveRankReputation> mapCodec() {
      return MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, GroveRankReputation> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
