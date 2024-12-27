package mysticmods.roots.mixin;

import com.mojang.serialization.Lifecycle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldOpenFlows.class)
public class MixinWorldOpenFlows {
  @Inject(method="confirmWorldCreation", at=@At(value="HEAD"), cancellable = true)
  private static void RootsSkipConfirmationScreen (Minecraft pMinecraft, CreateWorldScreen p_233128_, Lifecycle p_233129_, Runnable p_233130_, CallbackInfo info) {
    p_233130_.run();
    info.cancel();
  }
}
