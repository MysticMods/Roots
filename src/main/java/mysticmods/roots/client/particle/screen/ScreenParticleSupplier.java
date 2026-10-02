package mysticmods.roots.client.particle.screen;

import mysticmods.roots.client.particle.screen.base.ScreenParticle;
import mysticmods.roots.client.particle.screen.base.TextureSheetScreenParticle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.ParticleOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface ScreenParticleSupplier extends Supplier<Map<ParticleRenderType, List<ScreenParticle>>> {
  @Override
  default Map<ParticleRenderType, List<ScreenParticle>> get() {
    return getParticles();
  }

  Map<ParticleRenderType, List<ScreenParticle>> getParticles ();

  default void addContainerParticle(ScreenParticle particle) {
    ParticleRenderType type = particle.getRenderType();
    getParticles().computeIfAbsent(type, k -> new ArrayList<>()).add(particle);
  }

  default <T extends ParticleOptions> void addContainerParticle(T options, double x, double y, double xSpeed, double ySpeed) {
    TextureSheetScreenParticle particle = ScreenParticleEngine.createParticle(options, x, y, xSpeed, ySpeed);
    if (particle != null) {
      addContainerParticle(particle);
    }
  }
}
