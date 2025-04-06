package mysticmods.roots.action;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.action.GroveContext;
import mysticmods.roots.api.action.GroveReputationEntry;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class ShatterBlockAction implements GroveAction {
  @Override
  public boolean test(GroveContext context) {
    RootsAPI.LOG.error("ShatterBlockAction triggered by '{}' at '{}' with block '{}'", context.player().getName().getString(), context.position(), context.blockState());
    return true;
  }

  @Override
  public Set<GroveContext.Parameter> getUsedParameters() {
    return Context.PARAMETERS;
  }

  public record Context (ServerLevel level, ServerPlayer player, BlockPos position, BlockState blockState, ISpellInstance spell) implements GroveContext {
    public static final Set<Parameter> PARAMETERS = Set.of(GroveContext.LEVEL, GroveContext.PLAYER, GroveContext.POSITION, GroveContext.BLOCK_STATE, GroveContext.SPELL);

    @Override
    public boolean is(GroveReputationEntry.SubEntryType type, ResourceLocation tag) {
      if (type == GroveReputationEntry.SubEntryType.BLOCK) {
        return this.blockState().is(TagKey.create(Registries.BLOCK, tag));
      } else if (type == GroveReputationEntry.SubEntryType.SPELL) {
        return this.spell().getSpell().is(TagKey.create(RootsRegistries.Keys.SPELLS, tag));
      }
      return false;
    }
  }
}
