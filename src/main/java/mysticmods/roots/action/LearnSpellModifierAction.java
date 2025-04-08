package mysticmods.roots.action;

import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.action.GroveContext;
import mysticmods.roots.api.action.GroveReputationEntry;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.SpellModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;

import java.util.Set;

public class LearnSpellModifierAction implements GroveAction {
  @Override
  public boolean test(GroveContext context) {
    return true;
  }

  @Override
  public Set<GroveContext.Parameter> getUsedParameters() {
    return Context.PARAMETERS;
  }

  public record Context(ServerLevel level, ServerPlayer player, SpellModifier spellModifier) implements GroveContext {
    public static final Set<Parameter> PARAMETERS = Set.of(GroveContext.LEVEL, GroveContext.PLAYER, GroveContext.SPELL_MODIFIER);

    @Override
    public boolean is(GroveReputationEntry.SubEntryType type, ResourceLocation tag) {
      if (type == GroveReputationEntry.SubEntryType.SPELL) {
        return spellModifier().getSpell().is(TagKey.create(RootsRegistries.Keys.SPELLS, tag));
      } else if (type == GroveReputationEntry.SubEntryType.SPELL_MODIFIER) {
        return spellModifier().is(TagKey.create(RootsRegistries.Keys.SPELL_MODIFIERS, tag));
      }
      return false;
    }
  }
}
