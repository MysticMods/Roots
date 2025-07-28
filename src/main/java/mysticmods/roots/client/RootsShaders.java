package mysticmods.roots.client;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

public class RootsShaders {
  public static final ResourceLocation LOW_DISCARD_PARTICLE_SHADER_LOCATION = RootsAPI.rl("low_particle_discard");
  public static ShaderInstance LOW_DISCARD_PARTICLE_SHADER;

  public static final ResourceLocation SMART_CRUMBLING_SHADER_LOCATION = RootsAPI.rl("smart_crumbling");
  public static ShaderInstance SMART_CRUMBLING_SHADER;

  public static ShaderInstance getLowDiscardParticleShader() {
    return LOW_DISCARD_PARTICLE_SHADER;
  }

  public static ShaderInstance getSmartCrumblingShader() {
    return SMART_CRUMBLING_SHADER;
  }
}
