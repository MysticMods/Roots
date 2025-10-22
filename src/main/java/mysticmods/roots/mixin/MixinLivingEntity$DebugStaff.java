package mysticmods.roots.mixin;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity$DebugStaff {


  @Unique
  private long roots_1_21$lastAlertTime = -1;

  @Inject(method="triggerItemUseEffects",at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/LivingEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
  private void roots_debugStaffItemEatParticles(ItemStack stack, int amount, CallbackInfo ci) {
    return;
/*    if (roots_1_21$lastAlertTime != -1 && Util.getMillis() - roots_1_21$lastAlertTime < 5000) {
      return;
    }
    roots_1_21$lastAlertTime = Util.getMillis();
    LivingEntity entity = ((LivingEntity) (Object) this);
    byte data = entity.getEntityData().get(Entity.DATA_SHARED_FLAGS_ID);
    RootsAPI.LOG.error("Spawning item particles for {}", entity);
    RootsAPI.LOG.error("Entity data is: {}", data);
    RootsAPI.LOG.error("Using item: {}", data & 1);
    RootsAPI.LOG.error("Using hand: {}", (data & 2) > 0 ? "OFF_HAND" : "MAIN_HAND");
    RootsAPI.LOG.error(StringUtils.join(Thread.currentThread().getStackTrace(), "\n"));*/
  }
}
