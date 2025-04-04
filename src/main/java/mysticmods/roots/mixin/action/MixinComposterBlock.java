package mysticmods.roots.mixin.action;

import com.llamalad7.mixinextras.sugar.Local;
import mysticmods.roots.action.FillCompostAction;
import mysticmods.roots.init.ModActions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ComposterBlock.class)
public class MixinComposterBlock {
  @Inject(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V"))
  private void RootsOnComposterFill(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<ItemInteractionResult> cir, @Local(ordinal = 1) BlockState blockstate) {
    if (!(player instanceof ServerPlayer serverPlayer)) {
      return;
    }
    FillCompostAction.Context context = new FillCompostAction.Context(serverPlayer.serverLevel(), serverPlayer, pos, blockstate, state, stack, hand);
    ModActions.FILL_COMPOST.get().accept(context);
  }
}
