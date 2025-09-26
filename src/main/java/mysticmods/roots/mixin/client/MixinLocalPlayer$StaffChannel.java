package mysticmods.roots.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mysticmods.roots.api.RootsTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer$StaffChannel {
  @Shadow
  protected int sprintTriggerTime;

  @WrapOperation(method = "aiStep", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/Input;forwardImpulse:F", opcode = Opcodes.PUTFIELD))
  private void RootsModifyChannelMovementSpeedForward(Input input, float newValue, Operation<Void> original) {
    if (Minecraft.getInstance().player == null || !Minecraft.getInstance().player.getUseItem()
        .is(RootsTags.Items.CASTING_TOOLS)) {
      input.forwardImpulse = newValue;
    }
  }

  @WrapOperation(method = "aiStep", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/Input;leftImpulse:F", opcode = Opcodes.PUTFIELD))
  private void RootsModifyChannelMovementSpeedLeft(Input input, float newValue, Operation<Void> original) {
    if (Minecraft.getInstance().player == null || !Minecraft.getInstance().player.getUseItem()
        .is(RootsTags.Items.CASTING_TOOLS)) {
      input.leftImpulse = newValue;
    }
  }

  @WrapOperation(method = "aiStep", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;sprintTriggerTime:I", opcode = Opcodes.PUTFIELD))
  private void RootsModifyChannelMovementSpeedSprint(LocalPlayer player, int newValue, Operation<Void> original) {
    if (Minecraft.getInstance().player == null || !Minecraft.getInstance().player.getUseItem()
        .is(RootsTags.Items.CASTING_TOOLS)) {
      this.sprintTriggerTime = newValue;
    }
  }
}
