package mysticmods.roots.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mysticmods.roots.api.Color;
import mysticmods.roots.client.RootsClientHooks;
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
    } else if (thisEntity.level()
        .isClientSide() && RootsClientHooks.showBoundingBoxes() && thisEntity.hasData(ModAttachments.GROVE_POWER_ENTITY_FLAG)) {

      var data = thisEntity.getData(ModAttachments.GROVE_POWER_ENTITY_FLAG);
      if (!data.eligible()) {
        return Color.RED.rgb();
      } else if (data.producedPower()) {
        return Color.GREEN.rgb();
      } else {
        return Color.YELLOW.rgb();
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
    } else if (thisEntity.level()
        .isClientSide() && RootsClientHooks.showBoundingBoxes() && thisEntity.hasData(ModAttachments.GROVE_POWER_ENTITY_FLAG)) {
      return true;
    }

    return original.call();
  }
}
