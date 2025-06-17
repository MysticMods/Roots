package mysticmods.roots.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.trading.Merchant;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantMenu.class)
public class MixinMerchantMenu {
  @Shadow
  @Final
  private Merchant trader;

  @Inject(method="playTradeSound", at=@At("HEAD"), cancellable = true)
  private void RootsPlayTradeSound (CallbackInfo ci) {
    if (!(trader instanceof Entity)) {
      // Prevent crash with non-Entity traders (e.g., fairy huts)
      ci.cancel();
    }
  }
}
