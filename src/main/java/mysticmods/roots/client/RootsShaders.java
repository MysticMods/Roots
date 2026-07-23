package mysticmods.roots.client;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

public class RootsShaders {
  public static final ResourceLocation DISSOLVE_SHADER_LOCATION = RootsAPI.rl("dissolve");
  public static final ResourceLocation LOW_DISCARD_PARTICLE_SHADER_LOCATION = RootsAPI.rl("low_particle_discard");
  public static final ResourceLocation CARDIOID_PARTICLE_SHADER_LOCATION = RootsAPI.rl("cardioid_particle");
  public static final ResourceLocation RENDERTYPE_ENTITY_CUTOUT_DISSOLVE = RootsAPI.rl("rendertype_entity_cutout_dissolve");
  public static final ResourceLocation RENDERTYPE_ENTITY_TRANSLUCENT_CULL_DISSOLVE = RootsAPI.rl("rendertype_entity_translucent_cull_dissolve");
  public static final ResourceLocation RENDERTYPE_ENTITY_NO_OUTLINE_DISSOLVE = RootsAPI.rl("rendertype_entity_no_outline_dissolve");
  public static final ResourceLocation RENDERTYPE_ENTITY_CUTOUT_NO_CULL_DISSOLVE = RootsAPI.rl("rendertype_entity_cutout_no_cull_dissolve");
  public static final ResourceLocation RENDERTYPE_ENTITY_SOLID_DISSOLVE = RootsAPI.rl("rendertype_entity_solid_dissolve");
  public static final ResourceLocation RENDERTYPE_ENTITY_TRANSLUCENT_DISSOLVE = RootsAPI.rl("rendertype_entity_translucent_dissolve");
  public static ShaderInstance CARDIOID_PARTICLE_SHADER;
  public static ShaderInstance LOW_DISCARD_PARTICLE_SHADER;
  public static ShaderInstance DISSOLVE_SHADER;
  public static ShaderInstance RENDERTYPE_ENTITY_CUTOUT_DISSOLVE_SHADER;
  public static ShaderInstance RENDERTYPE_ENTITY_TRANSLUCENT_CULL_DISSOLVE_SHADER;
  public static ShaderInstance RENDERTYPE_ENTITY_NO_OUTLINE_DISSOLVE_SHADER;
  public static ShaderInstance RENDERTYPE_ENTITY_CUTOUT_NO_CULL_DISSOLVE_SHADER;
  public static ShaderInstance RENDERTYPE_ENTITY_SOLID_DISSOLVE_SHADER;
  public static ShaderInstance RENDERTYPE_ENTITY_TRANSLUCENT_DISSOLVE_SHADER;

  public static ShaderInstance getLowDiscardParticleShader() {
    return LOW_DISCARD_PARTICLE_SHADER;
  }

  public static ShaderInstance getDissolveShader() {
    return DISSOLVE_SHADER;
  }

  public static ShaderInstance getRenderTypeEntityCutoutDissolveShader() {
    return RENDERTYPE_ENTITY_CUTOUT_DISSOLVE_SHADER;
  }

  public static ShaderInstance getRenderTypeEntityTranslucentCullDissolveShader() {
    return RENDERTYPE_ENTITY_TRANSLUCENT_CULL_DISSOLVE_SHADER;
  }

  public static ShaderInstance getRenderTypeEntityNoOutlineDissolveShader() {
    return RENDERTYPE_ENTITY_NO_OUTLINE_DISSOLVE_SHADER;
  }

  public static ShaderInstance getRenderTypeEntityCutoutNoCullDissolveShader() {
    return RENDERTYPE_ENTITY_CUTOUT_NO_CULL_DISSOLVE_SHADER;
  }

  public static ShaderInstance getRenderTypeEntitySolidDissolveShader() {
    return RENDERTYPE_ENTITY_SOLID_DISSOLVE_SHADER;
  }

  public static ShaderInstance getRenderTypeEntityTranslucentDissolveShader() {
    return RENDERTYPE_ENTITY_TRANSLUCENT_DISSOLVE_SHADER;
  }

  public static ShaderInstance getCardioidParticleShader () {
    return CARDIOID_PARTICLE_SHADER;
  }
}
