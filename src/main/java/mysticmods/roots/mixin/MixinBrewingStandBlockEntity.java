package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mysticmods.roots.api.RootsTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BrewingStandBlockEntity.class)
public class MixinBrewingStandBlockEntity {
  @WrapOperation(method = "canPlaceItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
  private boolean roots$canPlaceAllowInfernoBulb(ItemStack instance, Item item, Operation<Boolean> original) {
    if (instance.is(RootsTags.Items.INFERNO_BULB_HERB)) {
      return true;
    }

    return original.call(instance, item);
  }

  @WrapOperation(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
  private static boolean roots$allowInfernoBulb(ItemStack instance, Item item, Operation<Boolean> original) {
    if (instance.is(RootsTags.Items.INFERNO_BULB_HERB)) {
      return true;
    }

    return original.call(instance, item);
  }
}
