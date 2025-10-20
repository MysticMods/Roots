package mysticmods.roots.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mysticmods.roots.init.ModEffects;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerRenderer.class)
public class MixinPlayerRenderer$LightDrifter {
  @WrapOperation(method = "setModelProperties", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/model/PlayerModel;crouching:Z"))
  private void roots$onSetModelProperties(PlayerModel<AbstractClientPlayer> instance, boolean value, Operation<Void> original, @Local(argsOnly = true) AbstractClientPlayer player) {
    if (!player.hasEffect(ModEffects.LIGHT_DRIFTER)) {
      original.call(instance, value);
    }
  }

  @WrapMethod(method = "getRenderOffset(Lnet/minecraft/client/player/AbstractClientPlayer;F)Lnet/minecraft/world/phys/Vec3;")
  private Vec3 roots$onGetRenderOffset(AbstractClientPlayer entity, float partialTicks, Operation<Vec3> original) {
    if (entity.hasEffect(ModEffects.LIGHT_DRIFTER)) {
      return Vec3.ZERO;
    }
    return original.call(entity, partialTicks);
  }
}
