package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mysticmods.roots.api.RootsTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = {"net.minecraft.world.inventory.BrewingStandMenu$FuelSlot"})
public class MixinBrewingStandMenu$FuelSlot {
  @WrapOperation(method = "mayPlaceItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
  private static boolean roots$allowInfernoBulb(ItemStack instance, Item item, Operation<Boolean> original) {
    if (instance.is(RootsTags.Items.INFERNO_BULB_HERB)) {
      return true;
    }

    return original.call(instance, item);
  }
}
