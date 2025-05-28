package mysticmods.roots.mixin.client;

import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.client.particle.world.SortedParticle;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.function.Predicate;

@Mixin(ParticleEngine.class)
public class MixinParticleEngine {
  @Shadow
  @Final
  private Map<ParticleRenderType, Queue<Particle>> particles;

  @Inject(method="render(Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;Ljava/util/function/Predicate;)V", at=@At("HEAD"))
  private void roots$renderParticles(LightTexture lightTexture, Camera camera, float partialTicks, Frustum frustum, Predicate<Particle> particlePredicate, CallbackInfo ci) {
    Queue<Particle> queue = this.particles.get(RootsParticleRenderTypes.SORTED_TRANSLUCENT);
    if (queue == null || queue.isEmpty()) {
      return;
    }

    List<SortedParticle> sortedParticles = new ArrayList<>();
    for (Particle particle : queue) {
      if (particle instanceof SortedParticle sortedParticle) {
        if (sortedParticle.getDistanceToCamera() == Float.MAX_VALUE) {
          sortedParticle.updateDistanceToCamera(camera, partialTicks);
        }
        sortedParticles.add(sortedParticle);
      }
    }

    sortedParticles.sort(Comparator.comparingDouble(SortedParticle::getDistanceToCamera));

    queue.clear();
    queue.addAll(sortedParticles);
  }
}
