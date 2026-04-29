package mysticmods.roots.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.mixin.client.accessor.AccessorMixinCompositeRenderType;
import mysticmods.roots.mixin.client.accessor.AccessorMixinCompositeState;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.function.BiFunction;
import java.util.function.Function;

public class RootsRenderTypes {
  private static boolean offMainTarget = false;

  public static void setOffMainTarget() {
    RootsRenderTypes.offMainTarget = true;
  }

  public static void resetOffMainTarget() {
    RootsRenderTypes.offMainTarget = false;
  }

  public static final Function<RenderStateShard.OutputStateShard, RenderStateShard.OutputStateShard> VARIABLE_RENDER_TARGET = Util.memoize((type) -> {
    if (type.name.equals("variable_target")) {
      return type;
    }
    return new RenderStateShard.OutputStateShard("variable_target", () -> {
      if (offMainTarget) {
      } else {
        type.setupRenderState();
      }
    }, () -> {
      if (offMainTarget) {
      } else {
        type.clearRenderState();
      }
    });
  });

  private static final RenderStateShard.LayeringStateShard CUSTOM_POLYGON_OFFSET_LAYERING = new RenderStateShard.LayeringStateShard(
      "polygon_offset_layering", () -> {
    RenderSystem.polygonOffset(-0.25F, -10.0F);
    RenderSystem.enablePolygonOffset();
  }, () -> {
    RenderSystem.polygonOffset(0.0F, 0.0F);
    RenderSystem.disablePolygonOffset();
  }
  );
  public static final RenderType GLINT = RenderType.create(
      "glint",
      DefaultVertexFormat.POSITION_TEX,
      VertexFormat.Mode.QUADS,
      1536,
      false,
      false,
      RenderType.CompositeState.builder()
          .setShaderState(RenderType.RENDERTYPE_GLINT_SHADER)
          .setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ITEM, true, false))
          .setWriteMaskState(RenderStateShard.COLOR_WRITE)
          .setCullState(RenderType.NO_CULL)
          .setDepthTestState(RenderType.LEQUAL_DEPTH_TEST)
          .setTransparencyState(RenderType.GLINT_TRANSPARENCY)
          .setTexturingState(RenderType.GLINT_TEXTURING)
          .setLayeringState(CUSTOM_POLYGON_OFFSET_LAYERING)
          .createCompositeState(false)
  );

  public static final RenderType ROOTS_LIGHTNING = RenderType.create("roots_lightning", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256,
      false, true, RenderType.CompositeState.builder()
          .setShaderState(RenderType.RENDERTYPE_LIGHTNING_SHADER)
          .setTransparencyState(RenderType.ADDITIVE_TRANSPARENCY)
          .createCompositeState(false)
  );

  public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_CUTOUT_DISSOLVE_SHADER = new RenderStateShard.ShaderStateShard(RootsShaders::getRenderTypeEntityCutoutDissolveShader);

  public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_TRANSLUCENT_CULL_DISSOLVE_SHADER = new RenderStateShard.ShaderStateShard(RootsShaders::getRenderTypeEntityTranslucentCullDissolveShader);

  public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_NO_OUTLINE_DISSOLVE_SHADER = new RenderStateShard.ShaderStateShard(RootsShaders::getRenderTypeEntityNoOutlineDissolveShader);

  public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_CUTOUT_NO_CULL_DISSOLVE_SHADER = new RenderStateShard.ShaderStateShard(RootsShaders::getRenderTypeEntityCutoutNoCullDissolveShader);

  public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_SOLID_DISSOLVE_SHADER = new RenderStateShard.ShaderStateShard(RootsShaders::getRenderTypeEntitySolidDissolveShader);

  public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_TRANSLUCENT_DISSOLVE_SHADER = new RenderStateShard.ShaderStateShard(RootsShaders::getRenderTypeEntityTranslucentDissolveShader);

  public static final RenderStateShard.ShaderStateShard DISSOLVE_SHADER = new RenderStateShard.ShaderStateShard(RootsShaders::getDissolveShader);

  public static final RenderStateShard.ShaderStateShard PARTICLE_LOW_DISCARD_SHADER = new RenderStateShard.ShaderStateShard(() -> {
    if (((ModList.get().isLoaded("iris") && !ModList.get()
        .isLoaded("monocle")) || ConfigManager.DISABLE_CUSTOM_PARTICLE_SHADER.get()) && !ConfigManager.FORCE_CUSTOM_PARTICLE_SHADER.get()) {
      return GameRenderer.getParticleShader();
    } else {
      return RootsShaders.getLowDiscardParticleShader();
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

  public static final RenderType DELAYED_PARTICLES = RenderType.create(
      "roots:translucent",
      DefaultVertexFormat.PARTICLE,
      VertexFormat.Mode.QUADS,
      256,
      false,
      false,
      RenderType.CompositeState.builder()
          .setShaderState(PARTICLE_LOW_DISCARD_SHADER)
          .setTransparencyState(RenderStateShard.NO_TRANSPARENCY)
          .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_PARTICLES, false, false))
          .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
          .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
          .setLightmapState(RenderStateShard.LIGHTMAP)
          .setOutputState(RenderType.PARTICLES_TARGET)
          .createCompositeState(false));

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
          .setOutputState(RenderType.PARTICLES_TARGET)
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
          .setOutputState(RenderType.PARTICLES_TARGET)
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
          .setWriteMaskState(RenderStateShard.COLOR_WRITE)
          .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
          .setLightmapState(RenderStateShard.LIGHTMAP)
          .setOutputState(RenderType.PARTICLES_TARGET)
          .createCompositeState(false));

  public static final RenderType ALWAYS_VISIBLE_LINES = RenderType.create(
      "always_visible_lines",
      DefaultVertexFormat.POSITION_COLOR_NORMAL,
      VertexFormat.Mode.LINES,
      1536,
      RenderType.CompositeState.builder()
          .setShaderState(RenderType.RENDERTYPE_LINES_SHADER)
          .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty()))
          .setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING)
          .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
          .setOutputState(RenderType.ITEM_ENTITY_TARGET)
          .setWriteMaskState(RenderType.COLOR_DEPTH_WRITE)
          .setCullState(RenderType.NO_CULL)
          .setDepthTestState(RenderStateShard.NO_DEPTH_TEST)
          .createCompositeState(false)
  );

  public static final ResourceLocation DISSOLVE_TEXTURE = RootsAPI.rl("textures/misc/dissolve.png");
  public static final ResourceLocation ITEM_DISSOLVE_TEXTURE = RootsAPI.rl("textures/misc/item_dissolve.png");

  public static final RenderType DISSOLVE = RenderType.create(
      "roots_dissolve",
      DefaultVertexFormat.BLOCK,
      VertexFormat.Mode.QUADS,
      256,
      false,
      true,
      RenderType.CompositeState.builder()
          .setShaderState(DISSOLVE_SHADER)
          .setTextureState(new RenderStateShard.TextureStateShard(DISSOLVE_TEXTURE, false, false))
          .setTransparencyState(RenderType.NO_TRANSPARENCY)
          .setWriteMaskState(RenderType.DEPTH_WRITE)
          .setCullState(RenderType.NO_CULL)
          .setDepthTestState(RenderType.LEQUAL_DEPTH_TEST)
          .createCompositeState(false)
  );

  public static final Function<ResourceLocation, RenderType> ENTITY_CUTOUT_DISSOLVE = Util.memoize(s -> {
    RenderType.CompositeState state = RenderType.CompositeState.builder()
        .setShaderState(RENDERTYPE_ENTITY_CUTOUT_DISSOLVE_SHADER)
        .setTextureState(new RenderStateShard.TextureStateShard(s, false, false))
        .setTransparencyState(RenderType.NO_TRANSPARENCY)
        .setLightmapState(RenderType.LIGHTMAP)
        .setOverlayState(RenderType.OVERLAY)
        .createCompositeState(true);
    return RenderType.create("roots_entity_cutout_dissolve", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, state);
  });

  public static final RenderType BLOCK_SHEET_ENTITY_CUTOUT_DISSOLVE = ENTITY_CUTOUT_DISSOLVE.apply(TextureAtlas.LOCATION_BLOCKS);
  public static final RenderType CHEST_SHEET_ENTITY_CUTOUT_DISSOLVE = ENTITY_CUTOUT_DISSOLVE.apply(Sheets.CHEST_SHEET);

  public static final RenderType ITEM_ENTITY_TRANSLUCENT_CULL_DISSOLVE = RenderType.create("roots_item_entity_translucent_cull_dissolve, ", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, true, true, RenderType.CompositeState.builder()
      .setShaderState(RootsRenderTypes.RENDERTYPE_ENTITY_TRANSLUCENT_CULL_DISSOLVE_SHADER)
      .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
      .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
      .setLightmapState(RenderType.LIGHTMAP)
      .setOverlayState(RenderType.OVERLAY)
      .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
      .createCompositeState(true));

  public static final RenderType ENTITY_TRANSLUCENT_CULL_DISSOLVE = RenderType.create("roots_entity_translucent_cull_dissolve", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, true, true,
      RenderType.CompositeState.builder()
          .setShaderState(RootsRenderTypes.RENDERTYPE_ENTITY_TRANSLUCENT_CULL_DISSOLVE_SHADER)
          .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
          .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
          .setLightmapState(RenderType.LIGHTMAP)
          .setOverlayState(RenderType.OVERLAY)
          .createCompositeState(true));

  public static final RenderType ENTITY_TRANSLUCENT_DISSOLVE = RenderType.create("roots_entity_translucent_dissolve", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, true, true,
      RenderType.CompositeState.builder()
          .setShaderState(RootsRenderTypes.RENDERTYPE_ENTITY_TRANSLUCENT_DISSOLVE_SHADER)
          .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
          .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
          .setLightmapState(RenderType.LIGHTMAP)
          .setOverlayState(RenderType.OVERLAY)
          .createCompositeState(true));

  public static final Function<ResourceLocation, RenderType> ENTITY_NO_OUTLINE = Util.memoize(
      s -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
            .setShaderState(RENDERTYPE_ENTITY_NO_OUTLINE_DISSOLVE_SHADER)
            .setTextureState(new RenderStateShard.TextureStateShard(s, false, false))
            .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
            .setCullState(RenderStateShard.NO_CULL)
            .setLightmapState(RenderStateShard.LIGHTMAP)
            .setOverlayState(RenderStateShard.OVERLAY)
            .setWriteMaskState(RenderStateShard.COLOR_WRITE)
            .createCompositeState(false);
        return RenderType.create("roots_entity_no_outline_dissolve", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, false, true, rendertype$compositestate);
      }
  );

  public static final RenderType BANNER_SHEET_DISSOLVE = ENTITY_NO_OUTLINE.apply(Sheets.BED_SHEET);
  public static final RenderType SHIELD_SHEET_DISSOLVE = ENTITY_NO_OUTLINE.apply(Sheets.SHIELD_SHEET);

  public static final BiFunction<ResourceLocation, Boolean, RenderType> ENTITY_CUTOUT_NO_CULL = Util.memoize(
      (s, outline) -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
            .setShaderState(RENDERTYPE_ENTITY_CUTOUT_NO_CULL_DISSOLVE_SHADER)
            .setTextureState(new RenderStateShard.TextureStateShard(s, false, false))
            .setTransparencyState(RenderType.NO_TRANSPARENCY)
            .setCullState(RenderStateShard.NO_CULL)
            .setLightmapState(RenderStateShard.LIGHTMAP)
            .setOverlayState(RenderStateShard.OVERLAY)
            .createCompositeState(outline);
        return RenderType.create("roots_entity_cutout_no_cull_dissolve", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, true, false, rendertype$compositestate);
      }
  );

  public static final RenderType SHULKER_SHEET_DISSOLVE = ENTITY_CUTOUT_NO_CULL.apply(Sheets.SHULKER_SHEET, true);
  public static final RenderType SIGN_SHEET_DISSOLVE = ENTITY_CUTOUT_NO_CULL.apply(Sheets.SIGN_SHEET, true);

  public static final Function<ResourceLocation, RenderType> ENTITY_SOLID_DISSOLVE = Util.memoize(
      s -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
            .setShaderState(RENDERTYPE_ENTITY_SOLID_DISSOLVE_SHADER)
            .setTextureState(new RenderStateShard.TextureStateShard(s, false, false))
            .setTransparencyState(RenderType.NO_TRANSPARENCY)
            .setLightmapState(RenderStateShard.LIGHTMAP)
            .setOverlayState(RenderStateShard.OVERLAY)
            .createCompositeState(true);
        return RenderType.create("roots_entity_solid_dissolve", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, true, false, rendertype$compositestate);
      }
  );

  // ???
  public static final RenderType BLOCK_SHEET_ENTITY_SOLID_DISSOLVE = ENTITY_SOLID_DISSOLVE.apply(TextureAtlas.LOCATION_BLOCKS);
  public static final RenderType BED_SHEET_DISSOLVE = ENTITY_SOLID_DISSOLVE.apply(Sheets.BED_SHEET);

  private static final Map<RenderType, RenderType> DISSOLVE_DEPTH_MAP = new HashMap<>();

  public static RenderType getDissolveDepth(RenderType renderType) {
    RenderType result = DISSOLVE_DEPTH_MAP.get(renderType);
    if (result != null) {
      return result;
    }

    RenderType.CompositeState state = ((AccessorMixinCompositeRenderType) renderType).rootsGetState();


    RenderStateShard.EmptyTextureStateShard textureStateShard = ((AccessorMixinCompositeState) (Object) state).rootsGetTextureState();
    RenderStateShard.ShaderStateShard shaderStateShard = ((AccessorMixinCompositeState) (Object) state).rootsGetShaderState();
    RenderStateShard.TransparencyStateShard transparencyStateShard = ((AccessorMixinCompositeState) (Object) state).rootsGetTransparencyState();
    RenderStateShard.CullStateShard cullStateShard = ((AccessorMixinCompositeState) (Object) state).rootsGetCullState();
    RenderStateShard.LightmapStateShard lightmapStateShard = ((AccessorMixinCompositeState) (Object) state).rootsGetLightmapState();
    RenderStateShard.OverlayStateShard overlayStateShard = ((AccessorMixinCompositeState) (Object) state).rootsGetOverlayState();
    RenderStateShard.LayeringStateShard layeringStateShard = ((AccessorMixinCompositeState) (Object) state).rootsGetLayeringState();
    RenderStateShard.OutputStateShard outputStateShard = ((AccessorMixinCompositeState) (Object) state).rootsGetOutputState();
    RenderStateShard.TexturingStateShard texturingStateShard = ((AccessorMixinCompositeState) (Object) state).rootsGetTexturingState();
    RenderStateShard.WriteMaskStateShard writeMaskStateShard = ((AccessorMixinCompositeState) (Object) state).rootsGetWriteMaskState();
    RenderStateShard.LineStateShard lineStateShard = ((AccessorMixinCompositeState) (Object) state).rootsGetLineState();
    RenderStateShard.ColorLogicStateShard colorLogicStateShard = ((AccessorMixinCompositeState) (Object) state).rootsGetColorLogicState();
    RenderType.OutlineProperty outlineProperty = ((AccessorMixinCompositeState) (Object) state).rootsGetOutlineProperty();

    result = AccessorMixinCompositeRenderType.rootsCreateCompositeRenderType(
        renderType.name + "_dissolve_depth",
        renderType.format,
        renderType.mode,
        renderType.bufferSize,
        renderType.affectsCrumbling,
        renderType.sortOnUpload,
        AccessorMixinCompositeState.rootsCreateCompositeState(
            textureStateShard,
            shaderStateShard,
            transparencyStateShard,
            RenderStateShard.EQUAL_DEPTH_TEST,
            cullStateShard,
            lightmapStateShard,
            overlayStateShard,
            layeringStateShard,
            outputStateShard,
            texturingStateShard,
            writeMaskStateShard,
            lineStateShard,
            colorLogicStateShard,
            outlineProperty
        )
    );

    DISSOLVE_DEPTH_MAP.put(renderType, result);
    return result;
  }

/*  public static class TextureWithDissolveStateShard extends RenderStateShard.EmptyTextureStateShard {
    private final Optional<ResourceLocation> texture;
    protected boolean blur;
    protected boolean mipmap;

    public TextureWithDissolveStateShard(ResourceLocation texture, int index, boolean blur, boolean mipmap) {
      super(() -> {
        TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
        texturemanager.getTexture(texture).setFilter(blur, mipmap);
        RenderSystem.setShaderTexture(0, texture);
        texturemanager.getTexture(DISSOLVE_TEXTURE).setFilter(false, false);
        RenderSystem.setShaderTexture(index, DISSOLVE_TEXTURE);
      }, () -> {
      });
      this.texture = Optional.of(texture);
      this.blur = blur;
      this.mipmap = mipmap;
    }

    @Override
    public String toString() {
      return this.name + "[" + this.texture + "(blur=" + this.blur + ", mipmap=" + this.mipmap + ")]";
    }

    @Override
    protected Optional<ResourceLocation> cutoutTexture() {
      return this.texture;
    }
  }*/
}
