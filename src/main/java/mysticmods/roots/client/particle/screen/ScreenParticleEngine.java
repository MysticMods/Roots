package mysticmods.roots.client.particle.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import mysticmods.roots.client.particle.screen.base.ScreenParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureManager;

import java.util.*;

public class ScreenParticleEngine {
  private static final Map<ParticleRenderType, List<ScreenParticle>> particles = new HashMap<>();

  public static void tick() {
    particles.forEach((type, particleList) -> {
      Iterator<ScreenParticle> iterator = particleList.iterator();
      while (iterator.hasNext()) {
        ScreenParticle next = iterator.next();
        next.tick();
        if (!next.isAlive()) {
          iterator.remove();
        }
      }
    });
  }

  public static void addParticle(ScreenParticle particle) {
    ParticleRenderType type = particle.getRenderType();
    particles.computeIfAbsent(type, k -> new ArrayList<>()).add(particle);
  }

  public static void render (float partialTicks) {
    RenderSystem.enableDepthTest();

    RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
    Tesselator tesselator = Tesselator.getInstance();
    TextureManager textureManager = Minecraft.getInstance().getTextureManager();

    particles.forEach((type, particleList) -> {
      if (!particleList.isEmpty()) {
        BufferBuilder buffer = type.begin(tesselator, textureManager);
        for (ScreenParticle particle : particleList) {
          particle.render(buffer, partialTicks);
        }

        MeshData meshdata = buffer.build();
        if (meshdata != null) {
          BufferUploader.drawWithShader(meshdata);
        }
      }
    });

    RenderSystem.depthMask(true);
    RenderSystem.disableBlend();
  }

}
