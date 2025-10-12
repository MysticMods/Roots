package mysticmods.roots.mixin.action;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
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
  @WrapMethod(method = "trigger")
  private void RootsTriggerConsumeItem(ServerPlayer player, ItemStack stack, Operation<Void> original) {
    original.call(player, stack);
    EatItemAction.Context context = new EatItemAction.Context(player.serverLevel(), player, stack);
    ModActions.EAT_ITEM.get().accept(context);
  }
}
