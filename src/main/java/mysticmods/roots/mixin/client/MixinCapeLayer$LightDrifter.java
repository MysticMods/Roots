package mysticmods.roots.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import mysticmods.roots.init.ModEffects;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CapeLayer.class)
public class MixinCapeLayer$LightDrifter {
  @ModifyExpressionValue(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;isCrouching()Z"))
  private boolean roots$LightDrifterCrouching(boolean original, @Local(argsOnly = true) AbstractClientPlayer player) {
    if (player.hasEffect(ModEffects.LIGHT_DRIFTER)) {
      return false;
    }

    return original;
  }
}
