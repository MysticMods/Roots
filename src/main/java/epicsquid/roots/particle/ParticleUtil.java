package epicsquid.roots.particle;

import java.util.Random;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.MysticalLib;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.roots.init.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class ParticleUtil {
  public static Random random = new Random();
  public static int counter = 0;

  public static void spawnParticleStar(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale,
                                       int lifetime) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, ModParticles.PARTICLE_STAR, x, y, z, vx, vy, vz, lifetime, r, g, b, a, scale, 1);
      }
    }
  }

  public static void spawnParticleFiery(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale,
                                        int lifetime) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, LibRegistry.PARTICLE_FLAME, x, y, z, vx, vy, vz, lifetime, r, g, b, a, scale);
      }
    }
  }

  public static void spawnParticleLineGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a,
                                           float scale, int lifetime) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, ModParticles.PARTICLE_LINE_GLOW, x, y, z, vx, vy, vz, lifetime, r, g, b, a, scale);
      }
    }
  }

  public static void spawnParticleLineGlowSteady(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a,
                                                 float scale, int lifetime) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, ModParticles.PARTICLE_LINE_GLOW_STEADY, x, y, z, vx, vy, vz, lifetime, r, g, b, a, scale);
      }
    }
  }

  public static void spawnParticleSmoke(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale,
                                        int lifetime, boolean additive) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, LibRegistry.PARTICLE_SMOKE, x, y, z, vx, vy, vz, lifetime, r, g, b, a, scale, 1);
      }
    }
  }

  public static void spawnParticleGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale,
                                       int lifetime) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, LibRegistry.PARTICLE_GLOW, x, y, z, vx, vy, vz, lifetime, r, g, b, a, scale);
      }
    }
  }

  public static void spawnParticleSpark(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale,
                                        int lifetime) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, LibRegistry.PARTICLE_SPARK, x, y, z, vx, vy, vz, lifetime, r, g, b, a, scale, lifetime);
      }
    }
  }

  public static void spawnParticleThorn(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale,
                                        int lifetime, boolean additive) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, ModParticles.PARTICLE_THORN, x, y, z, vx, vy, vz, lifetime, r, g, b, a, scale);
      }
    }
  }

  public static void spawnParticlePetal(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale,
                                        int lifetime) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.spawnParticle(world, ModParticles.PARTICLE_PETAL, x, y, z, vx, vy, vz, lifetime, r, g, b, a, scale);
      }
    }
  }

  /**
   *

   public static void spawnParticleGlowThroughBlocks(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime){
   if (MysticalLib.proxy instanceof ClientProxy){
   counter += random.nextInt(3);
   if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
   ClientProxy.particleRenderer.spawnParticle(new ParticleGlowThroughBlocks(world,x,y,z,vx,vy,vz,r,g,b,a, scale, lifetime));
   }
   }
   }



   public static void spawnParticleRune(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime, int id){
   if (MysticalLib.proxy instanceof ClientProxy){
   counter += random.nextInt(3);
   if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
   ClientProxy.particleRenderer.spawnParticle(new ParticleRune(world,x,y,z,vx,vy,vz,r,g,b,a, scale, lifetime, id));
   }
   }
   }

   public static void spawnParticleLineRune(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime){
   if (MysticalLib.proxy instanceof ClientProxy){
   counter += random.nextInt(3);
   if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
   ClientProxy.particleRenderer.spawnParticle(new ParticleLineRune(world,x,y,z,vx,vy,vz,r,g,b,a, scale, lifetime));
   }
   }
   }









   public static void spawnParticleFloatingGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime){
   if (MysticalLib.proxy instanceof ClientProxy){
   counter += random.nextInt(3);
   if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
   ClientProxy.particleRenderer.spawnParticle(new ParticleFloatingGlow(world,x,y,z,vx,vy,vz,r,g,b,a, scale, lifetime));
   }
   }
   }


   */
}