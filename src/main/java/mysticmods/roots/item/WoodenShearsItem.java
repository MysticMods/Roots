package mysticmods.roots.item;

import mysticmods.roots.api.RootsTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
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
      // Check isShearable on both sides (mirrors vanilla readyForShearing())
      if (target.isShearable(player, stack, entity.level(), pos)) {
        // Call onSheared on both sides (mirrors vanilla shear())
        List<ItemStack> drops = target.onSheared(player, stack, entity.level(), pos);
        // Spawn drops on the server side using spawnShearedDrop to retain vanilla mob-specific behavior
        if (!isClient) {
          for(ItemStack drop : drops) {
            if (entity.getType().is(RootsTags.Entities.LIMIT_WOODEN_SHEARS_DROPS)) {
              drop.setCount(1);
            }
            target.spawnShearedDrop(entity.level(), pos, drop);
          }
        }
        // Call GameEvent.SHEAR on both sides
        entity.gameEvent(GameEvent.SHEAR, player);
        // Damage the shear item stack by 1 on the server side
        if (!isClient) {
          stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        }
        // Return sided success if the entity was shearable
        return InteractionResult.sidedSuccess(isClient);
      }
    }
    return InteractionResult.PASS;
  }
}
