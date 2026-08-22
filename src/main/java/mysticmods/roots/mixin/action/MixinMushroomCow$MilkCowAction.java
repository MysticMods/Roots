package mysticmods.roots.mixin.action;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mysticmods.roots.action.MilkCowAction;
import mysticmods.roots.init.ModActions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MushroomCow.class)
public class MixinMushroomCow$MilkCowAction {
  @WrapOperation(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"))
  private void roots$OnMilkMushroomCow(Player player, InteractionHand hand, ItemStack itemStack, Operation<Void> original, @Local(ordinal = 0) ItemStack itemstack, @Local(ordinal = 2) ItemStack itemstack1) {
    if (player instanceof ServerPlayer serverPlayer && ModActions.MILK_COW.get().shouldTest()) {
      MilkCowAction.Context context = new MilkCowAction.Context(serverPlayer.serverLevel(), serverPlayer, (MushroomCow) (Object) this, hand, itemstack, itemstack1);
      ModActions.MILK_COW.get().accept(context);
    }
    original.call(player, hand, itemStack);
  }
}
