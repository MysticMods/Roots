package mysticmods.roots.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.attachment.ReputationStorage;
import mysticmods.roots.api.condition.*;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.Rank;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record GroveRankReputation(Grove grove, Rank minimumRank) implements IPlayerCondition, IRankCondition {
  public static final MapCodec<GroveRankReputation> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(RootsRegistries.GROVES.byNameCodec()
      .fieldOf("grove").forGetter(GroveRankReputation::grove), Rank.CODEC.fieldOf("minimum_rank")
      .forGetter(GroveRankReputation::minimumRank)).apply(instance, GroveRankReputation::new));

  @Override
  public boolean test(Level level, @NotNull Player player) {
    ReputationStorage rep = player.getData(ModAttachments.REPUTATION_STORAGE);
    return rep.getRank(grove).compareTo(minimumRank) >= 0;
  }

  @Override
  public MapCodec<? extends ICondition> codec() {
    return MAP_CODEC;
  }

  @Override
  public @Nullable IConditionIngredient getIngredient(Holder<ICondition> condition) {
    return null;
  }
}
