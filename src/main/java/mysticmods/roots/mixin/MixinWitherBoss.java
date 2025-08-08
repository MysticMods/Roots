package mysticmods.roots.mixin;

import mysticmods.roots.init.ModEffects;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherBoss.class)
public class MixinWitherBoss {
  @Inject(method="setAlternativeTarget",at=@At("HEAD"),cancellable = true)
  private void roots$setAlternativeTarget(int targetOffset, int newId, CallbackInfo ci) {
    // Handle the "alternative target" system for the Wither in relation to Geas
    if (((WitherBoss) (Object) this).hasEffect(ModEffects.GEAS)) {
      ci.cancel();
    }
  }
}
