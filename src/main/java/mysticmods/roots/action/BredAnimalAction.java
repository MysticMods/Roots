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
import net.minecraft.world.entity.Entity;

import java.util.Set;

public class BredAnimalAction extends GroveAction {
  @Override
  public void log(GroveContext context) {
    RootsAPI.LOG.error("BredAnimalAction fired by '{}' with offspring '{}' from first parent '{}' and second parent '{}'",
        context.player().getName().getString(), context.target().getName().getString(), context.secondary().getName()
            .getString(), context.tertiary().getName().getString());
  }

  @Override
  public boolean test(GroveContext context) {
    return true;
  }

  @Override
  public Set<GroveContext.Parameter> getUsedParameters() {
    return Context.PARAMETERS;
  }

  public record Context(ServerLevel level, ServerPlayer player, Entity target, Entity secondary,
                        Entity tertiary) implements GroveContext {
    public static Set<Parameter> PARAMETERS = Set.of(GroveContext.LEVEL, GroveContext.PLAYER,
        GroveContext.SECONDARY_ENTITY, GroveContext.TERTIARY_ENTITY);

    @Override
    public boolean is(GroveReputationEntry.SubEntryType type, ResourceLocation tag) {
      return switch (type) {
        case TARGET_ENTITY -> target != null && target.getType().is(TagKey.create(Registries.ENTITY_TYPE, tag));
        case SECONDARY_ENTITY -> secondary.getType().is(TagKey.create(Registries.ENTITY_TYPE, tag));
        case TERTIARY_ENTITY -> tertiary.getType().is(TagKey.create(Registries.ENTITY_TYPE, tag));
        default -> false;
      };
    }
  }
}
