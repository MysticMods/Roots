package mysticmods.roots.mixin;

import mysticmods.roots.api.RootsTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {
  @Redirect(method = "aiStep", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/Input;forwardImpulse:F", opcode = Opcodes.PUTFIELD))
  private void RootsModifyChannelMovementSpeed(Input input, float newValue) {
    if (Minecraft.getInstance().player == null || !Minecraft.getInstance().player.getUseItem().is(RootsTags.Items.CASTING_TOOLS)) {
      input.forwardImpulse = newValue;
    }
  }
}
