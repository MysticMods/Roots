package mysticmods.roots.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldSelectionList.WorldListEntry.class)
public class MixinWorldSelectionList$WorldListEntry {
  @Inject(method = "renderExperimentalWarning", at = @At("HEAD"), cancellable = true, remap = false)
  private void RootsIgnoreExperimentalWarningIcon(PoseStack stack, int mouseX, int mouseY, int top, int left, CallbackInfo ci)
  {
    ci.cancel();
  }
}
