package mysticmods.roots.client.particle.bolt;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.resources.ResourceLocation;

public record RenderPreset(ResourceLocation name) {
  public static final RenderPreset LIGHTNING = new RenderPreset(RootsAPI.rl("roots_lightning"));
}
