package mysticmods.roots.action;

import mysticmods.roots.api.action.GroveContext;
import mysticmods.roots.api.ritual.IRitualInstance;
import mysticmods.roots.api.spell.ISpellInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Set;

public record CropGrowthContext(ServerLevel level, ServerPlayer player, BlockPos position, BlockState blockState,
                                BlockState oldBlockState, InteractionHand hand, ItemStack item,
                                @Nullable ISpellInstance spell, @Nullable
                                IRitualInstance ritual) implements GroveContext {
  public static final Set<Parameter> PARAMTERS = Set.of(GroveContext.LEVEL, GroveContext.PLAYER, GroveContext.POSITION,
          GroveContext.BLOCK_STATE, GroveContext.OLD_BLOCK_STATE, GroveContext.HAND, GroveContext.ITEM);

  public CropGrowthContext(ServerLevel level, ServerPlayer player, BlockPos position, BlockState blockState, BlockState oldBlockState, InteractionHand hand, ItemStack item) {
    this(level, player, position, blockState, oldBlockState, hand, item, null, null);
  }
}
