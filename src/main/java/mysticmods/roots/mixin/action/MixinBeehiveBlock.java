package mysticmods.roots.mixin.action;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mysticmods.roots.action.HarvestBeeHiveAction;
import mysticmods.roots.init.ModActions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeehiveBlock.class)
public class MixinBeehiveBlock {
  @WrapOperation(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/stats/Stat;)V"))
  private void RootsBeehiveHarvested(Player player, Stat<?> stat, Operation<Void> original, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) InteractionHand hand) {
    original.call(player, stat);
    if (player instanceof ServerPlayer serverPlayer) {
      HarvestBeeHiveAction.Context context = new HarvestBeeHiveAction.Context(serverPlayer.serverLevel(), serverPlayer, pos, state, stack);
      ModActions.HARVEST_BEE_HIVE.get().accept(context);
    }
  }
}
