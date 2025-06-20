package mysticmods.roots.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.particle.ParticleRenderType;
import net.neoforged.neoforge.client.ClientHooks;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Comparator;
import java.util.List;

@Mixin(ClientHooks.class)
public class MixinClientHooks {
  @SuppressWarnings("rawtypes")
  @WrapMethod(method = "lambda$makeParticleRenderTypeComparator$12")
  private static int test(List renderOrder, Comparator vanillaComparator, ParticleRenderType typeOne, ParticleRenderType typeTwo, Operation<Integer> original) {
    if (typeOne == ParticleRenderType.CUSTOM && typeTwo != ParticleRenderType.CUSTOM) {
      return 1;
    }
    if (typeOne != ParticleRenderType.CUSTOM && typeTwo == ParticleRenderType.CUSTOM) {
      return -1;
    }
    return original.call(renderOrder, vanillaComparator, typeOne, typeTwo);
  }
}
