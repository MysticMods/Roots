package mysticmods.roots.client.particle.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import org.jetbrains.annotations.Nullable;

public class TestParticleRenderType implements ParticleRenderType {
  public static TestParticleRenderType INSTANCE = new TestParticleRenderType();

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
  public boolean isTranslucent() {
    return true;
  }
}
