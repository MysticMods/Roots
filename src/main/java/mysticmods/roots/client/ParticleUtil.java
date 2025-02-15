package mysticmods.roots.client;

import mysticmods.roots.mixin.AccessorMixinParticleEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TrackingEmitter;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;

import java.util.function.BiConsumer;

public class ParticleUtil {

  public static void addTrackingEmitter(Entity entity, ParticleOptions particleType, int emitterLiftime, BiConsumer<ClientLevel, Entity> particleBuilder) {
    Minecraft minecraft = Minecraft.getInstance();
    ((AccessorMixinParticleEngine) minecraft.particleEngine).getTrackingEmitters()
        .add(new RootsTrackingEmitter(minecraft.level, entity, particleType, emitterLiftime, particleBuilder));
  }

  public enum Point {
    X,
    Y,
    Z
  }

  public static class RootsTrackingEmitter extends TrackingEmitter {

    private final BiConsumer<ClientLevel, Entity> particleBuidler;

    public RootsTrackingEmitter(ClientLevel level, Entity entity, ParticleOptions particleType, int lifetime, BiConsumer<ClientLevel, Entity> particleBuilder) {
      super(level, entity, particleType, lifetime);
      this.particleBuidler = particleBuilder;
    }

    @Override
    public void tick() {
      this.x = entity.getX();
      this.z = entity.getZ();
      this.y = entity.getY();
      if (this.particleBuidler != null) {
        this.particleBuidler.accept(this.level, this.entity);
      }
      this.life++;
      if (this.life >= this.lifeTime) {
        this.remove();
      }
    }
  }
}
