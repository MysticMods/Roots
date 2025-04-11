package mysticmods.roots.mixin.client;

import mysticmods.roots.api.RootsTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {
  @Shadow
  protected int sprintTriggerTime;

  @Redirect(method = "aiStep", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/Input;forwardImpulse:F", opcode = Opcodes.PUTFIELD))
  private void RootsModifyChannelMovementSpeedForward(Input input, float newValue) {
    if (Minecraft.getInstance().player == null || !Minecraft.getInstance().player.getUseItem()
        .is(RootsTags.Items.CASTING_TOOLS)) {
      input.forwardImpulse = newValue;
    }
  }

  @Redirect(method = "aiStep", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/Input;leftImpulse:F", opcode = Opcodes.PUTFIELD))
  private void RootsModifyChannelMovementSpeedLeft(Input input, float newValue) {
    if (Minecraft.getInstance().player == null || !Minecraft.getInstance().player.getUseItem()
        .is(RootsTags.Items.CASTING_TOOLS)) {
      input.leftImpulse = newValue;
    }
  }

  @Redirect(method = "aiStep", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;sprintTriggerTime:I", opcode = Opcodes.PUTFIELD))
  private void RootsModifyChannelMovementSpeedSprint(LocalPlayer player, int newValue) {
    if (Minecraft.getInstance().player == null || !Minecraft.getInstance().player.getUseItem()
        .is(RootsTags.Items.CASTING_TOOLS)) {
      this.sprintTriggerTime = newValue;
    }
  }
}
