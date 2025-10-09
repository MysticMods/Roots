package mysticmods.roots.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mysticmods.roots.init.ModEffects;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;

// This is insufficient.
@Mixin(AbstractClientPlayer.class)
public class MixinAbstractClientPlayer$LightDrifter {
  @WrapMethod(method="isSpectator")
  public boolean roots$onIsSpectator(Operation<Boolean> original) {
    // Return true if the player has the Light Drifter effect, making them behave like a spectator
    if (((AbstractClientPlayer) (Object) this).hasEffect(ModEffects.LIGHT_DRIFTER)) {
      return true;
    }
    return original.call();
  }
}
