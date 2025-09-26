package mysticmods.roots.mixin.client;

import mysticmods.roots.client.ClientLightDrifterUtil;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer$LightDrifter {
  @Inject(method="aiStep",at=@At(value = "INVOKE", target="Lnet/minecraft/client/player/AbstractClientPlayer;aiStep()V"))
  public void roots$onAiStep(CallbackInfo ci) {
    ClientLightDrifterUtil.aiStep((LocalPlayer) (Object) this);
  }

  @Inject(method="sendPosition",at=@At("TAIL"))
  public void roots$onSendPosition(CallbackInfo ci) {
    ClientLightDrifterUtil.syncPosition((LocalPlayer) (Object) this);
  }

  @Inject(method="serverAiStep", at=@At("TAIL"))
  public void roots$onServerAiStep(CallbackInfo ci) {
    ClientLightDrifterUtil.serverAiStep((LocalPlayer) (Object) this);
  }
}
