package mysticmods.roots.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mysticmods.roots.init.ModEffects;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public class MixinMinecraft$LightDrifter {
  @Unique
  private static boolean roots$DoSkip (KeyMapping instance, Operation<Boolean> original) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player != null && mc.player.hasEffect(ModEffects.LIGHT_DRIFTER)) {
      return false;
    }

    return original.call(instance);
  }

  @WrapOperation(method="handleKeybinds", at=@At(value="INVOKE", target="Lnet/minecraft/client/KeyMapping;consumeClick()Z", ordinal=2))
  private boolean roots$preventHotbar(KeyMapping instance, Operation<Boolean> original) {
    return roots$DoSkip(instance, original);
  }

  @WrapOperation(method="handleKeybinds", at=@At(value="INVOKE", target="Lnet/minecraft/client/KeyMapping;consumeClick()Z", ordinal=4))
  private boolean roots$preventInventory(KeyMapping instance, Operation<Boolean> original) {
    return roots$DoSkip(instance, original);
  }

  @WrapOperation(method="handleKeybinds", at=@At(value="INVOKE", target="Lnet/minecraft/client/KeyMapping;consumeClick()Z", ordinal=6))
  private boolean roots$preventOffHandSwap(KeyMapping instance, Operation<Boolean> original) {
    return roots$DoSkip(instance, original);
  }

  @WrapOperation(method="handleKeybinds", at=@At(value="INVOKE", target="Lnet/minecraft/client/KeyMapping;consumeClick()Z", ordinal=7))
  private boolean roots$preventDrop(KeyMapping instance, Operation<Boolean> original) {
    return roots$DoSkip(instance, original);
  }
}
