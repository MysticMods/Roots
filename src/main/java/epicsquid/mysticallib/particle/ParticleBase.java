package epicsquid.mysticallib.particle;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class ParticleBase extends Particle implements IParticle {
  public static Map<ResourceLocation, TextureAtlasSprite> CACHE = new HashMap<>();

  protected int lifetime = 0;

  public ParticleBase(@Nonnull World world, double x, double y, double z, double vx, double vy, double vz, double[] data) {
    super(world, x, y, z, 0, 0, 0);
    this.motionX = vx;
    this.motionY = vy;
    this.motionZ = vz;
    if (data.length >= 1) {
      lifetime = (int) data[0];
    }
    this.particleMaxAge = lifetime;
    ResourceLocation texture = ParticleRegistry.getTexture(this.getClass());

    TextureAtlasSprite sprite = CACHE.get(texture);
    if (sprite == null) {
      sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texture.toString());
      CACHE.put(texture, sprite);
    }
    this.setParticleTexture(sprite);
    this.particleScale = 1.0f;
    this.canCollide = false;
  }

  @Override
  public void onUpdate() {
    super.onUpdate();
    this.motionX *= 0.95f;
    this.motionY *= 0.95f;
    this.motionZ *= 0.95f;
    lifetime--;
  }

  protected void onUpdateNoMotion() {
    super.onUpdate();
    lifetime--;
  }

  protected void setDead() {
    this.lifetime = 0;
  }

  @Override
  public int getFXLayer() {
    return 1;
  }

  @Override
  public boolean alive() {
    return lifetime > 0;
  }

  @Override
  public boolean isAdditive() {
    return false;
  }

  @Override
  public boolean renderThroughBlocks() {
    return false;
  }
}
