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
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.Set;

public class TradeVillagerAction implements GroveAction {
  @Override
  public boolean test(GroveContext context) {
    RootsAPI.LOG.error("TradeVillagerAction triggered by '{}' with offer '{}'", context.player().getName().getString(), context.offer().getResult().getDisplayName().getString());
    return true;
  }

  @Override
  public Set<GroveContext.Parameter> getUsedParameters() {
    return Context.PARAMETERS;
  }

  public record Context(ServerLevel level, ServerPlayer player, AbstractVillager target, MerchantOffer offer) implements GroveContext {
    public static final Set<Parameter> PARAMETERS = Set.of(
        GroveContext.LEVEL,
        GroveContext.PLAYER,
        GroveContext.TARGET_ENTITY,
        GroveContext.OFFER
    );

    @Override
    public boolean is(GroveReputationEntry.SubEntryType type, ResourceLocation tag) {
      if (type == GroveReputationEntry.SubEntryType.TARGET_ENTITY) {
        return this.target().getType().is(TagKey.create(Registries.ENTITY_TYPE, tag));
      } else if (type == GroveReputationEntry.SubEntryType.ITEM) {
        return this.offer().getResult().is(TagKey.create(Registries.ITEM, tag));
      }
      return false;
    }
  }
}
