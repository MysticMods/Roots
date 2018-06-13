package teamroots.roots.particle;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import teamroots.roots.Roots;
import teamroots.roots.proxy.ClientProxy;

public class ParticleUtil {
	public static Random random = new Random();
	public static int counter = 0;
	
	public static void spawnParticleGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime){
		if (Roots.proxy instanceof ClientProxy){
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticleGlow(world,x,y,z,vx,vy,vz,r,g,b,a, scale, lifetime));
			}
		}
	}
	
	public static void spawnParticleGlowThroughBlocks(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime){
		if (Roots.proxy instanceof ClientProxy){
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticleGlowThroughBlocks(world,x,y,z,vx,vy,vz,r,g,b,a, scale, lifetime));
			}
		}
	}
	
	public static void spawnParticleStar(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime){
		if (Roots.proxy instanceof ClientProxy){
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticleStar(world,x,y,z,vx,vy,vz,r,g,b,a, scale, lifetime));
			}
		}
	}
	
	public static void spawnParticleSpark(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime){
		if (Roots.proxy instanceof ClientProxy){
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticleSpark(world,x,y,z,vx,vy,vz,r,g,b,a, scale, lifetime));
			}
		}
	}
	
	public static void spawnParticleRune(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime, int id){
		if (Roots.proxy instanceof ClientProxy){
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticleRune(world,x,y,z,vx,vy,vz,r,g,b,a, scale, lifetime, id));
			}
		}
	}
	
	public static void spawnParticleLineRune(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime){
		if (Roots.proxy instanceof ClientProxy){
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticleLineRune(world,x,y,z,vx,vy,vz,r,g,b,a, scale, lifetime));
			}
		}
	}
	
	public static void spawnParticleFiery(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime){
		if (Roots.proxy instanceof ClientProxy){
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticleFiery(world,x,y,z,vx,vy,vz,r,g,b,a, scale, lifetime));
			}
		}
	}
	
	public static void spawnParticleThorn(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime, boolean additive){
		if (Roots.proxy instanceof ClientProxy){
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticleThorn(world,x,y,z,vx,vy,vz,r,g,b,a, scale, lifetime, additive));
			}
		}
	}
	
	public static void spawnParticlePetal(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime){
		if (Roots.proxy instanceof ClientProxy){
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticlePetal(world,x,y,z,vx,vy,vz,r,g,b,a, scale, lifetime));
			}
		}
	}
	
	public static void spawnParticleSmoke(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime, boolean additive){
		if (Roots.proxy instanceof ClientProxy){
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticleSmoke(world,x,y,z,vx,vy,vz,r,g,b,a, scale, lifetime, additive));
			}
		}
	}

	public static void spawnParticleFloatingGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime){
		if (Roots.proxy instanceof ClientProxy){
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticleFloatingGlow(world,x,y,z,vx,vy,vz,r,g,b,a, scale, lifetime));
			}
		}
	}

	public static void spawnParticleLineGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime){
		if (Roots.proxy instanceof ClientProxy){
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticleLineGlow(world,x,y,z,vx,vy,vz,r,g,b,a, scale, lifetime));
			}
		}
	}
}
