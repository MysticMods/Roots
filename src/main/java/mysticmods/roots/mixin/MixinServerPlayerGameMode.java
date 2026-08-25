package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mysticmods.roots.spell.ShatterSpell;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerGameMode.class)
public class MixinServerPlayerGameMode {
  @WrapOperation(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayerGameMode;isCreative()Z"))
  private boolean roots$checkShatterCreative(ServerPlayerGameMode instance, Operation<Boolean> original) {
    boolean result = original.call(instance);
    if (result && ShatterSpell.IS_CASTING_SHATTER) {
      return false;
    }
    return result;
  }
}
