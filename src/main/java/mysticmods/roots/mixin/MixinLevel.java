package mysticmods.roots.mixin;

import mysticmods.roots.event.forge.BlockHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class MixinLevel {
  @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z", at = @At(value = "RETURN"))
  private void RootsSetBlock(BlockPos pPos, BlockState pState, int pFlags, int pRecursionLeft, CallbackInfoReturnable<Boolean> cir) {
    Level level = (Level) (Object) this;
    if (!level.isClientSide() && !level.isDebug() && !level.isOutsideBuildHeight(pPos)) {
      BlockHandler.onBlockChanged(level, pPos, pState);
    }
  }
}
