package mysticmods.roots.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.minecraft.client.renderer.RenderStateShard.COLOR_WRITE;

public class RootsRenderTypes {
  public static final RenderType ROOTS_LIGHTNING = RenderType.create("roots_lightning", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256,
      false, true, RenderType.CompositeState.builder()
          .setShaderState(RenderType.RENDERTYPE_LIGHTNING_SHADER)
          .setTransparencyState(RenderType.ADDITIVE_TRANSPARENCY)
          .createCompositeState(false)
  );

  public static final RenderStateShard.ShaderStateShard SMART_CRUMBLING_SHADER = new RenderStateShard.ShaderStateShard(RootsShaders::getSmartCrumblingShader);

  public static final RenderStateShard.ShaderStateShard PARTICLE_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::getParticleShader);

  public static final RenderStateShard.ShaderStateShard PARTICLE_LOW_DISCARD_SHADER = new RenderStateShard.ShaderStateShard(() -> {
    // TODO: Check for Iris/etc here
    if (true) {
      return RootsShaders.getLowDiscardParticleShader();
    } else {
      return GameRenderer.getParticleShader();
    }
  });

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

  public static final RenderType TRANSLUCENT_DELAYED_PARTICLES = RenderType.create(
      "roots:particles_translucent",
      DefaultVertexFormat.PARTICLE,
      VertexFormat.Mode.QUADS,
      256,
      false,
      true,
      RenderType.CompositeState.builder()
          .setShaderState(PARTICLE_LOW_DISCARD_SHADER)
          .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
          .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_PARTICLES, false, false))
          .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
          .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
          .setLightmapState(RenderStateShard.LIGHTMAP)
          .createCompositeState(false));

  public static final RenderType TRANSLUCENT_DELAYED_PARTICLES_NO_CULL = RenderType.create(
      "roots:particles_translucent_no_cull",
      DefaultVertexFormat.PARTICLE,
      VertexFormat.Mode.QUADS,
      256,
      false,
      true,
      RenderType.CompositeState.builder()
          .setCullState(RenderType.NO_CULL)
          .setShaderState(PARTICLE_LOW_DISCARD_SHADER)
          .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
          .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_PARTICLES, false, false))
          .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
          .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
          .setLightmapState(RenderStateShard.LIGHTMAP)
          .createCompositeState(false));

  public static final RenderType TRANSLUCENT_DELAYED_PARTICLES_NO_MASK = RenderType.create(
      "roots:particles_translucent_no_mask",
      DefaultVertexFormat.PARTICLE,
      VertexFormat.Mode.QUADS,
      256,
      false,
      true,
      RenderType.CompositeState.builder()
          .setShaderState(PARTICLE_LOW_DISCARD_SHADER)
          .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
          .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_PARTICLES, false, false))
          .setWriteMaskState(COLOR_WRITE)
          .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
          .setLightmapState(RenderStateShard.LIGHTMAP)
          .createCompositeState(false));

  public static final Function<ResourceLocation, RenderType> CRUMBLE_GLINT = Util.memoize((texture) ->
      RenderType.create(
          "glint_translucent",
          DefaultVertexFormat.POSITION_TEX,
          VertexFormat.Mode.QUADS,
          1536,
          RenderType.CompositeState.builder()
              .setShaderState(RenderType.RENDERTYPE_GLINT_TRANSLUCENT_SHADER)
              .setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
              .setWriteMaskState(COLOR_WRITE)
              .setCullState(RenderType.NO_CULL)
              .setDepthTestState(RenderType.EQUAL_DEPTH_TEST)
              .setTransparencyState(RenderType.GLINT_TRANSPARENCY)
              .setTexturingState(RenderType.GLINT_TEXTURING)
              .setOutputState(RenderType.ITEM_ENTITY_TARGET)
              .createCompositeState(false)
      ));

  private static List<RenderType> CRUMBLE_TYPES = null;

  public static RenderType getCrumbleType(int index) {
    if (CRUMBLE_TYPES == null) {
      CRUMBLE_TYPES = ModelBakery.BREAKING_LOCATIONS.stream().map(CRUMBLE_GLINT).collect(Collectors.toList());
    }
    return CRUMBLE_TYPES.get(index);
  }
}
