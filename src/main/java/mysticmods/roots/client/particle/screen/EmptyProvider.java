package mysticmods.roots.client.particle.screen;

import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;

public record EmptyProvider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {

  @Override
  public @org.jetbrains.annotations.Nullable Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
    return null;
  }
}
