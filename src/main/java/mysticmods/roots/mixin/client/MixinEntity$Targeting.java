package mysticmods.roots.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mysticmods.roots.client.TargetingSystem;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public class MixinEntity$Targeting {
  @WrapMethod(method = "getTeamColor")
  private int roots$getTeamColour(Operation<Integer> original) {
    Entity thisEntity = (Entity) (Object) this;

    if (thisEntity.hasData(ModAttachments.TARGETED_ENTITY)) {
      if (TargetingSystem.isTargetedEntity(thisEntity)) {
        return 0x7734eb;
      } else {
        thisEntity.removeData(ModAttachments.TARGETED_ENTITY);
      }
    }

    return original.call();
  }

  @WrapMethod(method = "isCurrentlyGlowing")
  private boolean roots$isCurrentlyGlowing(Operation<Boolean> original) {
    Entity thisEntity = (Entity) (Object) this;

    if (thisEntity.hasData(ModAttachments.TARGETED_ENTITY)) {
      if (TargetingSystem.isTargetedEntity(thisEntity)) {
        return true;
      } else {
        thisEntity.removeData(ModAttachments.TARGETED_ENTITY);
      }
    }

    return original.call();
  }
}
