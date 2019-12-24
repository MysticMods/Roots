package epicsquid.roots.init;

import epicsquid.mysticallib.MysticalLib;
import epicsquid.mysticallib.particle.ParticleRegistry;
import epicsquid.roots.particle.*;
import net.minecraft.util.ResourceLocation;

public class ModParticles {

  public static String PARTICLE_PETAL, PARTICLE_STAR, PARTICLE_LINE_GLOW, PARTICLE_THORN, PARTICLE_LINE_GLOW_STEADY, PARTICLE_STAR_NO_GRAVITY;

  public static void init() {
    PARTICLE_PETAL = ParticleRegistry.registerParticle(MysticalLib.MODID, ParticlePetal.class, new ResourceLocation("roots:particle/particle_petal"));
    PARTICLE_STAR = ParticleRegistry.registerParticle(MysticalLib.MODID, ParticleStar.class, new ResourceLocation("roots:particle/particle_star"));
    PARTICLE_STAR_NO_GRAVITY = ParticleRegistry.registerParticle(MysticalLib.MODID, ParticleStarNoGravity.class, new ResourceLocation("roots:particle/particle_star"));
    PARTICLE_LINE_GLOW = ParticleRegistry.registerParticle(MysticalLib.MODID, ParticleLineGlow.class, new ResourceLocation("roots:particle/particle_glow"));
    PARTICLE_THORN = ParticleRegistry.registerParticle(MysticalLib.MODID, ParticleThorn.class, new ResourceLocation("roots:particle/particle_thorn"));
    PARTICLE_LINE_GLOW_STEADY = ParticleRegistry
        .registerParticle(MysticalLib.MODID, ParticleLineGlowSteady.class, new ResourceLocation("roots:particle/particle_glow"));

  }
}
