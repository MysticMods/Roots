package mysticmods.roots.item;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.util.ItemUtil;
import mysticmods.roots.util.TooltipUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

public class WoodenShearsItem extends ShearsItem {
  public WoodenShearsItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, net.minecraft.world.InteractionHand hand) {
    if (entity instanceof net.neoforged.neoforge.common.IShearable target) {
      BlockPos pos = entity.blockPosition();
      boolean isClient = entity.level().isClientSide();
      if (target.isShearable(player, stack, entity.level(), pos)) {
        List<ItemStack> drops = ItemUtil.combine(target.onSheared(player, stack, entity.level(), pos));
        if (!isClient) {
          for (ItemStack drop : drops) {
            if (entity.getType().is(RootsTags.Entities.LIMIT_WOODEN_SHEARS_DROPS)) {
              drop.setCount(1);
            }
            target.spawnShearedDrop(entity.level(), pos, drop);
          }
        }
        entity.gameEvent(GameEvent.SHEAR, player);
        if (!isClient) {
          stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        }
        return InteractionResult.sidedSuccess(isClient);
      }
    }
    return InteractionResult.PASS;
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    tooltipComponents.add(TooltipUtil.tooltip(this));
  }
}
