package mysticmods.roots.mixin;


import mysticmods.roots.api.RootsTags;
import mysticmods.roots.effect.SimpleEffect;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffectInstance.class)
public class MixinMobEffectInstance {
  @Unique
  private boolean roots_1_21$checkedParticles = false;

  @Shadow
  private boolean visible;

  @Inject(method = "isVisible", at = @At(value = "HEAD"))
  protected void RootsInjectIsVisible(CallbackInfoReturnable<Boolean> cir) {
    if (!roots_1_21$checkedParticles) {
      Holder<MobEffect> effect = ((MobEffectInstance) (Object) this).getEffect();
      if (effect.value() instanceof SimpleEffect simpleEffect) {
        if (simpleEffect.isHiddenByDefault()) {
          this.visible = false;
        }
      } else if (effect.is(RootsTags.MobEffects.SUPPRESS_PARTICLES)) {
        this.visible = false;
      }
      roots_1_21$checkedParticles = true;
    }
  }
}

