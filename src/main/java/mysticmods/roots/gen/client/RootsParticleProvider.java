package mysticmods.roots.gen.client;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModParticles;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RootsParticleProvider implements DataProvider {
  public static final String PARTICLE_FOLDER = "particles";

  protected final PackOutput output;
  protected final String modid;
  protected Map<ResourceLocation, ParticleTemplate> generatedParticles = new HashMap<>();

  protected void registerParticles() {
    add(ModParticles.SINGLE_PIXEL, "simple1");
  }

  protected void add(Holder<ParticleType<?>> particle, String... textures) {
    add(particle, Arrays.stream(textures).map(this::modLoc).toList());
  }

  protected void add(Holder<ParticleType<?>> particle, List<ResourceLocation> textures) {
    add(particle.getKey().location().getPath(), textures);
  }

  protected void add(String name, String... textures) {
    add(name, Arrays.stream(textures).map(this::modLoc).toList());
  }

  protected void add(String name, List<ResourceLocation> textures) {
    generatedParticles.put(modLoc(name), new ParticleTemplate(modLoc(name), textures));
  }

  public RootsParticleProvider(PackOutput output) {
    Preconditions.checkNotNull(output);
    this.output = output;
    this.modid = RootsAPI.MODID;
  }

  public ResourceLocation modLoc(String name) {
    return ResourceLocation.fromNamespaceAndPath(modid, name);
  }

  protected void clear() {
    generatedParticles.clear();
  }

  @Override
  public CompletableFuture<?> run(CachedOutput cache) {
    clear();
    registerParticles();
    return generateAll(cache);
  }

  @Override
  public String getName() {
    return "Roots Particles Provider";
  }

  protected CompletableFuture<?> generateAll(CachedOutput cache) {
    CompletableFuture<?>[] futures = new CompletableFuture<?>[this.generatedParticles.size()];
    int i = 0;

    for (ParticleTemplate model : this.generatedParticles.values()) {
      Path target = getPath(model);
      futures[i++] = DataProvider.saveStable(cache, model.toJson(), target);
    }

    return CompletableFuture.allOf(futures);
  }

  protected Path getPath(ParticleTemplate model) {
    ResourceLocation loc = model.location();
    return this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(loc.getNamespace())
        .resolve(PARTICLE_FOLDER)
        .resolve(loc.getPath() + ".json");
  }

  protected record ParticleTemplate(ResourceLocation location, List<ResourceLocation> locations) {
    public JsonObject toJson() {
      JsonObject result = new JsonObject();
      JsonArray array = new JsonArray();
      for (ResourceLocation location : locations) {
        array.add(location.toString());
      }
      result.add("textures", array);
      return result;
    }
  }
}