package mysticmods.roots.mixin;

/*
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
*/
