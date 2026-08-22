package mysticmods.roots.mixin.action;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mysticmods.roots.action.BrushBlockAction;
import mysticmods.roots.init.ModActions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BrushableBlockEntity.class)
public class MixinBrushableBlockEntity$BrushBlockAction {
  @Shadow
  private ItemStack item;

  @WrapOperation(method = "brush", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BrushableBlockEntity;brushingCompleted(Lnet/minecraft/world/entity/player/Player;)V"))
  private void roots$OnBlockBrush(BrushableBlockEntity instance, Player player, Operation<Void> original) {
    if (player instanceof ServerPlayer serverPlayer && ModActions.BRUSH_BLOCK.get().shouldTest()) {
      BrushableBlockEntity blockEntity = (BrushableBlockEntity) (Object) this;
      BrushBlockAction.Context context = new BrushBlockAction.Context(serverPlayer.serverLevel(), serverPlayer, blockEntity.getBlockPos(), blockEntity.getBlockState(), item, blockEntity);
      ModActions.BRUSH_BLOCK.get().accept(context);
    }
    original.call(instance, player);
  }
}
