package mysticmods.roots.action;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.action.GroveContext;
import mysticmods.roots.api.action.GroveReputationEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

import java.util.Set;

public class KillEntityAction implements GroveAction {
  @Override
  public boolean test(GroveContext context) {
    RootsAPI.LOG.error("KillEntityAction triggered by '{}' with entity '{}'", context.player().getName().getString(), context.target().getType());
    return true;
  }

  @Override
  public Set<GroveContext.Parameter> getUsedParameters() {
    return Context.PARAMETERS;
  }

  public record Context(ServerLevel level, ServerPlayer player, Entity target, Entity secondary,
                        DamageSource damage) implements GroveContext {
    public static final Set<Parameter> PARAMETERS = Set.of(GroveContext.LEVEL, GroveContext.PLAYER, GroveContext.TARGET_ENTITY, GroveContext.SECONDARY_ENTITY, GroveContext.DAMAGE);

    @Override
    public boolean is(GroveReputationEntry.SubEntryType type, ResourceLocation tag) {
      if (type == GroveReputationEntry.SubEntryType.TARGET_ENTITY) {
        return this.target().getType().is(TagKey.create(Registries.ENTITY_TYPE, tag));
      } else if (type == GroveReputationEntry.SubEntryType.SECONDARY_ENTITY) {
        return this.secondary().getType().is(TagKey.create(Registries.ENTITY_TYPE, tag));
      } else if (type == GroveReputationEntry.SubEntryType.ITEM) {
        return this.item().is(TagKey.create(Registries.ITEM, tag));
      } else if (type == GroveReputationEntry.SubEntryType.DAMAGE) {
        return this.damage().is(TagKey.create(Registries.DAMAGE_TYPE, tag));
      }
      return false;
    }
  }
}
