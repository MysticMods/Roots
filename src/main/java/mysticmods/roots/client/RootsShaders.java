package mysticmods.roots.client;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

public class RootsShaders {
  public static final ResourceLocation DISSOLVE_SHADER_LOCATION = RootsAPI.rl("dissolve");
  public static final ResourceLocation LOW_DISCARD_PARTICLE_SHADER_LOCATION = RootsAPI.rl("low_particle_discard");
  public static final ResourceLocation RENDERTYPE_ENTITY_CUTOUT_DISSOLVE = RootsAPI.rl("rendertype_entity_cutout_dissolve");
  public static final ResourceLocation RENDERTYPE_ENTITY_TRANSLUCENT_CULL_DISSOLVE = RootsAPI.rl("rendertype_entity_translucent_cull_dissolve");
  public static ShaderInstance LOW_DISCARD_PARTICLE_SHADER;
  public static ShaderInstance DISSOLVE_SHADER;
  public static ShaderInstance RENDERTYPE_ENTITY_CUTOUT_DISSOLVE_SHADER;
  public static ShaderInstance RENDERTYPE_ENTITY_TRANSLUCENT_CULL_DISSOLVE_SHADER;

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
}
