package mysticmods.roots.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.InputConstants;
import mysticmods.roots.client.KeyBindings;
import mysticmods.roots.init.ModEffects;
import net.minecraft.Util;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(KeyboardHandler.class)
public class MixinKeyboardHandler$LightDrifter {
  @Unique
  private long roots_1_21$lightDrifterCancel = -1L;

  @WrapOperation(method = "keyPress", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/InputConstants;isKeyDown(JI)Z", ordinal = 0))
  private boolean roots$onKeyPress(long window, int key, Operation<Boolean> original) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player != null && mc.screen == null && mc.player.hasEffect(ModEffects.LIGHT_DRIFTER)) {
      if (InputConstants.isKeyDown(window, KeyBindings.CANCEL_LIGHT_DRIFTER.getKey().getValue())) {
        roots_1_21$lightDrifterCancel = Util.getMillis();
      } else {
        roots_1_21$lightDrifterCancel = -1L;
      }
    } else {
      roots_1_21$lightDrifterCancel = -1L;
    }
    return original.call(window, key);
  }

  @WrapMethod(method="tick")
  private void roots$tick (Operation<Void> original) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player != null && mc.screen == null && mc.player.hasEffect(ModEffects.LIGHT_DRIFTER)) {
      if (roots_1_21$lightDrifterCancel > 0L) {
        long i = Util.getMillis();
        if (10000L - (i - roots_1_21$lightDrifterCancel) < 0L) {
          // TODO: Do the actual cancel
        }
      }
    }
    original.call();
  }
}
