package mysticmods.roots.mixin.action;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mysticmods.roots.action.FillCompostAction;
import mysticmods.roots.init.ModActions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ComposterBlock.class)
public class MixinComposterBlock$CompostAction {
  @WrapOperation(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V"))
  private void roots$OnComposterFill(ItemStack instance, int amount, LivingEntity entity, Operation<Void> original, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) InteractionHand hand, @Local(ordinal = 1) BlockState blockstate) {
    if (!(entity instanceof ServerPlayer serverPlayer)) {
      return;
    }
    if (ModActions.FILL_COMPOST.get().shouldTest()) {
      FillCompostAction.Context context = new FillCompostAction.Context(serverPlayer.serverLevel(), serverPlayer, pos, blockstate, state, stack, hand);
      ModActions.FILL_COMPOST.get().accept(context);
    }
    original.call(instance, amount, entity);
  }
}
