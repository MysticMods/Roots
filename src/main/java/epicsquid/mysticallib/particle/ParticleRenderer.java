package epicsquid.mysticallib.particle;

import epicsquid.mysticallib.MysticalLib;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParticleRenderer {
  private CopyOnWriteArrayList<Particle> particles = new CopyOnWriteArrayList<>();

  public synchronized void updateParticles() {
    boolean doRemove;
    List<Particle> toRemove = new ArrayList<>();

    for (Particle particle : particles) {
      doRemove = true;
      if (particle != null) {
        if (particle instanceof IParticle) {
          if (((IParticle) particle).alive()) {
            particle.onUpdate();
            doRemove = false;
          }
        }
      }
      if (doRemove) {
        toRemove.add(particle);
      }
    }

    if (!toRemove.isEmpty()) {
      particles.removeAll(toRemove);
    }
  }

  public synchronized void renderParticles(@Nonnull PlayerEntity dumbplayer, float partialTicks) {
    float f = ActiveRenderInfo.getRotationX();
    float f1 = ActiveRenderInfo.getRotationZ();
    float f2 = ActiveRenderInfo.getRotationYZ();
    float f3 = ActiveRenderInfo.getRotationXY();
    float f4 = ActiveRenderInfo.getRotationXZ();
    PlayerEntity player = MysticalLib.proxy.getPlayer();
    if (player != null) {
      Particle.interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
      Particle.interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
      Particle.interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
      Particle.cameraViewDir = player.getLook(partialTicks);
      GlStateManager.enableAlpha();
      GlStateManager.enableBlend();
      GlStateManager.alphaFunc(516, 0.003921569F);
      GlStateManager.disableCull();

      GlStateManager.depthMask(false);

      Minecraft.getMinecraft().renderEngine.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
      Tessellator tess = Tessellator.getInstance();
      BufferBuilder buffer = tess.getBuffer();

      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
      for (Particle particle : particles) {
        if (particle instanceof IParticle) {
          if (!((IParticle) particle).isAdditive()) {
            particle.renderParticle(buffer, player, partialTicks, f, f4, f1, f2, f3);
          }
        }
      }
      tess.draw();

      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
      for (Particle particle : particles) {
        if (particle != null) {
          if (((IParticle) particle).isAdditive()) {
            particle.renderParticle(buffer, player, partialTicks, f, f4, f1, f2, f3);
          }
        }
      }
      tess.draw();

      GlStateManager.disableDepth();
      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
      for (Particle particle : particles) {
        if (particle instanceof IParticle) {
          if (!((IParticle) particle).isAdditive() && ((IParticle) particle).renderThroughBlocks()) {
            particle.renderParticle(buffer, player, partialTicks, f, f4, f1, f2, f3);
          }
        }
      }
      tess.draw();

      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
      for (Particle particle : particles) {
        if (particle != null) {
          if (((IParticle) particle).isAdditive() && ((IParticle) particle).renderThroughBlocks()) {
            particle.renderParticle(buffer, player, partialTicks, f, f4, f1, f2, f3);
          }
        }
      }
      tess.draw();
      GlStateManager.enableDepth();

      GlStateManager.enableCull();
      GlStateManager.depthMask(true);
      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
      GlStateManager.disableBlend();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableAlpha();
    }
  }

  private static Map<Class<? extends ParticleBase>, String> particleLookup = new HashMap<>();

  public void spawnParticle(World world, Class<? extends ParticleBase> particle, double x, double y, double z, double vx, double vy, double vz, double... data) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      try {
        Constructor<? extends ParticleBase> constructor = ParticleRegistry.getParticles().get(particle);
        if (constructor == null) {
          MysticalLib.logger.error("Unable to instantiable particle " + Util.getLowercaseClassName(particle) + " as it has not been registered.");
          return;
        }
        particles.add(constructor.newInstance(world, x, y, z, vx, vy, vz, data));
      } catch (Throwable e) {
        MysticalLib.logger.error("Unable to instantiate particle " + Util.getLowercaseClassName(particle) + " for unknown reason", e);
      }
    }
  }
}
