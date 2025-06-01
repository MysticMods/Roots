package mysticmods.roots.client.particle.screen;

import mysticmods.roots.client.particle.screen.base.TextureSheetScreenParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;

import javax.annotation.Nullable;

public interface ScreenParticleProvider<T extends ParticleOptions> {
  @Nullable
  TextureSheetScreenParticle createParticle(SpriteSet sprites, T type, ClientLevel level, double x, double y, double xSpeed, double ySpeed);
}
