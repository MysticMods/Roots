package mysticmods.roots.mixin;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
  @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
  public void RootsLivingEntityTick(CallbackInfo ci) {
    LivingEntity entity = (LivingEntity) (Object) this;
    if (entity.getType().is(RootsTags.Entities.TIME_STOP_EXCLUDE)) {
      return;
    }
    if (entity.hasEffect(ModEffects.TIME_STOP)) {
      ((AccessorMixinLivingEntity) entity).callTickEffects();
      ci.cancel();
    }
  }
}
