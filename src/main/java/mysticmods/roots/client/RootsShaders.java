package mysticmods.roots.client;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

public class RootsShaders {
  public static final ResourceLocation LOW_DISCARD_PARTICLE_SHADER_LOCATION = RootsAPI.rl("low_particle_discard");
  public static final ResourceLocation CRUMBLE_SHADER_LOCATION = RootsAPI.rl("crumble_dissolve");
  public static ShaderInstance LOW_DISCARD_PARTICLE_SHADER;
  public static ShaderInstance CRUMBLE_SHADER;

  public static ShaderInstance getLowDiscardParticleShader() {
    return LOW_DISCARD_PARTICLE_SHADER;
  }

  public static ShaderInstance getCrumblingShader () {
    return CRUMBLE_SHADER;
  }
}
