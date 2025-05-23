package mysticmods.roots.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class RootsRenderTypes {
  public static final RenderType ROOTS_LIGHTNING = RenderType.create("roots_lightning", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256,
      false, true, RenderType.CompositeState.builder()
          .setShaderState(RenderType.RENDERTYPE_LIGHTNING_SHADER)
          .setTransparencyState(RenderType.ADDITIVE_TRANSPARENCY)
          .createCompositeState(false)
  );

  public static final RenderStateShard.ShaderStateShard PARTICLE_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::getParticleShader);

  public static final Function<ResourceLocation, RenderType> ROOTS_BEAM =
      p_286159_ -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
            .setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
            .setTransparencyState(RenderType.LIGHTNING_TRANSPARENCY)
            .setCullState(RenderType.NO_CULL)
            .setOverlayState(RenderType.OVERLAY)
            .setLightmapState(RenderType.LIGHTMAP)
            .setDepthTestState(RenderType.NO_DEPTH_TEST)
            .setTextureState(new RenderStateShard.TextureStateShard(p_286159_, false, false))
            .createCompositeState(false);
        return RenderType.create("roots_beam", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, rendertype$compositestate);
      };
}
