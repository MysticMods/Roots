package mysticmods.roots.client.particle.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import org.jetbrains.annotations.Nullable;

// Adapted from Ars Nouveau: https://github.com/baileyholl/Ars-Nouveau/blob/main/src/main/java/com/hollingsworth/arsnouveau/client/particle/ParticleRenderTypes.java
public class RootsParticleRenderTypes {
  public static ParticleRenderType END_PORTAL = new ParticleRenderType() {

    @Override
    public @Nullable BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
      RenderSystem.disableBlend();
      RenderSystem.depthMask(true);
      RenderSystem.setShader(GameRenderer::getRendertypeEndPortalShader);
      RenderSystem.setShaderTexture(0, TheEndPortalRenderer.END_SKY_LOCATION);
      RenderSystem.setShaderTexture(1, TheEndPortalRenderer.END_PORTAL_LOCATION);
      return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    @Override
    public String toString() {
      return "roots:end_portal";
    }
  };

  public static ParticleRenderType GLOW = new ParticleRenderType() {
    @Override
    public @Nullable BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
      RenderSystem.enableBlend();
      RenderSystem.depthMask(true);
      RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
      RenderSystem.enableCull();
      RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
      RenderSystem.enableDepthTest();
      return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    @Override
    public String toString() {
      return "roots:glow";
    }
  };

  public static ParticleRenderType GLOW_NO_MASK = new ParticleRenderType() {
    @Override
    public @Nullable BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
      RenderSystem.enableBlend();
      RenderSystem.depthMask(false);
      RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
      RenderSystem.enableCull();
      RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
      RenderSystem.enableDepthTest();
      return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    @Override
    public String toString() {
      return "roots:glow_no_mask";
    }
  };

  public static ParticleRenderType SORTED_TRANSLUCENT = new ParticleRenderType() {
    @Override
    public BufferBuilder begin(Tesselator tess, TextureManager tex) {
      RenderSystem.depthMask(true);
      RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
/*      RenderSystem.blendFuncSeparate(
          GlStateManager.SourceFactor.SRC_ALPHA,
          GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
          GlStateManager.SourceFactor.ONE,
          GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
      );*/
      return tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    @Override
    public String toString() {
      return "roots:translucent_no_mask";
    }
  };
}
