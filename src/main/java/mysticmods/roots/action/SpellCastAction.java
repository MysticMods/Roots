package mysticmods.roots.action;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.action.GroveContext;
import mysticmods.roots.api.action.GroveReputation;
import mysticmods.roots.api.action.GroveReputationEntry;
import mysticmods.roots.api.spell.ParentChargeType;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

/**
 * This action is triggered whenever a spell is successfully cast,
 * and has been charged.
 */
public class SpellCastAction extends GroveAction {
  @Override
  public boolean test(GroveContext context) {
    return context.costing().shouldCharge();
  }

  @Override
  public GroveReputation modify(GroveContext context, GroveReputation reputation) {
    if (context.spell().getChargeType() == ParentChargeType.OPERATION) {
      return reputation.multiply(context.costing().operations());
    }
    return super.modify(context, reputation);
  }

  @Override
  public void log(GroveContext context) {
    RootsAPI.LOG.error("SpellCastAction fired by '{}' with spell '{}'",
        context.player().getName().getString(), context.spell().getName().getString());
  }

  @Override
  public Set<GroveContext.Parameter> getUsedParameters() {
    return Context.PARAMETERS;
  }

  public record Context(ServerLevel level, ServerPlayer player, InteractionHand hand, ItemStack item,
                        ISpellInstance spell, Costing costing) implements GroveContext {
    public static final Set<Parameter> PARAMETERS = Set.of(GroveContext.LEVEL, GroveContext.PLAYER, GroveContext.HAND, GroveContext.ITEM, GroveContext.SPELL, GroveContext.COSTING);

    @Override
    public boolean is(GroveReputationEntry.SubEntryType type, ResourceLocation tag) {
      if (type == GroveReputationEntry.SubEntryType.EXACT_SPELL) {
        // TODO: Test
        return this.spell().is(tag);
      } else if (type == GroveReputationEntry.SubEntryType.SPELL) {
        return this.spell().is(TagKey.create(RootsRegistries.Keys.SPELLS, tag));
      }
      return false;
    }
  }
}