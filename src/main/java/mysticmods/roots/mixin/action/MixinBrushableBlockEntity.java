package mysticmods.roots.mixin.action;

import mysticmods.roots.action.BrushBlockAction;
import mysticmods.roots.init.ModActions;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrushableBlockEntity.class)
public class MixinBrushableBlockEntity {
  @Shadow
  private ItemStack item;

  @Inject(method="brush", at=@At(value="INVOKE", target="Lnet/minecraft/world/level/block/entity/BrushableBlockEntity;brushingCompleted(Lnet/minecraft/world/entity/player/Player;)V"))
  private void RootsOnBlockBrush (long startTick, Player player, Direction hitDirection, CallbackInfoReturnable<Boolean> cir) {
    if (!(player instanceof ServerPlayer serverPlayer)) {
      return;
    }
    BrushableBlockEntity blockEntity = (BrushableBlockEntity) (Object) this;
    BrushBlockAction.Context context = new BrushBlockAction.Context(serverPlayer.serverLevel(), serverPlayer, blockEntity.getBlockPos(), blockEntity.getBlockState(), item, blockEntity);
    ModActions.BRUSH_BLOCK.get().accept(context);
  }
}
