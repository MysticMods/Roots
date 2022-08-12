package mysticmods.roots.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class CastingItem extends Item {
  public CastingItem(Properties pProperties) {
    super(pProperties);
  }

  @Override
  public UseAnim getUseAnimation(ItemStack pStack) {
    // TODO: Bow?
    return UseAnim.BOW;
  }

  @Override
  public int getUseDuration(ItemStack pStack) {
    return 72000;
  }

  @Override
  public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
    super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
    pPlayer.startUsingItem(pUsedHand);
    return super.use(pLevel, pPlayer, pUsedHand);
  }
}
