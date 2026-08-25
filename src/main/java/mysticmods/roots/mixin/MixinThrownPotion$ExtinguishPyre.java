package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.block.PyreBlock;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.config.ConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ThrownPotion.class)
public class MixinThrownPotion$ExtinguishPyre {
  @WrapMethod(method = "dowseFire")
  private void roots$DowseFire(BlockPos pos, Operation<Void> original) {
    ThrownPotion potion = (ThrownPotion) (Object) this;

    Level level = potion.level();
    BlockState state = level.getBlockState(pos);
    original.call(pos);
    if (state.is(RootsTags.Blocks.PYRES) && state.hasProperty(PyreBlock.ACTIVE) && state.getValue(PyreBlock.ACTIVE) && ConfigManager.ENABLE_EXTINGUISH_PYRE.getAsBoolean()) {
      if (level.getBlockEntity(pos) instanceof PyreBlockEntity pyre) {
        if (ConfigManager.DEBUG_PYRE.getAsBoolean()) {
          RootsAPI.LOG.info("Stopping ritual on PyreBlockEntity at {} in {} as entity {} used a throwable potion and triggered the 'dowseFire' feature", pos, level.dimension()
              .location(), potion.getOwner());
        }
        pyre.stopRitual(false);
      }
    }
  }
}
