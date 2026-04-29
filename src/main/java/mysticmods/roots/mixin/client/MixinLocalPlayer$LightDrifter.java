package mysticmods.roots.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mysticmods.roots.client.ClientLightDrifterUtil;
import mysticmods.roots.init.ModEffects;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer$LightDrifter {
  @WrapMethod(method = "aiStep")
  public void roots$onAiStep(Operation<Void> original) {
    original.call();
    ClientLightDrifterUtil.aiStep((LocalPlayer) (Object) this);
  }

  @WrapMethod(method = "sendPosition")
  public void roots$syncPosition(Operation<Void> original) {
    original.call();
    ClientLightDrifterUtil.syncPosition((LocalPlayer) (Object) this);
  }

  @WrapMethod(method = "serverAiStep")
  public void roots$onServerAiStep(Operation<Void> original) {
    original.call();
    ClientLightDrifterUtil.serverAiStep((LocalPlayer) (Object) this);
  }

  @WrapMethod(method = "sendIsSprintingIfNeeded")
  public void roots$onSendIsSprintingIfNeeded(Operation<Void> original) {
    if (!((LocalPlayer) (Object) this).hasEffect(ModEffects.LIGHT_DRIFTER)) {
      original.call();
    }
  }

  @WrapMethod(method = "canSpawnSprintParticle")
  public boolean roots$onCanSpawnSprintParticle(Operation<Void> original) {
    if (!((LocalPlayer) (Object) this).hasEffect(ModEffects.LIGHT_DRIFTER)) {
      original.call();
    }
    return false;
  }
}
