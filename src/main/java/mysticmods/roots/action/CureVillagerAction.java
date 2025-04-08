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
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;

import java.util.Set;

public class CureVillagerAction implements GroveAction {
  @Override
  public boolean test(GroveContext context) {
    RootsAPI.LOG.error("CureVillageAction triggered by '{}' for entity '{}'", context.player().getName().getString(), context.target().getName().getString());
    return true;
  }

  @Override
  public Set<GroveContext.Parameter> getUsedParameters() {
    return Context.PARAMETERS;
  }

  public record Context(ServerLevel level, ServerPlayer player, Villager target,
                        Zombie secondary) implements GroveContext {
    public static final Set<Parameter> PARAMETERS = Set.of(
        GroveContext.LEVEL, GroveContext.PLAYER, GroveContext.TARGET_ENTITY, GroveContext.SECONDARY_ENTITY
    );

    @Override
    public boolean is(GroveReputationEntry.SubEntryType type, ResourceLocation tag) {
      if (type == GroveReputationEntry.SubEntryType.TARGET_ENTITY) {
        return target.getType().is(TagKey.create(Registries.ENTITY_TYPE, tag));
      } else if (type == GroveReputationEntry.SubEntryType.SECONDARY_ENTITY) {
        return secondary.getType().is(TagKey.create(Registries.ENTITY_TYPE, tag));
      } else {
        return false;
      }
    }
  }
}
