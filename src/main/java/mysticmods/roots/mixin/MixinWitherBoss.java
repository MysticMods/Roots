package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mysticmods.roots.init.ModEffects;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherBoss.class)
public class MixinWitherBoss {
  @WrapMethod(method = "setAlternativeTarget")
  private void roots$setAlternativeTarget(int targetOffset, int newId, Operation<Void> original) {
    // Handle the "alternative target" system for the Wither in relation to Geas
    if (!((WitherBoss) (Object) this).hasEffect(ModEffects.GEAS)) {
      original.call(targetOffset, newId);
    }
  }
}
