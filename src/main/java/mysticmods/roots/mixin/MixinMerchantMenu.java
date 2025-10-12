package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
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

  @WrapMethod(method = "playTradeSound")
  private void RootsPlayTradeSound(Operation<Void> original) {
    if (trader instanceof Entity) {
      // Prevent crash with non-Entity traders (e.g., fairy huts)
      original.call();
    }
  }
}
