package mysticmods.roots.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModAdvancements;
import mysticmods.roots.util.EntityUtils;
import mysticmods.roots.util.PacifistUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;

import java.util.Optional;

public class PacifistTrigger extends SimpleCriterionTrigger<PacifistTrigger.TriggerInstance> {
  @Override
  public Codec<PacifistTrigger.TriggerInstance> codec() {
    return TriggerInstance.CODEC;
  }

  public void trigger(ServerPlayer player, Entity entity) {
    this.trigger(player, (instance) -> instance.test(player, entity));
  }

  public record TriggerInstance(
      Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
    public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(codec ->
        codec.group(ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player))
            .apply(codec, TriggerInstance::new));

    public boolean test(ServerPlayer serverPlayer, Entity entity) {
      return PacifistUtil.test(serverPlayer, entity);
    }
  }

  public static Criterion<TriggerInstance> pacifist() {
    return ModAdvancements.PACIFIST.get().createCriterion(new TriggerInstance(Optional.empty()));
  }
}
