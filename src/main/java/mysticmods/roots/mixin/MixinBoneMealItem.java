package mysticmods.roots.mixin;

import mysticmods.roots.action.CropGrowthAction;
import mysticmods.roots.init.ModActions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BoneMealItem.class)
public class MixinBoneMealItem {
  @Inject(method="applyBonemeal",at=@At(value="INVOKE", target="Lnet/minecraft/world/level/block/BonemealableBlock;performBonemeal(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", shift= At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
  private static void applyBonemeal(ItemStack stack, Level level, BlockPos pos, Player player, CallbackInfoReturnable<Boolean> cir, BlockState originalState) {
    if (player == null || level.isClientSide()) {
      return;
    }

    InteractionHand hand = InteractionHand.MAIN_HAND;
    if (ItemStack.isSameItem(player.getOffhandItem(), stack)) {
      hand = InteractionHand.OFF_HAND;
    }
    BlockState state = level.getBlockState(pos);
    CropGrowthAction.Context context = new CropGrowthAction.Context((ServerLevel) level, (ServerPlayer) player, pos, state, originalState, hand, stack);
    ModActions.CROP_GROWTH.get().accept(context);
  }
}
