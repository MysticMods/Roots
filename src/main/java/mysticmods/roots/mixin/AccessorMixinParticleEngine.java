package mysticmods.roots.mixin;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.TrackingEmitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Queue;

@Mixin(ParticleEngine.class)
public interface AccessorMixinParticleEngine {
  @Accessor("trackingEmitters")
  Queue<TrackingEmitter> rootsGetTrackingEmitters();
}
