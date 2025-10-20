package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mysticmods.roots.init.ModEffects;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WitherBoss.class)
public class MixinWitherBoss$Geas {
  @WrapMethod(method = "setAlternativeTarget")
  private void roots$setAlternativeTarget(int targetOffset, int newId, Operation<Void> original) {
    // Handle the "alternative target" system for the Wither in relation to Geas
    if (!((WitherBoss) (Object) this).hasEffect(ModEffects.GEAS)) {
      original.call(targetOffset, newId);
    }
  }
}
