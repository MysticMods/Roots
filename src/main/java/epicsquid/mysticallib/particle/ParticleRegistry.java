package epicsquid.mysticallib.particle;

import epicsquid.mysticallib.MysticalLib;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.util.*;

public class ParticleRegistry {
  public static Map<Class<? extends ParticleBase>, List<ResourceLocation>> particleMultiTextures = new HashMap<>();

  private static Map<Class<? extends ParticleBase>, Constructor<? extends ParticleBase>> particles = new HashMap<>();

  private static Random rand = new Random();

  public static ResourceLocation getTexture(Class<? extends ParticleBase> clazz) {
    List<ResourceLocation> textures = particleMultiTextures.get(clazz);
    return textures.get(rand.nextInt(textures.size()));
  }

  public static Class<? extends ParticleBase> registerParticle(@Nonnull String modid, @Nonnull Class<? extends ParticleBase> particleClass, @Nonnull ResourceLocation... textures) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      try {
        if (particles.containsKey(particleClass) || particleMultiTextures.containsKey(particleClass)) {
          MysticalLib.logger.error("WARNING: PARTICLE ALREADY REGISTERED WITH NAME \"" + Util.getLowercaseClassName(particleClass) + "\"!");
        } else {
          particles.put(particleClass, particleClass.getConstructor(World.class, double.class, double.class, double.class, double.class, double.class, double.class, double[].class));
          particleMultiTextures.put(particleClass, Arrays.asList(textures));
        }
      } catch (NoSuchMethodException | SecurityException e) {
        e.printStackTrace();
      }
    }
    return particleClass;
  }

  public static Map<Class<? extends ParticleBase>, Constructor<? extends ParticleBase>> getParticles() {
    return particles;
  }
}
