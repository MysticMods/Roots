package mysticmods.roots.action;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.action.GroveContext;
import mysticmods.roots.api.action.GroveReputationEntry;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.ISpellInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;

import java.util.Set;

public class LearnSpellAction extends GroveAction {
  @Override
  public void log(GroveContext context) {
    RootsAPI.LOG.error("LearnSpellAction fired by '{}' with spell '{}'",
        context.player().getName().getString(), context.spell().getSpell().builtInRegistryHolder().getKey().location());
  }

  @Override
  public boolean test(GroveContext context) {
    return true;
  }

  @Override
  public Set<GroveContext.Parameter> getUsedParameters() {
    return Context.PARAMETERS;
  }

  public record Context(ServerLevel level, ServerPlayer player, ISpellInstance spell) implements GroveContext {
    public static final Set<Parameter> PARAMETERS = Set.of(GroveContext.LEVEL, GroveContext.PLAYER, GroveContext.SPELL);

    @Override
    public boolean is(GroveReputationEntry.SubEntryType type, ResourceLocation tag) {
      if (type == GroveReputationEntry.SubEntryType.EXACT_SPELL) {
        return this.spell().getSpell().builtInRegistryHolder().getKey().location().equals(tag);
      } else if (type == GroveReputationEntry.SubEntryType.SPELL) {
        return spell.getSpell().is(TagKey.create(RootsRegistries.Keys.SPELLS, tag));
      }
      return false;
    }
  }
}
