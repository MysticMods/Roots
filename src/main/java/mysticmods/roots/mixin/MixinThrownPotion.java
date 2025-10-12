package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.block.PyreBlock;
import mysticmods.roots.blockentity.PyreBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ThrownPotion.class)
public class MixinThrownPotion {
  @WrapMethod(method = "dowseFire")
  private void rootsDowseFire(BlockPos pos, Operation<Void> original, @Local BlockState state) {
    original.call(pos);
    if (state.is(RootsTags.Blocks.PYRES) && state.hasProperty(PyreBlock.ACTIVE) && state.getValue(PyreBlock.ACTIVE)) {
      if (((ThrownPotion) (Object) this).level().getBlockEntity(pos) instanceof PyreBlockEntity pyre) {
        pyre.stopRitual(false);
      }
    }
  }
}
