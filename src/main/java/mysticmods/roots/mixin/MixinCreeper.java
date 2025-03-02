package mysticmods.roots.mixin;

import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public abstract class MixinCreeper {
  @Shadow
  private int swell;

  @Shadow
  private int oldSwell;

  @Inject(method = "tick", at = @At("HEAD"))
  private void RootsCreeperTick(CallbackInfo ci) {
    Creeper thisCreeper = ((Creeper) (Object) this);
    if (thisCreeper.isAlive()) {
      if (thisCreeper.getTarget() == null) {
        thisCreeper.setSwellDir(-1);
        this.swell = 0;
        this.oldSwell = 0;
      }
    }
  }
}
