package mysticmods.roots.client.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

public class HarvestParticle extends RootsParticle {
  private SpriteSet sprites;

  protected HarvestParticle(SpriteSet sprites, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int c1, int c2) {
    super(level, x, y, z);
    this.sprites = sprites;
    this.lifetime = 6;
    this.alpha = 0.7f;
    this.xd = 0;
    this.yd = 0;
    this.zd = 0;
    this.hasPhysics = false;
    this.quadSize = 0.7f;
    this.setSpriteFromAge(sprites);
  }

  @Override
  protected void updateColour(float f) {
  }

  @Override
  protected void updateAlpha(float f) {
  }

  @Override
  protected void updateQuadSize(float f) {
  }

  @Override
  protected void updateSprite(float f) {
    setSpriteFromAge(this.sprites);
  }

  @Override
  public ParticleRenderType getRenderType() {
    return RootsParticleRenderTypes.DELAYED_ADDITIVE;
  }

  @Override
  public FacingCameraMode getFacingCameraMode() {
    return RootsParticle.BILLBOARD_TILTED;
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new HarvestParticle(sprite, level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2());
      return particle;
    }
  }

}
