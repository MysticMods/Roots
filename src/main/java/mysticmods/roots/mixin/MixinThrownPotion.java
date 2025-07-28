package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.block.PyreBlock;
import mysticmods.roots.blockentity.PyreBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownPotion.class)
public class MixinThrownPotion {
  @Inject(method = "dowseFire", at = @At("TAIL"))
  private void rootsDowseFire(BlockPos pos, CallbackInfo ci, @Local BlockState state) {
    if (state.is(RootsTags.Blocks.PYRES) && state.hasProperty(PyreBlock.ACTIVE) && state.getValue(PyreBlock.ACTIVE) && ((ThrownPotion) (Object) this).level()
        .getBlockEntity(pos) instanceof PyreBlockEntity pyre) {
      pyre.stopRitual(false);
    }
  }
}
