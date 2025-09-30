package mysticmods.roots.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mysticmods.roots.client.ClientLightDrifterUtil;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
public class MixinMouseHandler$LightDrifter {
  @WrapOperation(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"))
  private void roots$turnPlayer(LocalPlayer instance, double d0, double d1, Operation<Void> original) {
    original.call(instance, d0, d1);
    ClientLightDrifterUtil.turnPlayer(instance, d0, d1);
  }
}
