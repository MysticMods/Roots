package mysticmods.roots.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.shaders.Effect;
import mysticmods.roots.client.ClientLightDrifterUtil;
import mysticmods.roots.init.ModEffects;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.effect.MobEffect;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
}
