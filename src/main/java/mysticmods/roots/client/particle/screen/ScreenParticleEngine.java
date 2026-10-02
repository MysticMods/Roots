package mysticmods.roots.client.particle.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.particle.screen.base.ScreenParticle;
import mysticmods.roots.client.particle.screen.base.TextureSheetScreenParticle;
import mysticmods.roots.mixin.client.accessor.AccessorMixinClientHooks;
import mysticmods.roots.mixin.client.accessor.AccessorMixinParticleEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.function.BiConsumer;

public class ScreenParticleEngine {
  private static final Map<ParticleRenderType, List<ScreenParticle>> hudParticles = new HashMap<>();
  private static final Map<ParticleType<?>, ScreenParticleProvider<?>> particleTypes = new HashMap<>();

  private static final BiConsumer<ParticleRenderType, List<ScreenParticle>> particleConsumer = (type, particleList) -> {
    Iterator<ScreenParticle> iterator = particleList.iterator();
    while (iterator.hasNext()) {
      ScreenParticle next = iterator.next();
      next.tick();
      if (!next.isAlive()) {
        iterator.remove();
      }
    }
  };

  public static boolean hasAnyParticlesToRender (Map<ParticleRenderType, List<ScreenParticle>> map) {
    for (Map.Entry<ParticleRenderType, List<ScreenParticle>> entry : map.entrySet()) {
      if (!entry.getValue().isEmpty()) {
        return true;
      }
    }

    return false;
  }

  public static void tick() {
    // TODO: Check that this works
    // TODO: Do particles pause when the game is paused?
    Minecraft mc = Minecraft.getInstance();
    if (mc.isPaused()) {
      return;
    }
    hudParticles.forEach(particleConsumer);

    for (Screen screen : AccessorMixinClientHooks.roots$getGuiLayers()) {
      if (screen instanceof ScreenParticleSupplier particleScreen) {
        particleScreen.getParticles().forEach(particleConsumer);
      }
    }
  }

  public static void addHudParticle(ScreenParticle particle) {
    ParticleRenderType type = particle.getRenderType();
    hudParticles.computeIfAbsent(type, k -> new ArrayList<>()).add(particle);
  }

  public static <T extends ParticleOptions> void addHudParticle(T options, double x, double y, double xSpeed, double ySpeed) {
    TextureSheetScreenParticle particle = createParticle(options, x, y, xSpeed, ySpeed);
    if (particle != null) {
      addHudParticle(particle);
    }
  }

  protected static <T extends ParticleOptions> TextureSheetScreenParticle createParticle(T options, double x, double y, double xSpeed, double ySpeed) {
    ParticleType<?> type = options.getType();

    ScreenParticleProvider<T> provider;
    try {
      //noinspection unchecked
      provider = (ScreenParticleProvider<T>) particleTypes.get(type);
      if (provider == null) {
        throw new IllegalArgumentException("No particle provider registered for type: " + BuiltInRegistries.PARTICLE_TYPE.getKey(type));
      }
    } catch (ClassCastException e) {
      throw new IllegalArgumentException("Particle provider for type " + BuiltInRegistries.PARTICLE_TYPE.getKey(type) + " is not of the correct type.", e);
    }

    SpriteSet sprites = getSpriteSet(type);
    if (sprites == null) {
      throw new IllegalArgumentException("No sprite set found for particle type: " + BuiltInRegistries.PARTICLE_TYPE.getKey(type));
    }

    ClientLevel level = Minecraft.getInstance().level;
    return provider.createParticle(sprites, options, level, x, y, xSpeed, ySpeed);
  }

  public static SpriteSet getSpriteSet(ParticleType<?> type) {
    ResourceLocation key = BuiltInRegistries.PARTICLE_TYPE.getKey(type);
    return ((AccessorMixinParticleEngine) Minecraft.getInstance().particleEngine).roots$GetSpriteSets().get(key);
  }

  public static <T extends ParticleOptions> void register(ParticleType<T> type, ScreenParticleProvider<T> provider) {
    if (particleTypes.containsKey(type)) {
      throw new IllegalArgumentException("Particle type '" + type + "' is already registered.");
    }
    particleTypes.put(type, provider);
  }

  public static boolean hasHudParticles() {
    return hasAnyParticlesToRender(hudParticles);
  }

  private record ScreenParticleRenderer(Tesselator tesselator, TextureManager textureManager,
                                        float partialTicks) implements BiConsumer<ParticleRenderType, List<ScreenParticle>> {

    @Override
    public void accept(ParticleRenderType type, List<ScreenParticle> particleList) {
      if (!particleList.isEmpty()) {
        BufferBuilder buffer = type.begin(tesselator, textureManager);
        if (buffer == null) {
          RootsAPI.LOG.error("Failed to create buffer for particle type: '{}'", type);
          return;
        }
        for (ScreenParticle particle : particleList) {
          particle.render(buffer, partialTicks);
        }

        MeshData meshdata = buffer.build();
        if (meshdata != null) {
          BufferUploader.drawWithShader(meshdata);
        }
      }
    }
  }

  public static void renderHudParticles(float partialTicks) {
    RenderSystem.disableDepthTest();
    RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
    Tesselator tesselator = Tesselator.getInstance();
    TextureManager textureManager = Minecraft.getInstance().getTextureManager();

    ScreenParticleRenderer renderer = new ScreenParticleRenderer(tesselator, textureManager, partialTicks);

    hudParticles.forEach(renderer);

    RenderSystem.depthMask(true);
    RenderSystem.disableBlend();
  }

  // Fired via the `ClientTickHandler` `ScreenEvent.Renderer.Post` event
  public static void renderScreenParticles(Screen screen, float partialTicks) {
    if (!(screen instanceof ScreenParticleSupplier particleSupplier)) {
      return;
    }

    Map<ParticleRenderType, List<ScreenParticle>> particlesToRender = particleSupplier.getParticles();

    if (!hasAnyParticlesToRender(particlesToRender)) {
      return;
    }

    RenderSystem.disableDepthTest();
    RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
    Tesselator tesselator = Tesselator.getInstance();
    TextureManager textureManager = Minecraft.getInstance().getTextureManager();

    ScreenParticleRenderer renderer = new ScreenParticleRenderer(tesselator, textureManager, partialTicks);
    particlesToRender.forEach(renderer);

    RenderSystem.depthMask(true);
    RenderSystem.disableBlend();
  }
}
