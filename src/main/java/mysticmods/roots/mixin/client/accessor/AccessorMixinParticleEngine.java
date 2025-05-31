package mysticmods.roots.mixin.client.accessor;

import com.google.common.collect.Maps;
import net.minecraft.client.particle.*;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.Queue;

@Mixin(ParticleEngine.class)
public interface AccessorMixinParticleEngine {
  @Accessor("trackingEmitters")
  Queue<TrackingEmitter> rootsGetTrackingEmitters();

  @Accessor("particles")
  Map<ParticleRenderType, Queue<Particle>> rootsGetParticles ();

  @Accessor("spriteSets")
  Map<ResourceLocation, SpriteSet> rootsGetSpriteSets();
}
