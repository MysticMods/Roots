package epicsquid.roots.particle;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.MysticalLib;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.RayCastUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModParticles;
import epicsquid.roots.spell.SpellBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ParticleUtil {
  public static Random random = new Random();
  public static int counter = 0;

  public static void spawnParticleRain(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, LibRegistry.PARTICLE_RAIN, x, y, z, vx, vy, vz, lifetime, a, scale, 1);
      }
    }
  }

  public static void spawnParticleStar(World world, float x, float y, float z, float vx, float vy, float vz, float[] rgba, float scale, int lifetime) {
    spawnParticleStar(world, x, y, z, vx, vy, vz, rgba[0], rgba[1], rgba[2], rgba[3], scale, lifetime);
  }

  public static void spawnParticleStar(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, ModParticles.PARTICLE_STAR, x, y, z, vx, vy, vz, lifetime, r, g, b, a, scale, 1);
      }
    }
  }

  public static void spawnParticleStarNoGravity(World world, float x, float y, float z, float vx, float vy, float vz, float[] rgba, float scale, int lifetime) {
    spawnParticleStarNoGravity(world, x, y, z, vx, vy, vz, rgba[0], rgba[1], rgba[2], rgba[3], scale, lifetime);
  }

  public static void spawnParticleStarNoGravity(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, ModParticles.PARTICLE_STAR_NO_GRAVITY, x, y, z, vx, vy, vz, lifetime, r, g, b, a, scale, 1);
      }
    }
  }

  public static void spawnParticleFiery(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, LibRegistry.PARTICLE_FLAME, x, y, z, vx, vy, vz, lifetime, r, g, b, a, scale);
      }
    }
  }

  public static void spawnParticleFiery(World world, float x, float y, float z, float vx, float vy, float vz, float[] rgba, float scale, int lifetime) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, LibRegistry.PARTICLE_FLAME, x, y, z, vx, vy, vz, lifetime, rgba[0], rgba[1], rgba[2], rgba[3], scale);
      }
    }
  }

  public static void spawnParticleLineGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, ModParticles.PARTICLE_LINE_GLOW, x, y, z, vx, vy, vz, lifetime, r, g, b, a, scale);
      }
    }
  }

  public static void spawnParticleLineGlowSteady(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, ModParticles.PARTICLE_LINE_GLOW_STEADY, x, y, z, vx, vy, vz, lifetime, r, g, b, a, scale);
      }
    }
  }

  public static void spawnParticleSmoke(World world, float x, float y, float z, float vx, float vy, float vz, float[] rgba, float scale, int lifetime, boolean additive) {
    spawnParticleSmoke(world, x, y, z, vx, vy, vz, rgba[0], rgba[1], rgba[2], rgba[3], scale, lifetime, additive);
  }

  public static void spawnParticleSmoke(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime, boolean additive) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, LibRegistry.PARTICLE_SMOKE, x, y, z, vx, vy, vz, lifetime, r, g, b, a, scale, 1);
      }
    }
  }

  public static void spawnParticleCloud(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime, boolean additive) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, LibRegistry.PARTICLE_CLOUD, x, y, z, vx, vy, vz, lifetime, r, g, b, a, scale, 1);
      }
    }
  }

  public static void spawnParticleGlow(World world, float x, float y, float z, float vx, float vy, float vz, float[] rgba, float scale, int lifetime) {
    spawnParticleGlow(world, x, y, z, vx, vy, vz, rgba[0], rgba[1], rgba[2], rgba[3], scale, lifetime);
  }

  public static void spawnParticleGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, LibRegistry.PARTICLE_GLOW, x, y, z, vx, vy, vz, lifetime, r, g, b, a, scale);
      }
    }
  }

  public static void spawnParticleSpark(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, LibRegistry.PARTICLE_SPARK, x, y, z, vx, vy, vz, lifetime, r, g, b, a, scale, lifetime);
      }
    }
  }

  public static void spawnParticleThorn(World world, float x, float y, float z, float vx, float vy, float vz, float[] rgba, float scale, int lifetime, boolean additive) {
    spawnParticleThorn(world, x, y, z, vx, vy, vz, rgba[0], rgba[1], rgba[2], rgba[3], scale, lifetime, additive);
  }

  public static void spawnParticleThorn(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime, boolean additive) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, ModParticles.PARTICLE_THORN, x, y, z, vx, vy, vz, lifetime, r, g, b, a, scale);
      }
    }
  }

  public static void spawnParticlePetal(World world, float x, float y, float z, float vx, float vy, float vz, float[] rgba, float scale, int lifetime) {
    spawnParticlePetal(world, x, y, z, vz, vy, vz, rgba[0], rgba[1], rgba[2], rgba[3], scale, lifetime);
  }

  public static void spawnParticlePetal(World world, float x, float y, float z, float vx, float vy, float vz, float[] rgba, float scale, int lifetime, boolean nodepth) {
    spawnParticlePetal(world, x, y, z, vz, vy, vz, rgba[0], rgba[1], rgba[2], rgba[3], scale, lifetime, nodepth);
  }

  public static void spawnParticlePetal(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
    spawnParticleSmoke(world, x, y, z, vx, vy, vz, r, g, b, a, scale, lifetime, false);
  }

  public static void spawnParticlePetal(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime, boolean noDepth) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, noDepth ? ModParticles.PARTICLE_PETAL_NO_DEPTH : ModParticles.PARTICLE_PETAL, x, y, z, vx, vy, vz, lifetime, r, g, b, a, scale);
      }
    }
  }

  // // Stuff // //
  public static void renderBeam(EntityPlayer player, double distance, ParticleSpawner spawner, SpellBase colors) {
    if (!player.world.isRemote) {
      return;
    }

    List<Vec3d> positions = RayCastUtil.rayTraceEntitiesPositions(player, distance);
    renderBeam(player.world, positions.get(0), positions.get(1), spawner, colors);
  }

  public static void renderBeam(World world, Vec3d startPosition, Vec3d stopPosition, ParticleSpawner spawner, SpellBase colors) {
    double dist = stopPosition.subtract(startPosition).length();
    double alphaDist = 0;
    for (double j = 0; j < dist; j += 0.15) {
      double x = startPosition.x * (1.0 - j / dist) + stopPosition.x * (j / dist);
      double y = startPosition.y * (1.0 - j / dist) + stopPosition.y * (j / dist);
      double z = startPosition.z * (1.0 - j / dist) + stopPosition.z * (j / dist);
      alphaDist += 0.15;

      if (Util.rand.nextBoolean()) {
        spawner.spawn(world, (float) x, (float) y, (float) z, 0, 0, 0, colors.getFirstColours(0.75f * (float) (1.0f - alphaDist / dist)), 3f + 3f * Util.rand.nextFloat(), 30);
      } else {
        spawner.spawn(world, (float) x, (float) y, (float) z, 0, 0, 0, colors.getSecondColours(0.75f * (float) (1.0f - alphaDist / dist)), 3f + 3f * Util.rand.nextFloat(), 30);
      }
    }
  }

  @FunctionalInterface
  public interface ParticleSpawner {
    void spawn(World world, float x, float y, float z, float vx, float vy, float vz, float[] rgba, float scale, int lifetime);
  }
}

