package mysticmods.roots.action;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.action.GroveContext;
import mysticmods.roots.api.action.GroveReputationEntry;
import mysticmods.roots.api.ritual.IRitualInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Set;

public class StartRitualAction implements GroveAction {
  @Override
  public boolean test(GroveContext context) {
    RootsAPI.LOG.error("StartRitualAction fired by '{}' with ritual '{}'",
        context.player().getName().getString(), context.ritual().getRitual().getName().getString());
    return true;
  }

  @Override
  public Set<GroveContext.Parameter> getUsedParameters() {
    return Context.PARAMETERS;
  }

  public record Context(ServerLevel level, ServerPlayer player, IRitualInstance ritual,
                        BlockEntity blockEntity) implements GroveContext {
    public static final Set<Parameter> PARAMETERS = Set.of(GroveContext.LEVEL, GroveContext.PLAYER, GroveContext.RITUAL, GroveContext.BLOCK_ENTITY);

    @Override
    public boolean is(GroveReputationEntry.SubEntryType type, ResourceLocation tag) {
      if (type == GroveReputationEntry.SubEntryType.EXACT_RITUAL) {
        return this.ritual.getRitual().builtInRegistryHolder().getKey().location().equals(tag);
      } else if (type == GroveReputationEntry.SubEntryType.RITUAL) {
        return this.ritual().getRitual().is(tag);
      }
      return false;
    }
  }
}
