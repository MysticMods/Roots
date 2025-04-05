package mysticmods.roots.action;


import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.action.GroveContext;
import mysticmods.roots.api.action.GroveReputationEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Set;

public class ArriveDimensionAction implements GroveAction {
  @Override
  public boolean test(GroveContext context) {
    RootsAPI.LOG.error("ArriveDimensionAction fired by '{}' in dimension '{}'", context.player().getName()
        .getString(), context.level().dimension().location());
    return true;
  }

  @Override
  public Set<GroveContext.Parameter> getUsedParameters() {
    return Context.PARAMETERS;
  }

  public record Context(ServerLevel level, ServerPlayer player) implements GroveContext {
    public static final Set<Parameter> PARAMETERS = Set.of(GroveContext.LEVEL, GroveContext.PLAYER);

    @Override
    public boolean is(GroveReputationEntry.SubEntryType type, ResourceLocation tag) {
      if (type == GroveReputationEntry.SubEntryType.DIMENSION) {
        return level.dimension().location().equals(tag);
      }
      return false;
    }
  }
}
