package mysticmods.roots.item.util;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.stats.Stats;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.level.block.LayeredCauldronBlock;

public class RootsCauldronInteractions {
  public static final CauldronInteraction CLEAN_POUCH = (state, level, pos, player, hand, stack) -> {
    if (!stack.is(RootsTags.Items.DYEABLE)) {
      return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    } else if (!stack.has(ModAttachments.DYEABLE)) {
      return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    } else if (stack.get(ModAttachments.DYEABLE) != DyeableWithDefault.DEFAULT) {
      stack.set(ModAttachments.DYEABLE, DyeableWithDefault.DEFAULT);
      player.awardStat(Stats.CLEAN_ARMOR);
      LayeredCauldronBlock.lowerFillLevel(state, level, pos);
      return ItemInteractionResult.sidedSuccess(level.isClientSide());
    } else {
      return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
  };
}
