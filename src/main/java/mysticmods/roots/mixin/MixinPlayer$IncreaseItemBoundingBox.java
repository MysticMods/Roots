package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mysticmods.roots.util.EnchantmentUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@SuppressWarnings("resource")
@Mixin(Player.class)
public class MixinPlayer$IncreaseItemBoundingBox {
  @Shadow
  private void touch(Entity entity) {
    throw new NotImplementedException("Mixin shadowed method");
  }

  @WrapOperation(method = "aiStep", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"))
  private boolean roots$increaseItemBoundingBox(List<ItemEntity> instance, Operation<Boolean> original, @Local AABB aabb) {
    var result = original.call(instance);
    if ((Object) this instanceof ServerPlayer player) {
      var helm = player.getItemBySlot(EquipmentSlot.HEAD);
      if (!helm.isEmpty()) {
        var increase = EnchantmentUtil.getCollectingIncrease(player.serverLevel(), helm);
        if (increase > 0) {
          var aabb2 = aabb.inflate(increase);
          for (ItemEntity entity : player.level().getEntitiesOfClass(ItemEntity.class, aabb2)) {
            this.touch(entity);
          }
        }
      }
    }
    return result;
  }
}
