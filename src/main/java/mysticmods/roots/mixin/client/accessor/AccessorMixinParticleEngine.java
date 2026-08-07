package mysticmods.roots.mixin.client.accessor;

import net.minecraft.client.particle.*;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.Queue;

@Mixin(ParticleEngine.class)
public interface AccessorMixinParticleEngine {
  @Accessor("trackingEmitters")
  Queue<TrackingEmitter> roots$GetTrackingEmitters();

  @Accessor("particles")
  Map<ParticleRenderType, Queue<Particle>> roots$GetParticles();

  @Accessor("spriteSets")
  Map<ResourceLocation, SpriteSet> roots$GetSpriteSets();
}
