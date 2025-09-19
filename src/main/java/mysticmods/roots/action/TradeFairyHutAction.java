package mysticmods.roots.action;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.action.GroveContext;
import mysticmods.roots.api.action.GroveReputationEntry;
import mysticmods.roots.blockentity.FairyHutBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class TradeFairyHutAction extends GroveAction {
  @Override
  public void log(GroveContext context) {
    RootsAPI.LOG.error("TradeFairyHut triggered by '{}' with offer '{}'", context.player().getName()
        .getString(), context.offer().getResult().getDisplayName().getString());
  }

  @Override
  public boolean test(GroveContext context) {
    return true;
  }

  @Override
  public Set<GroveContext.Parameter> getUsedParameters() {
    return Context.PARAMETERS;
  }

  public record Context(ServerLevel level, ServerPlayer player, FairyHutBlockEntity blockEntity, BlockPos position,
                        BlockState blockState,
                        MerchantOffer offer) implements GroveContext {
    public static final Set<Parameter> PARAMETERS = Set.of(
        GroveContext.LEVEL,
        GroveContext.PLAYER,
        GroveContext.POSITION,
        GroveContext.BLOCK_ENTITY,
        GroveContext.BLOCK_STATE,
        GroveContext.OFFER
    );

    @Override
    public boolean is(GroveReputationEntry.SubEntryType type, ResourceLocation tag) {
      if (type == GroveReputationEntry.SubEntryType.BLOCK) {
        return this.blockState().is(TagKey.create(Registries.BLOCK, tag));
      } else if (type == GroveReputationEntry.SubEntryType.ITEM) {
        return this.offer().getResult().is(TagKey.create(Registries.ITEM, tag));
      }
      return false;
    }
  }
}
