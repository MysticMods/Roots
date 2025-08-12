package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mysticmods.roots.api.RootsTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BeehiveBlock.class)
public class MixinBeehiveBlock {
  @WrapOperation(method="useItemOn", at=@At(value="INVOKE", target="Lnet/minecraft/world/level/block/BeehiveBlock;angerNearbyBees(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"))
  private void roots$cancelAngerNearbyBees(BeehiveBlock instance, Level bee, BlockPos list1, Operation<Void> original, @Local(argsOnly = true) ItemStack stack) {
    if (!stack.is(RootsTags.Items.RUNIC_SHEARS)) {
      original.call(instance, bee, list1);
    }
  }

  @WrapOperation(method="useItemOn", at=@At(value="INVOKE", target="Lnet/minecraft/world/level/block/BeehiveBlock;releaseBeesAndResetHoneyLevel(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$BeeReleaseStatus;)V"))
  private void roots$cancelReleaseBeesAndResetHoneyLevel(BeehiveBlock instance, Level level, BlockState state, BlockPos pos, Player player, BeehiveBlockEntity.BeeReleaseStatus status, Operation<Void> original, @Local(argsOnly = true) ItemStack stack) {
    if (!stack.is(RootsTags.Items.RUNIC_SHEARS)) {
      original.call(instance, level, state, pos, player, status);
    } else {
      ((BeehiveBlock) (Object) this).resetHoneyLevel(level, state, pos);
    }
  }
}
