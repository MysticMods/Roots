package mysticmods.roots.mixin.action;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mysticmods.roots.action.CropGrowthAction;
import mysticmods.roots.init.ModActions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BoneMealItem.class)
public class MixinBoneMealItem$GrowAction {
  @WrapOperation(method = "applyBonemeal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/BonemealableBlock;performBonemeal(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"))
  private static void RootsActionApplyBonemeal(BonemealableBlock instance, ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState, Operation<Void> original, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) Player player, @Local BlockState originalState) {
    original.call(instance, serverLevel, randomSource, blockPos, blockState);
    if (player != null && !level.isClientSide()) {
      InteractionHand hand = InteractionHand.MAIN_HAND;
      if (ItemStack.isSameItem(player.getOffhandItem(), stack)) {
        hand = InteractionHand.OFF_HAND;
      }
      BlockState state = level.getBlockState(pos);
      CropGrowthAction.Context context = new CropGrowthAction.Context((ServerLevel) level, (ServerPlayer) player, pos, state, originalState, hand, stack);
      ModActions.CROP_GROWTH.get().accept(context);
      if (originalState.getBlock() instanceof MushroomBlock && !state.is(originalState.getBlock())) {
        ModActions.GROW_HUGE_MUSHROOM.get().accept(context);
      }
    }
  }
}
