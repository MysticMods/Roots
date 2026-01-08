package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
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

  // TODO: What's actually gonna use this? Enchantment? Spell?

  @WrapOperation(method="aiStep", at=@At(value="INVOKE", target="Ljava/util/List;isEmpty()Z"))
  private boolean lootr$increaseItemBoundingBox(List<ItemEntity> instance, Operation<Boolean> original, @Local AABB aabb) {
    Player player = (Player) (Object) this;
    var result = original.call(instance);
    var aabb2 = aabb.inflate(1.5, 0.5, 1.5);
    for (ItemEntity entity : player.level().getEntitiesOfClass(ItemEntity.class, aabb2)) {
      this.touch(entity);
    }
    return result;
  }
}
