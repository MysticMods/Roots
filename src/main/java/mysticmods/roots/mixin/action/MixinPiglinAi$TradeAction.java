package mysticmods.roots.mixin.action;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mysticmods.roots.action.TradePiglinAction;
import mysticmods.roots.init.ModActions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(PiglinAi.class)
public class MixinPiglinAi$TradeAction {
  @WrapOperation(method = "stopHoldingOffHandItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;getBarterResponseItems(Lnet/minecraft/world/entity/monster/piglin/Piglin;)Ljava/util/List;"))
  private static List<ItemStack> roots$StopHoldingOffHandItem(Piglin piglin, Operation<List<ItemStack>> original) {
    List<ItemStack> result = original.call(piglin);
    if (ModActions.TRADE_PIGLIN.get().shouldTest()) {
      if (!result.isEmpty()) {
        piglin.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER).ifPresent(player -> {
          if (player instanceof ServerPlayer serverPlayer) {
            for (ItemStack item : result) {
              TradePiglinAction.Context context = new TradePiglinAction.Context(serverPlayer.serverLevel(), serverPlayer, piglin, item);
              ModActions.TRADE_PIGLIN.get().accept(context);
            }
          }
        });
      }
    }
    return result;
  }
}
