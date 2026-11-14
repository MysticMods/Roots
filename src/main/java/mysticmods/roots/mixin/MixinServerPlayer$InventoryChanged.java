package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mysticmods.roots.api.attachment.KeybindsNotified;
import mysticmods.roots.api.item.IKeybindItem;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.network.client.ClientboundNotifyKeybind;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = {"net/minecraft/server/level/ServerPlayer$2"})
public class MixinServerPlayer$InventoryChanged {
  @WrapOperation(method = "slotChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/critereon/InventoryChangeTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;)V"))
  private void roots$onSlotChanged(InventoryChangeTrigger instance, ServerPlayer player, Inventory inventory, ItemStack stack, Operation<Void> original) {
    original.call(instance, player, inventory, stack);
    if (stack.getItem() instanceof IKeybindItem) {
      Item item = stack.getItem();

      KeybindsNotified keys = player.getData(ModAttachments.NOTIFIED_KEYBINDS);
      if (!keys.has(item)) {
        player.setData(ModAttachments.NOTIFIED_KEYBINDS, keys.add(item));
        PacketDistributor.sendToPlayer(player, new ClientboundNotifyKeybind(item));
      }
    }
  }
}
