package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractArrow.class)
public class MixinAbstractArrow {
  @Shadow
  private ItemStack pickupItemStack;

  @WrapOperation(method="<init>(Lnet/minecraft/world/entity/EntityType;DDDLnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V", at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/projectile/AbstractArrow;setCustomName(Lnet/minecraft/network/chat/Component;)V"))
  private void roots$removeItemStackComponent(AbstractArrow instance, Component component, Operation<Void> original) {
    original.call(instance, component);
    // Remove the quiver record from the arrow's pick-up stack allowing it to stack properly
    if (this.pickupItemStack.has(ModAttachments.QUIVER_RECORD)) {
      this.pickupItemStack.remove(ModAttachments.QUIVER_RECORD);
    }
  }
}
