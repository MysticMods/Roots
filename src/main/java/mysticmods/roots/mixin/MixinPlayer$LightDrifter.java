package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mysticmods.roots.init.ModEffects;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public class MixinPlayer$LightDrifter {
  @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;moveCloak()V"))
  private void RootsCancelCloakAnimation(Player instance, Operation<Void> original) {
    if (!instance.hasEffect(ModEffects.LIGHT_DRIFTER)) {
      original.call(instance);
    }
  }
}
