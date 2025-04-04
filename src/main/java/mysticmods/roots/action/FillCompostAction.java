package mysticmods.roots.action;

import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.action.GroveContext;
import mysticmods.roots.api.action.GroveReputationEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class FillCompostAction implements GroveAction {
  @Override
  public boolean test(GroveContext context) {
    return context.blockState().getValue(ComposterBlock.LEVEL) == ComposterBlock.MAX_LEVEL;
  }

  @Override
  public Set<GroveContext.Parameter> getUsedParameters() {
    return Context.PARAMETERS;
  }

  public record Context(ServerLevel level, ServerPlayer player, BlockPos position, BlockState blockState,
                        BlockState oldBlockState, ItemStack item, InteractionHand hand) implements GroveContext {
    public static Set<Parameter> PARAMETERS = Set.of(
        GroveContext.LEVEL,
        GroveContext.PLAYER,
        GroveContext.POSITION,
        GroveContext.BLOCK_STATE,
        GroveContext.OLD_BLOCK_STATE,
        GroveContext.ITEM,
        GroveContext.HAND
    );

    @Override
    public boolean is(GroveReputationEntry.SubEntryType type, ResourceLocation tag) {
      if (type == GroveReputationEntry.SubEntryType.BLOCK) {
        return blockState.is(TagKey.create(Registries.BLOCK, tag));
      } else if (type == GroveReputationEntry.SubEntryType.OLD_BLOCK) {
        return oldBlockState.is(TagKey.create(Registries.BLOCK, tag));
      } else if (type == GroveReputationEntry.SubEntryType.ITEM) {
        return item.is(TagKey.create(Registries.ITEM, tag));
      }
      return false;
    }
  }
}
