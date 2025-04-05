package mysticmods.roots.mixin.action;

import com.llamalad7.mixinextras.sugar.Local;
import mysticmods.roots.action.MilkCowAction;
import mysticmods.roots.init.ModActions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Cow.class)
public class MixinCow {
  @Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"))
  public void RootsOnCowInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir, @Local(ordinal = 0) ItemStack itemstack, @Local(ordinal = 1) ItemStack itemStack1) {
    if (player instanceof ServerPlayer serverPlayer) {
      MilkCowAction.Context context = new MilkCowAction.Context(serverPlayer.serverLevel(), serverPlayer, (Cow) (Object) this, hand, itemstack, itemStack1);
      ModActions.MILK_COW.get().accept(context);
    }
  }
}
