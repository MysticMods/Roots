package mysticmods.roots.client.particle.render;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import mysticmods.roots.client.RootsRenderTypes;
import mysticmods.roots.client.RootsShaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;

public class RootsParticleRenderTypes {
  // TODO: Move these out
  private static ImmutableList<ParticleRenderType> DELAYED_RENDER_ORDER;

  public static ImmutableList<ParticleRenderType> getDelayedRenderOrder() {
    if (DELAYED_RENDER_ORDER == null) {
      DELAYED_RENDER_ORDER = ImmutableList.of(
          RootsParticleRenderTypes.OPAQUE,
          RootsParticleRenderTypes.DELAYED_TRANSLUCENT,
          RootsParticleRenderTypes.DELAYED_TRANSLUCENT_NO_CULL,
          RootsParticleRenderTypes.DELAYED_TRANSLUCENT_NO_DEPTH
      );
    }
    return DELAYED_RENDER_ORDER;
  }

  private static ImmutableMap<ParticleRenderType, RenderType> DELAYED_PARTICLE_RENDER_TYPES;

  public static ImmutableMap<ParticleRenderType, RenderType> getDelayedParticleRenderTypes() {
    if (DELAYED_PARTICLE_RENDER_TYPES == null) {
      DELAYED_PARTICLE_RENDER_TYPES = ImmutableMap.of(
          RootsParticleRenderTypes.OPAQUE, RootsRenderTypes.DELAYED_PARTICLES,
          RootsParticleRenderTypes.DELAYED_TRANSLUCENT, RootsRenderTypes.TRANSLUCENT_DELAYED_PARTICLES,
          RootsParticleRenderTypes.DELAYED_TRANSLUCENT_NO_CULL, RootsRenderTypes.TRANSLUCENT_DELAYED_PARTICLES_NO_CULL,
          RootsParticleRenderTypes.DELAYED_TRANSLUCENT_NO_DEPTH, RootsRenderTypes.TRANSLUCENT_DELAYED_PARTICLES_NO_MASK
      );
    }
    return DELAYED_PARTICLE_RENDER_TYPES;
  }

  public interface RootsParticleRenderType extends ParticleRenderType {
    default boolean isDelayed() {
      return false;
    }
  }

  public static RootsParticleRenderType CARDIOID_GLOW = new RootsParticleRenderType() {
    @Override
    public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
      RenderSystem.enableBlend();
      RenderSystem.depthMask(false);
      RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
      RenderSystem.setShader(RootsShaders::getCardioidParticleShader);
      RenderSystem.enableCull();
      RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
      RenderSystem.enableDepthTest();
      Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
      return tesselator.begin(VertexFormat.Mode.QUADS, RootsRenderTypes.CARDIOID_PARTICLE);
    }

    @Override
    public String toString() {
      return "roots:glow";
    }
  };

  // Normal render types
  public static RootsParticleRenderType GLOW = new RootsParticleRenderType() {
    @Override
    public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
      RenderSystem.enableBlend();
      RenderSystem.depthMask(false);
      RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
      RenderSystem.setShader(RootsShaders::getLowDiscardParticleShader);
      RenderSystem.enableCull();
      RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
      RenderSystem.enableDepthTest();
      Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
      return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    @Override
    public String toString() {
      return "roots:glow";
    }
  };

  public static RootsParticleRenderType GLOW_NO_CULL = new RootsParticleRenderType() {
    @Override
    public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
      RenderSystem.enableBlend();
      RenderSystem.depthMask(false);
      RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
      RenderSystem.setShader(RootsShaders::getLowDiscardParticleShader);
      RenderSystem.disableCull();
      RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
      RenderSystem.enableDepthTest();
      Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
      return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    @Override
    public String toString() {
      return "roots:glow_no_cull";
    }
  };

  public static RootsParticleRenderType GLOW_NO_DEPTH = new RootsParticleRenderType() {
    @Override
    public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
      RenderSystem.enableBlend();
      RenderSystem.depthMask(false);
      RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
      RenderSystem.setShader(RootsShaders::getLowDiscardParticleShader);
      RenderSystem.enableCull();
      RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
      RenderSystem.disableDepthTest();
      Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
      return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    @Override
    public String toString() {
      return "roots:glow_no_depth";
    }
  };


  public static RootsParticleRenderType OPAQUE = new RootsParticleRenderType() {
    @Override
    public boolean isDelayed() {
      return true;
    }

    @Override
    public BufferBuilder begin(Tesselator tess, TextureManager tex) {
      RenderSystem.depthMask(true);
      RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
      RenderSystem.disableBlend();
      RenderSystem.setShader(RootsShaders::getLowDiscardParticleShader);
      Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
      return tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    @Override
    public String toString() {
      return "roots:opaque";
    }
  };

  // Delayed render types that use quad sorting
  public static RootsParticleRenderType DELAYED_TRANSLUCENT_NO_DEPTH = new RootsParticleRenderType() {
    @Override
    public boolean isDelayed() {
      return true;
    }

    @Override
    public BufferBuilder begin(Tesselator tess, TextureManager tex) {
      RenderSystem.depthMask(false);
      RenderSystem.disableDepthTest();
      RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(
          GlStateManager.SourceFactor.SRC_ALPHA,
          GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
          GlStateManager.SourceFactor.ONE,
          GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
      );
      RenderSystem.setShader(RootsShaders::getLowDiscardParticleShader);
      Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
      return tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    @Override
    public String toString() {
      return "roots:delayed_translucent_no_mask";
    }
  };

  public static RootsParticleRenderType DELAYED_TRANSLUCENT = new RootsParticleRenderType() {
    @Override
    public boolean isDelayed() {
      return true;
    }

    @Override
    public BufferBuilder begin(Tesselator tess, TextureManager tex) {
      RenderSystem.depthMask(true);
      RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(
          GlStateManager.SourceFactor.SRC_ALPHA,
          GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
          GlStateManager.SourceFactor.ONE,
          GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
      );
      RenderSystem.setShader(RootsShaders::getLowDiscardParticleShader);
      Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
      return tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    @Override
    public String toString() {
      return "roots:delayed_translucent";
    }
  };

  public static RootsParticleRenderType DELAYED_TRANSLUCENT_NO_CULL = new RootsParticleRenderType() {
    @Override
    public boolean isDelayed() {
      return true;
    }

    @Override
    public BufferBuilder begin(Tesselator tess, TextureManager tex) {
      RenderSystem.depthMask(true);
      RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
      RenderSystem.enableBlend();
      RenderSystem.disableCull();
      RenderSystem.blendFuncSeparate(
          GlStateManager.SourceFactor.SRC_ALPHA,
          GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
          GlStateManager.SourceFactor.ONE,
          GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
      );
      RenderSystem.setShader(RootsShaders::getLowDiscardParticleShader);
      Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
      return tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    @Override
    public String toString() {
      return "roots:delayed_translucent_no_cull";
    }
  };

  // Special render types
  public static RootsParticleRenderType END_PORTAL = new RootsParticleRenderType() {
    @Override
    public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
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

}
