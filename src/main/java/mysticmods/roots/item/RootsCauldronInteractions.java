package mysticmods.roots.item;

import mysticmods.roots.api.RootsTags;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.stats.Stats;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.LayeredCauldronBlock;

public class RootsCauldronInteractions {
  public static final CauldronInteraction CLEAN_POUCH = (state, level, pos, player, hand, stack) -> {
    if (!stack.is(RootsTags.Items.DYEABLE)) {
      return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    } else if (!stack.has(DataComponents.BASE_COLOR)) {
      return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    } else if (stack.get(DataComponents.BASE_COLOR) != DyeColor.BROWN) {
      stack.set(DataComponents.BASE_COLOR, DyeColor.BROWN);
      player.awardStat(Stats.CLEAN_ARMOR);
      LayeredCauldronBlock.lowerFillLevel(state, level, pos);
      return ItemInteractionResult.sidedSuccess(level.isClientSide());
    } else {
      return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
  };
}
