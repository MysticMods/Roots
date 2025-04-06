package mysticmods.roots.mixin.action;

import mysticmods.roots.action.EatItemAction;
import mysticmods.roots.init.ModActions;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConsumeItemTrigger.class)
public class MixinConsumeItemTrigger {
  @Inject(method="trigger", at=@At("HEAD"))
  private void RootsTriggerConsumeItem (ServerPlayer player, ItemStack stack, CallbackInfo ci) {
    EatItemAction.Context context = new EatItemAction.Context(player.serverLevel(), player, stack);
    ModActions.EAT_ITEM.get().accept(context);
  }
}
